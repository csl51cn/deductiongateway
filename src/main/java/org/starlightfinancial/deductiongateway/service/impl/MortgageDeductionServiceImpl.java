package org.starlightfinancial.deductiongateway.service.impl;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.enums.RsbCodeEnum;
import org.starlightfinancial.deductiongateway.enums.ChinaPayReturnCodeEnum;
import org.starlightfinancial.deductiongateway.service.AssemblerFactory;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.utility.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 批量扣款
 *
 * @author sili.chen
 */
@Service
public class MortgageDeductionServiceImpl implements MortgageDeductionService {

    @Autowired
    MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    SysDictRepository sysDictRepository;

    @Autowired
    private LimitManagerRepository limitManagerRepository;

    @Autowired
    private AssemblerFactory assemblerFactory;

    @Autowired
    private ChinaPayConfig chinaPayConfig;

    @Autowired
    private BaofuConfig baofuConfig;


    private static final Logger log = LoggerFactory.getLogger(MortgageDeductionService.class);

    @Override
    public void importCustomerData(MultipartFile uploadedFile, int staffId) {
        try {
            if (null == uploadedFile) {
                log.info("");
                return;
            }
            if (!StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xls") && !StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xlsx")) {
                log.info("");
                return;
            }

            if (StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xls")) {
                this.readXls(uploadedFile, staffId);
            } else if (StringUtils
                    .endsWith(uploadedFile.getOriginalFilename(), ".xlsx")) {
                this.readXlsx(uploadedFile, staffId);
            }
        } catch (Exception e) {
            log.error("");
            e.printStackTrace();
        }
    }

    private void readXls(MultipartFile uploadedFile, int staffId) throws IllegalAccessException, NoSuchFieldException, IOException {
        try {
            List<MortgageDeduction> list = new ArrayList<>();
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(uploadedFile.getInputStream());
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

            Map<Integer, String> colIndexMap = new HashMap<>();
            Map<String, String> colNameMap = new HashMap<>();
            colNameMap.put("业务编号", "contractNo");
            colNameMap.put("银行名称", "param1");
            colNameMap.put("卡折标识", "param2");
            colNameMap.put("银行卡号", "param3");
            colNameMap.put("姓名", "param4");
            colNameMap.put("证件类型", "param5");
            colNameMap.put("证件号码", "param6");
            colNameMap.put("分账户数据1", "splitData1");
            colNameMap.put("分账户数据2", "splitData2");
            colNameMap.put("服务费管理司", "target");

            List<SysDict> openBankList = sysDictRepository.findByDicType(DictionaryType.MERID_SOURCE);
            List<SysDict> cTypeLst = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);

            if (null != hssfSheet) {
                HSSFRow firstRow = hssfSheet.getRow(hssfSheet.getFirstRowNum());
                for (int i = firstRow.getFirstCellNum(); i < firstRow.getLastCellNum(); i++) {
                    HSSFCell hssfCell = firstRow.getCell(i);
                    if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING && colNameMap.containsKey(StringUtils.trimToEmpty(hssfCell.getStringCellValue()))) {
                        String columnName = colNameMap.get(StringUtils.trimToEmpty(hssfCell.getStringCellValue()));
                        if (StringUtils.isNotBlank(columnName)) {
                            colIndexMap.put(i, columnName);
                        }
                    }
                }

                outterloop:
                for (int i = hssfSheet.getFirstRowNum() + 1; i <= hssfSheet.getPhysicalNumberOfRows(); i++) {
                    HSSFRow hssfRow = hssfSheet.getRow(i);
                    if (null != hssfRow) {
                        MortgageDeduction mortgageDeduction = new MortgageDeduction();
                        for (int j = hssfRow.getFirstCellNum(); j < hssfRow.getLastCellNum(); j++) {
                            HSSFCell hssfCell = hssfRow.getCell(j);
                            if (StringUtils.isEmpty(ExcelReader.getCellFormatValue(hssfCell)))
                                continue outterloop;
                            if (colIndexMap.containsKey(j)) {
                                String fieldName = colIndexMap.get(j);
                                Field field = MortgageDeduction.class.getDeclaredField(fieldName);
                                field.setAccessible(true);
                                if (field.getType() == String.class) {
                                    String value = "";
                                    if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                        value = StringUtils.trimToEmpty(hssfCell.getStringCellValue());
                                    } else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                        value = hssfCell.getNumericCellValue() + "";
                                        if ("0.0".equals(value)) {
                                            value = "0";
                                        }
                                    }
                                    field.set(mortgageDeduction, value);
                                }
                                if (field.getType() == BigDecimal.class) {
                                    BigDecimal value = BigDecimal.valueOf(hssfCell.getNumericCellValue());
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


                        //处理开户行
                        for (int k = 0; k < openBankList.size(); k++) {
                            if (mortgageDeduction.getParam1().equals(openBankList.get(k).getDicValue())) {
                                mortgageDeduction.setParam1(openBankList.get(k).getDicKey());
                                break;
                            }
                        }

                        //处理证件类型
                        for (int k = 0; k < cTypeLst.size(); k++) {
                            if (mortgageDeduction.getParam5().equals(cTypeLst.get(k).getDicValue())) {
                                mortgageDeduction.setParam5(cTypeLst.get(k).getDicKey());
                                break;
                            }
                        }

                        //处理服务费管理公司
                        if(StringUtils.isBlank(mortgageDeduction.getTarget())){
                            mortgageDeduction.setTarget("润坤");
                        }
//                        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && "铠岳".equals(mortgageDeduction.getTarget().trim())) {
//                            mortgageDeduction.setTarget(chinaPayConfig.getClassicKaiYueMemberId());
//                        } else {
//                            mortgageDeduction.setTarget(chinaPayConfig.getClassicRunKunMemberId());
//                        }

                        if (mortgageDeduction.getParam3() != null && !"".equals(mortgageDeduction.getParam3())) {
                            list.add(mortgageDeduction);
                        }
                    }
                }

                for (MortgageDeduction mortgageDeduction : list) {
                    List<MortgageDeduction> result = this.doSplitAccount(mortgageDeduction);
                    for (MortgageDeduction deduction : result) {
                        mortgageDeductionRepository.save(deduction);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void readXlsx(MultipartFile uploadedFile, int staffId) {

    }

    @Override
    public List<Map> saveMortgageDeductions(List<MortgageDeduction> list, String deductionMethod) throws Exception {

        ManualBatchAssembler assembler = (ManualBatchAssembler) assemblerFactory.getAssembleImpl("manual");
        if (StringUtils.equals("UNIONPAY", deductionMethod)) {
            assembler.saveUNIONPAY(list);
        } else if (StringUtils.equals("BAOFU", deductionMethod)) {
            assembler.saveBAOFU(list);
        }


        return null;
    }

    public List<MortgageDeduction> doSplitAccount(MortgageDeduction mortgageDeduction) {
        List<MortgageDeduction> result = new ArrayList<>();
        LimitManager limitManager = limitManagerRepository.findByBankCode(mortgageDeduction.getParam1());
        BigDecimal singleLimit = limitManager.getSingleLimit();
        BigDecimal bxAmount = mortgageDeduction.getSplitData1();
        BigDecimal fwfAmount = mortgageDeduction.getSplitData2();

        if (bxAmount.add(fwfAmount).doubleValue() <= singleLimit.doubleValue() || singleLimit.doubleValue() == -1 || singleLimit.doubleValue() == 0) {
            result.add(mortgageDeduction);
        } else {
            result = recurLimit(limitManager, mortgageDeduction);
        }

        return result;
    }

    private List recurLimit(LimitManager limitManager, MortgageDeduction mortgageDeduction) {
        List result = new ArrayList();
        BigDecimal totalAmount = mortgageDeduction.getSplitData1().add(mortgageDeduction.getSplitData2());
        BigDecimal drawDownAmount = totalAmount;
        BigDecimal drawDownBxAmount = mortgageDeduction.getSplitData1();
        BigDecimal drawDownFwfAmount = mortgageDeduction.getSplitData2();

        while (drawDownAmount.doubleValue() > 0) {
            BigDecimal bxAmount;
            if (drawDownAmount.doubleValue() > limitManager.getSingleLimit().doubleValue()) {
                bxAmount = limitManager.getSingleLimit().subtract(drawDownFwfAmount);
                if (bxAmount.doubleValue() >= limitManager.getSingleLimit().doubleValue()) {
                    bxAmount = limitManager.getSingleLimit();
                }
            } else {
                bxAmount = drawDownAmount;
            }
            MortgageDeduction newObj = new MortgageDeduction();
            BeanUtils.copyProperties(mortgageDeduction, newObj);
            newObj.setSplitData1(bxAmount);
            newObj.setSplitData2(drawDownFwfAmount);
            drawDownAmount = drawDownAmount.subtract(bxAmount).subtract(drawDownFwfAmount);
            drawDownBxAmount = drawDownBxAmount.subtract(bxAmount);
            drawDownFwfAmount = drawDownFwfAmount.subtract(drawDownFwfAmount);

            result.add(newObj);
        }

        return result;
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
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
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
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));

            }
        };

        long count = mortgageDeductionRepository.count(specification);
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

                //处理服务费管理公司
//                if (mortgageDeduction.getTarget() != null && chinaPayConfig.getExpressRealTimeKaiYueServiceMemberId().equals(mortgageDeduction.getTarget().trim())) {
//                    mortgageDeduction.setTarget("铠岳");
//                } else {
//                    mortgageDeduction.setTarget("润坤");
//                }

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
        List<MortgageDeduction> mortgageDeductionList = mortgageDeductionRepository.findByIdIn(idsList.toArray(new Integer[]{}));
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
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
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
            cell.setCellValue(mortgageDeduction.getParam1());
            cell = row.createCell(5);
            cell.setCellValue(mortgageDeduction.getParam3());
            cell = row.createCell(6);
            if (Utility.checkBigDecimal2(mortgageDeduction.getSplitData1()) == true) {
                cell.setCellValue(mortgageDeduction.getSplitData1().toString());
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(7);
            if (Utility.checkBigDecimal2(mortgageDeduction.getSplitData2()) == true) {
                cell.setCellValue(mortgageDeduction.getSplitData2().toString());
            } else {
                cell.setCellValue("");
            }

            cell = row.createCell(8);
            String company = mortgageDeduction.getTarget() != null ? mortgageDeduction.getTarget().trim() : "";
//            if (StringUtils.equals(company, chinaPayConfig.getClassicKaiYueMemberId())) {
//                cell.setCellValue("铠岳");
//            }
//
//            if (StringUtils.equals(company, chinaPayConfig.getClassicRunKunMemberId())) {
//                cell.setCellValue("润坤");
//            }
            cell.setCellValue(company);
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
            cell.setCellValue(mortgageDeduction.getOrderDesc());
            cell = row.createCell(15);
            if (Utility.checkBigDecimal2(mortgageDeduction.getRsplitData1()) == true) {
                cell.setCellValue(mortgageDeduction.getRsplitData1().toString());
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(16);
            if (Utility.checkBigDecimal2(mortgageDeduction.getRsplitData2()) == true) {
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
     * 查询支付结果
     *
     * @param id 记录id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message queryResult(String id) {
        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.findById(Integer.parseInt(id));
        Message message = null;
        if (StringUtils.equals(mortgageDeduction.getType(), "1")) {
            message = Message.fail("请先进行代扣操作,再查询代扣结果");
        } else {
            String channel = mortgageDeduction.getChannel();

            if (StringUtils.equals(channel, DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getCode())) {
                //银联代扣
                List<BasicNameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("MerOrderNo", mortgageDeduction.getOrdId()));
                list.add(new BasicNameValuePair("TranDate", Utility.convertToString(mortgageDeduction.getPayTime(), "yyyyMMdd")));
                Map send = null;
                try {
                    send = HttpClientUtil.send(chinaPayConfig.getExpressRealTimeQueryResultUrl(), list);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Message.fail("代扣状态查询失败");
                }
                //有返回结果
                if (send.containsKey("returnData")) {
                    String returnData = (String) send.get("returnData");
                    JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                    if (StringUtils.equals(RsbCodeEnum.ERROR_CODE_01.getCode(), jsonObject.getString("error_code"))) {
                        //查询成功
                        JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                        if (StringUtils.equals(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_001.getCode(), result.getString("OrderStatus"))) {
                            //支付成功
                            mortgageDeduction.setIssuccess("1");
                            mortgageDeduction.setResult(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_001.getCode());
                            mortgageDeduction.setErrorResult("支付成功");
                            updateMortgageDeduction(mortgageDeduction);
                        } else if (!StringUtils.equals(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_008.getCode(), result.getString("OrderStatus"))) {
                            //订单状态不为"0014,数据接收成功"为失败
                            mortgageDeduction.setIssuccess("0");
                            mortgageDeduction.setResult(jsonObject.getString("error_code"));
                            mortgageDeduction.setErrorResult(ChinaPayReturnCodeEnum.getValueByCode(jsonObject.getString("error_code")));
                            updateMortgageDeduction(mortgageDeduction);
                        }
                        message = Message.success();

                    } else {
                        //查询失败
                        message = Message.fail("代扣状态查询失败");
                    }
                } else {
                    //无返回结果
                    message = Message.fail("代扣状态查询失败");
                }
            } else {
                //宝付代扣
                List<BasicNameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("origTransId", mortgageDeduction.getOrdId()));
                list.add(new BasicNameValuePair("origTradeDate", Utility.convertToString(mortgageDeduction.getPayTime(), "yyyy-MM-dd HH:mm:ss")));
                Map send = null;
                try {
                    send = HttpClientUtil.send(baofuConfig.getProtocolQueryResultUrl(), list);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Message.fail("代扣状态查询失败");
                }
                if (send.containsKey("returnData")) {
                    //有返回结果
                    String returnData = (String) send.get("returnData");
                    JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                    if (StringUtils.equals(BFErrorCodeEnum.BF00000.getCode(), jsonObject.getString("error_code"))) {
                        //查询成功
                        JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                        if (StringUtils.equals(BFErrorCodeEnum.BF00000.getCode(), result.getString("biz_resp_code"))) {
                            //支付成功
                            mortgageDeduction.setIssuccess("1");
                            mortgageDeduction.setResult(BFErrorCodeEnum.BF00000.getCode());
                            mortgageDeduction.setErrorResult("支付成功");
                            updateMortgageDeduction(mortgageDeduction);
                        } else if (!StringUtils.equals(BFErrorCodeEnum.BF00113.getCode(), result.getString("biz_resp_code"))) {
                            //订单状态不为"BF00113,交易处理中，请稍后查询"为失败,状态为BF00113不处理
                            mortgageDeduction.setIssuccess("0");
                            mortgageDeduction.setResult(jsonObject.getString("error_code"));
                            mortgageDeduction.setErrorResult(BFErrorCodeEnum.getValueByCode(jsonObject.getString("error_code")));
                            updateMortgageDeduction(mortgageDeduction);
                        }
                        message = Message.success();

                    } else {
                        //查询失败
                        message = Message.fail("代扣状态查询失败");
                    }
                } else {
                    //无返回结果
                    message = Message.fail("代扣状态查询失败");
                }

            }
        }

        return message;
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
            sort = new Sort(Sort.Direction.DESC, "id");
        }
        Integer pageNumber = pageBean.getPageNumber();
        Integer pageSize = pageBean.getPageSize();
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }


}
