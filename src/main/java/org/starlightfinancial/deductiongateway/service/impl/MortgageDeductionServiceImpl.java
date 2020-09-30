package org.starlightfinancial.deductiongateway.service.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.config.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.service.AutoAccountUploadService;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.utility.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 批量扣款
 *
 * @author sili.chen
 */
@Service
public class MortgageDeductionServiceImpl implements MortgageDeductionService {


    private static final Logger log = LoggerFactory.getLogger(MortgageDeductionService.class);
    private static final Map<String, String> COLUMN_NAME_MAP = new HashMap<>();

    static {
        COLUMN_NAME_MAP.put("业务编号", "contractNo");
        COLUMN_NAME_MAP.put("银行名称", "param1");
        COLUMN_NAME_MAP.put("卡折标识", "param2");
        COLUMN_NAME_MAP.put("银行卡号", "param3");
        COLUMN_NAME_MAP.put("姓名", "param4");
        COLUMN_NAME_MAP.put("证件类型", "param5");
        COLUMN_NAME_MAP.put("证件号码", "param6");
        COLUMN_NAME_MAP.put("分账户数据1", "splitData1");
        COLUMN_NAME_MAP.put("分账户数据2", "splitData2");
        COLUMN_NAME_MAP.put("服务费管理司", "target");
    }

    @Autowired
    MortgageDeductionRepository mortgageDeductionRepository;
    @Autowired
    SysDictRepository sysDictRepository;
    @Autowired
    private ChinaPayConfig chinaPayConfig;
    @Autowired
    private BeanConverter beanConverter;

    @Override
    public void importCustomerData(MultipartFile uploadedFile, int staffId) {
        String fileName = uploadedFile.getOriginalFilename();
        Workbook workbook = null;
        try (
                //获取excel文件的io流
                InputStream is = uploadedFile.getInputStream()) {
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(PoiUtil.XLS)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(PoiUtil.XLSX)) {
                //2007
                workbook = new XSSFWorkbook(is);
            } else {
                log.info("手动代扣上传的文件格式不是正确,上传的文件后缀为:{}"
                        , uploadedFile.getOriginalFilename().substring(uploadedFile.getOriginalFilename().lastIndexOf(".")));
                return;
            }
            readExcel(workbook, staffId);
        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
            log.error("手动代扣上传文件导入失败", e);
        }
    }

    /**
     * 读取代扣excel
     *
     * @param workbook excel工作簿
     * @param staffId  操作人id
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IOException
     */
    private void readExcel(Workbook workbook, int staffId) throws NoSuchFieldException, IllegalAccessException, IOException {
        List<MortgageDeduction> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        Map<Integer, String> colIndexMap = new HashMap<>();
        List<SysDict> openBankList = sysDictRepository.findByDicType(DictionaryType.MERID_SOURCE);
        List<SysDict> cTypeLst = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
        if (null != sheet) {
            Row firstRow = sheet.getRow(sheet.getFirstRowNum());
            for (int i = firstRow.getFirstCellNum(); i < firstRow.getLastCellNum(); i++) {
                Cell cell = firstRow.getCell(i);
                if (CellType.STRING.compareTo(cell.getCellTypeEnum()) == 0 && COLUMN_NAME_MAP.containsKey(StringUtils.trimToEmpty(cell.getStringCellValue()))) {
                    String columnName = COLUMN_NAME_MAP.get(StringUtils.trimToEmpty(cell.getStringCellValue()));
                    if (StringUtils.isNotBlank(columnName)) {
                        colIndexMap.put(i, columnName);
                    }
                }
            }
            outterloop:
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (null != row) {
                    MortgageDeduction mortgageDeduction = new MortgageDeduction();
                    for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        if (StringUtils.isEmpty(ExcelReader.getCellFormatValue(cell))) {
                            continue outterloop;
                        }
                        if (colIndexMap.containsKey(j)) {
                            String fieldName = colIndexMap.get(j);
                            Field field = MortgageDeduction.class.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            if (field.getType() == String.class) {
                                String value = "";
                                if (CellType.STRING.compareTo(cell.getCellTypeEnum()) == 0) {
                                    value = StringUtils.trimToEmpty(cell.getStringCellValue());
                                } else if (CellType.NUMERIC.compareTo(cell.getCellTypeEnum()) == 0) {
                                    value = cell.getNumericCellValue() + "";
                                    if ("0.0".equals(value)) {
                                        value = "0";
                                    }
                                }
                                field.set(mortgageDeduction, value);
                            }
                            if (field.getType() == BigDecimal.class) {
                                BigDecimal value = BigDecimal.valueOf(cell.getNumericCellValue());
                                field.set(mortgageDeduction, value);
                            }
                        }
                    }
                    mortgageDeduction.setIssuccess("2");
                    mortgageDeduction.setType("1");
                    mortgageDeduction.setPlanNo(-1);
                    mortgageDeduction.setCreateDate(new Date());
                    mortgageDeduction.setSplitType("0001");
                    mortgageDeduction.setCreatId(staffId);
                    mortgageDeduction.setCustomerName(mortgageDeduction.getParam4());
                    if (StringUtils.isNotBlank(mortgageDeduction.getParam2()) && "卡".equals(mortgageDeduction.getParam2().trim())) {
                        mortgageDeduction.setParam2("0");
                    } else {
                        mortgageDeduction.setParam2("0");
                    }
                    // 处理开户行
                    for (int k = 0; k < openBankList.size(); k++) {
                        if (mortgageDeduction.getParam1().equals(openBankList.get(k).getDicValue())) {
                            mortgageDeduction.setParam1(openBankList.get(k).getDicKey());
                            break;
                        }
                    }
                    // 处理证件类型
                    for (int k = 0; k < cTypeLst.size(); k++) {
                        if (mortgageDeduction.getParam5().equals(cTypeLst.get(k).getDicValue())) {
                            mortgageDeduction.setParam5(cTypeLst.get(k).getDicKey());
                            break;
                        }
                    }
                    // 处理服务费管理公司
                    if (StringUtils.equals(mortgageDeduction.getTarget(), "悦至渝")) {
                        mortgageDeduction.setTarget("悦至渝");
                    } else if (StringUtils.equals(mortgageDeduction.getTarget(), "铠岳")) {
                        mortgageDeduction.setTarget("铠岳");
                    } else if (StringUtils.equals(mortgageDeduction.getTarget(), "润坤")) {
                        mortgageDeduction.setTarget("润坤");
                    } else {
                        mortgageDeduction.setTarget("悦至渝");
                    }
                    // 设置是否上传自动入账表格
                    mortgageDeduction.setIsUploaded("0");
                    if (mortgageDeduction.getParam3() != null && !"".equals(mortgageDeduction.getParam3())) {
                        if (mortgageDeduction.getSplitData1().compareTo(BigDecimal.ZERO) != 0
                                && mortgageDeduction.getSplitData2().compareTo(BigDecimal.ZERO) != 0) {
                            List<MortgageDeduction> deductionList = splitServiceMoney(mortgageDeduction);
                            deductionList.forEach(deduction -> list.add(deduction));
                        } else {
                            list.add(mortgageDeduction);
                        }
                    }
                }
            }
            workbook.close();
            mortgageDeductionRepository.save(list);
        }

    }

    /**
     * @param mortgageDeduction 原始上传的代扣记录
     * @return
     * @Author sili.chen
     * @Description 拷贝对象，将本息和服务费拆分成单独的两条
     * @Date 15:42 2020/9/9
     */
    private List splitServiceMoney(MortgageDeduction mortgageDeduction) {
        List<MortgageDeduction> list = new ArrayList<>();
        MortgageDeduction mortgageDeduction1 = mortgageDeduction;
        MortgageDeduction mortgageDeduction2 = mortgageDeduction.cloneSelf();
        mortgageDeduction1.setSplitData2(new BigDecimal(0));
        mortgageDeduction2.setSplitData1(new BigDecimal(0));
        list.add(mortgageDeduction1);
        list.add(mortgageDeduction2);
        return list;
    }

    /**
     * 查询代扣数据
     *
     * @param startDate
     * @param endDate
     * @param customerName
     * @param pageBean
     * @param type         0:已执行代扣,1:未代扣数据
     * @param contractNo   合同编号
     * @param creatid
     * @return
     */
    @Override
    public PageBean queryMortgageDeductionData(Date startDate, Date endDate, String customerName, PageBean pageBean, String type, String contractNo, int creatid) {

        Specification<MortgageDeduction> specification = new Specification<MortgageDeduction>() {
            @Override
            public Predicate toPredicate(Root<MortgageDeduction> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                criteriaQuery.distinct(true);
                //客户名非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(customerName)) {
                    predicates.add(criteriaBuilder.equal(root.get("customerName"), customerName));
                }
                //合同号非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(contractNo)) {
                    predicates.add(criteriaBuilder.equal(root.get("contractNo"), contractNo));
                }
                String[] typeArr = type.split(",");
                predicates.add(criteriaBuilder.in(root.get("type")).value(Arrays.asList(typeArr)));

                //代扣结果查询:typeArr为[0,1],代扣执行页面查询:typeArr为[1]
                if (typeArr.length == 1) {
                    predicates.add(criteriaBuilder.equal(root.get("creatId"), creatid));
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate));
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
                } else {
                    Predicate payTimeStart = criteriaBuilder.greaterThanOrEqualTo(root.get("payTime"), startDate);
                    Predicate payTimeEnd = criteriaBuilder.lessThanOrEqualTo(root.get("payTime"), endDate);

                    Predicate createDateStart = criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate);
                    Predicate createDateEnd = criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate);
                    Predicate predicateStart = criteriaBuilder.or(payTimeStart, createDateStart);
                    Predicate predicateEnd = criteriaBuilder.or(payTimeEnd, createDateEnd);
                    predicates.add(criteriaBuilder.and(predicateStart, predicateEnd));

                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));

            }
        };

        long count = mortgageDeductionRepository.count(specification);
        if (count == 0) {
            //如果查询出来的总记录数为0,直接返回null,避免后续查询代码执行
            return null;
        }
        double tempTotalPageCount = count / (pageBean.getPageSize().doubleValue());
        double totalPageCount = Math.ceil(tempTotalPageCount == 0 ? 1 : tempTotalPageCount);
        if (totalPageCount < pageBean.getPageNumber()) {
            pageBean.setPageNumber(1);
        }
        PageRequest pageRequest = buildPageRequest(pageBean, type);
        Page<MortgageDeduction> page = mortgageDeductionRepository.findAll(specification, pageRequest);
        if (page.hasContent()) {
            List<MortgageDeduction> mortgageDeductionList = page.getContent();
            List<SysDict> sysDicts = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
            for (MortgageDeduction mortgageDeduction : mortgageDeductionList) {

                //处理证件类型
                for (SysDict sysDict : sysDicts) {
                    if (mortgageDeduction.getParam5().equals(sysDict.getDicKey())) {
                        mortgageDeduction.setParam5(sysDict.getDicValue());
                    }
                }

                //处理服务费管理公司:最初数据库保存的是ChinaPayClassicDeduction 对应的服务费公司商户号(数字), 对接银联新无卡重构代码后保存的是公司名称(汉字)
                if (mortgageDeduction.getTarget() != null && StringUtils.isNumeric(mortgageDeduction.getTarget())) {
                    if (chinaPayConfig.getClassicKaiYueMemberId().equals(mortgageDeduction.getTarget().trim())) {
                        mortgageDeduction.setTarget("铠岳");
                    } else if (chinaPayConfig.getClassicRunKunMemberId().equals(mortgageDeduction.getTarget().trim())) {
                        mortgageDeduction.setTarget("润坤");
                    }
                }


                //处理代扣卡银行名
                String bankName = BankCodeEnum.getBankNameById(mortgageDeduction.getParam1());
                if (bankName != null) {
                    mortgageDeduction.setParam1(bankName);
                }
            }
            pageBean.setRows(mortgageDeductionList);
            pageBean.setTotal(page.getTotalElements());
            return pageBean;
        }
        return null;
    }

    /**
     * 根据id查询MortgageDeduction数据
     *
     * @param ids
     * @return
     */
    @Override
    public List<MortgageDeduction> findMortgageDeductionListByIds(String ids) {
        String[] idsStrArr = ids.split(",");
        ArrayList<Integer> idsList = new ArrayList<>();
        for (String id : idsStrArr) {
            idsList.add(Integer.parseInt(id));
        }
        List<MortgageDeduction> mortgageDeductionList = mortgageDeductionRepository.findAll(idsList);
        return mortgageDeductionList;
    }


    /**
     * 导出代扣结果excel
     *
     * @param startDate
     * @param endDate
     * @param customerName
     * @return
     */
    @Override
    public Workbook exportXLS(Date startDate, Date endDate, String customerName) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("扣款统计");
        //表头
        String[] headers = new String[]
                {"订单号", "客户名称", "合同编号", "还款日期", "开户行", "卡号/折号", "账数据１金额（元）", "分账数据２金额（元）",
                        "收分账数据２的公司", "持卡人姓名", "证件号", "扣款结果", "原因", "对账结果", "代扣渠道", "银联分账数据１扣款(分)", "银联分账数据2扣款(分)"};
        sheet.setDefaultColumnWidth(16);
        HSSFRow rowHead1 = sheet.createRow(0);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = rowHead1.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellStyle);
        }

        HSSFRow row = null;
        HSSFCell cell = null;
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFCellStyle numericStyle = workbook.createCellStyle();
        numericStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        int i = 1;

        Specification<MortgageDeduction> specification = new Specification<MortgageDeduction>() {
            @Override
            public Predicate toPredicate(Root<MortgageDeduction> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("payTime"), startDate));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("payTime"), endDate));
                predicates.add(criteriaBuilder.equal(root.get("type"), "0"));
                //客户名非空判断。不为空则加此条件
                if (StringUtils.isNotEmpty(customerName)) {
                    predicates.add(criteriaBuilder.equal(root.get("customerName"), customerName));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("payTime")));
                return criteriaQuery.getRestriction();
            }
        };
        List<MortgageDeduction> listCustomer = mortgageDeductionRepository.findAll(specification);
        for (MortgageDeduction mortgageDeduction : listCustomer) {
            row = sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(mortgageDeduction.getOrdId());
            cell = row.createCell(1);
            cell.setCellValue(mortgageDeduction.getCustomerName());
            cell = row.createCell(2);
            cell.setCellValue(mortgageDeduction.getContractNo());
            cell = row.createCell(3);
            cell.setCellValue(Utility.convertToString(mortgageDeduction.getCreateDate(), "yyyy-MM-dd"));
            cell = row.createCell(4);
            Optional<String> bankName = Optional.ofNullable(BankCodeEnum.getBankNameById(mortgageDeduction.getParam1()));
            cell.setCellValue(bankName.orElse(mortgageDeduction.getParam1()));
            cell = row.createCell(5);
            cell.setCellValue(mortgageDeduction.getParam3());
            cell = row.createCell(6);
            if (Utility.checkBigDecimal2(mortgageDeduction.getSplitData1())) {
                cell.setCellValue(mortgageDeduction.getSplitData1().toString());
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(7);
            if (Utility.checkBigDecimal2(mortgageDeduction.getSplitData2())) {
                cell.setCellValue(mortgageDeduction.getSplitData2().toString());
            } else {
                cell.setCellValue("");
            }

            cell = row.createCell(8);
            String company = mortgageDeduction.getTarget() != null ? mortgageDeduction.getTarget().trim() : "";
            //处理服务费管理公司:最初数据库保存的是ChinaPayClassicDeduction 对应的服务费公司商户号(数字), 对接银联新无卡重构代码后保存的是公司名称(汉字)
            if (mortgageDeduction.getTarget() != null && StringUtils.isNumeric(mortgageDeduction.getTarget())) {
                //为数字的时候需要转换
                if (chinaPayConfig.getClassicKaiYueMemberId().equals(mortgageDeduction.getTarget().trim())) {
                    cell.setCellValue("铠岳");
                } else if (chinaPayConfig.getClassicRunKunMemberId().equals(mortgageDeduction.getTarget().trim())) {
                    cell.setCellValue("润坤");
                }
            } else {
                //为汉字的情况,直接设置
                cell.setCellValue(company);
            }

            cell = row.createCell(9);
            cell.setCellValue(mortgageDeduction.getParam4());
            cell = row.createCell(10);
            cell.setCellValue(mortgageDeduction.getParam6());
            cell = row.createCell(11);
            if (mortgageDeduction.getIssuccess() == null || StringUtils.equals(mortgageDeduction.getIssuccess(), "0")) {
                cell.setCellValue("扣款失败");
            } else if (StringUtils.equals(mortgageDeduction.getIssuccess(), "1")) {
                cell.setCellValue("扣款成功");
            } else {
                cell.setCellValue("暂无结果");
            }

            cell = row.createCell(12);
            cell.setCellValue(mortgageDeduction.getErrorResult());

            cell = row.createCell(13);
            String checkState = mortgageDeduction.getCheckState() == null ? "" : mortgageDeduction.getCheckState();
            if ("0".equals(checkState)) {
                cell.setCellValue("不一致");
            } else if ("1".equals(checkState)) {
                cell.setCellValue("一致");
            } else {
                cell.setCellValue("未对账");
            }

            cell = row.createCell(14);
            String deductionChannel = DeductionChannelEnum.getDescByCode(mortgageDeduction.getChannel());
            if (deductionChannel == null) {
                deductionChannel = mortgageDeduction.getOrderDesc();
            }
            cell.setCellValue(deductionChannel);
            cell = row.createCell(15);
            if (Utility.checkBigDecimal2(mortgageDeduction.getRsplitData1())) {
                cell.setCellValue(mortgageDeduction.getRsplitData1().toString());
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(16);
            if (Utility.checkBigDecimal2(mortgageDeduction.getRsplitData2())) {
                cell.setCellValue(mortgageDeduction.getRsplitData2().toString());
            } else {
                cell.setCellValue("");
            }
            i = i + 1;
        }
        return workbook;
    }

    /**
     * 更新代扣数据
     *
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMortgageDeductions(List<MortgageDeduction> list) {
        for (MortgageDeduction mortgageDeduction : list) {
            mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
        }
    }

    /**
     * 删除代扣数据
     *
     * @param list
     */
    @Override
    public void deleteMortgageDeductions(List<MortgageDeduction> list) {
        for (MortgageDeduction mortgageDeduction : list) {
            mortgageDeductionRepository.delete(mortgageDeduction);
        }
    }

    /**
     * 根据订单号查询
     *
     * @param ordId
     * @return
     */
    @Override
    public MortgageDeduction findByOrdId(String ordId) {
        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.findByOrdId(ordId);
        return mortgageDeduction;
    }

    /**
     * 更新代扣数据
     *
     * @param mortgageDeduction
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMortgageDeduction(MortgageDeduction mortgageDeduction) {
        mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
    }

    /**
     * 自动上传代扣成功的记录
     */
    @Override
    public void uploadAutoAccountingFile() throws IOException, ClassNotFoundException {
        Date yesterday = Utility.toMidNight(Utility.addDay(new Date(), -1));
        List<MortgageDeduction> original = mortgageDeductionRepository.findByIsUploadedAndIssuccessAndPayTimeAfterOrderByPayTimeDesc(String.valueOf(0), String.valueOf(1), yesterday);
        List<MortgageDeduction> mortgageDeductions = Utility.deepCopy(original);
        if (mortgageDeductions.size() <= 0) {
            log.info("暂无需要上传的代扣记录");
            return;
        }
        //合并同一个合同号的多条代扣记录
        HashMap<String, MortgageDeduction> contractNoMortgageDeductionMap = new LinkedHashMap<>(32);
        mortgageDeductions.forEach(
                mortgageDeduction -> {
                    MortgageDeduction value = contractNoMortgageDeductionMap.get(mortgageDeduction.getContractNo());
                    if (value == null) {
                        contractNoMortgageDeductionMap.put(mortgageDeduction.getContractNo(), mortgageDeduction);
                    } else {
                        value.setSplitData1(value.getSplitData1().add(mortgageDeduction.getSplitData1()));
                        value.setSplitData2(value.getSplitData2().add(mortgageDeduction.getSplitData2()));
                    }
                }
        );
        Collection<MortgageDeduction> noDuplicateMortgageDeductionCollections = contractNoMortgageDeductionMap.values();
        //转换list
        ArrayList<MortgageDeduction> noDuplicateMortgageDeductions = new ArrayList<>(noDuplicateMortgageDeductionCollections);
        List<AutoAccountingExcelRow> autoAccountingExcelRows = noDuplicateMortgageDeductions.stream().map(mortgageDeduction -> beanConverter.transToAutoAccountingExcelRow(mortgageDeduction)).collect(Collectors.toList());
        //生成Excel
        HSSFWorkbook workbook = AutoAccountUploadService.createWorkBook(autoAccountingExcelRows);
        try {
            AutoAccountUploadService.upload(workbook);
        } catch (IOException e) {
            log.error("上传自动入账excel文档失败", e);
            return;
        }

        //设置为已上传,并更新
        original.forEach(
                mortgageDeduction -> {
                    mortgageDeduction.setIsUploaded(String.valueOf("1"));
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                }
        );

    }


    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(PageBean pageBean, String type) {
        String[] typeArr = type.split(",");
        Sort sort = null;
        //代扣结果查询:typeArr为[0,1],代扣执行页面查询:typeArr为[1]
        if (typeArr.length == 1) {
            sort = new Sort(Sort.Direction.ASC, "id");
        } else {
            sort = new Sort(Sort.Direction.DESC, "payTime", "id");
        }
        Integer pageNumber = pageBean.getPageNumber();
        Integer pageSize = pageBean.getPageSize();
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }


}
