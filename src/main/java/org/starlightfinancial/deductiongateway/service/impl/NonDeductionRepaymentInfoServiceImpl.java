package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.local.AutoAccountingExcelRow;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfoQueryCondition;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfoRepository;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.service.AutoAccountUploadService;
import org.starlightfinancial.deductiongateway.service.CacheService;
import org.starlightfinancial.deductiongateway.service.NonDeductionRepaymentInfoCalculationService;
import org.starlightfinancial.deductiongateway.service.NonDeductionRepaymentInfoService;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.PoiUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息Service实现类
 * @date: Created in 2018/7/17 17:20
 * @Modified By:
 */
@Service
public class NonDeductionRepaymentInfoServiceImpl implements NonDeductionRepaymentInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonDeductionRepaymentInfoServiceImpl.class);

    @Autowired
    private NonDeductionRepaymentInfoRepository nonDeductionRepaymentInfoRepository;

    @Autowired
    private BeanConverter beanConverter;

    /**
     * 新增非代扣还款数据时的列名与字段映射
     */
    private static final HashMap<String, String> UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP = new HashMap<String, String>(6) {
        {
            put("还款时间", "repaymentTermDate");
            put("还款原始信息", "repaymentOriginalInfo");
            put("单位/个人还款", "customerName");
            put("还款方式", "repaymentMethod");
            put("还款类别", "repaymentType");
            put("还款金额", "repaymentAmount");
        }
    };

    /**
     * 拆分非代扣还款数据时的列名与字段映射
     */
    private static final HashMap<String, String> SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP = new HashMap<String, String>(UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP) {
        {
            put("入账时间", "accountingDate");
            put("合同编号", "contractNo");
        }
    };

    /**
     * 导出非代扣还款数据表头
     */
    private static final String[] EXPORT_COLUMN_NAME = new String[]{"入账公司", "合同编号", "还款时间", "还款原始信息", "单位/个人还款", "还款方式", "还款类别", "还款金额", "入账时间", "导入时间"};


    /**
     * 非代扣还款信息文件导入
     *
     * @param uploadFile 文件
     * @param session    会话session
     * @throws IOException IO异常时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importFile(MultipartFile uploadFile, HttpSession session) throws Exception {
        //读取Excel文件转换为NonDeductionRepaymentInfo对象
        Map<String, List<NonDeductionRepaymentInfo>> resultMap = PoiUtil.readExcel(uploadFile, UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP, NonDeductionRepaymentInfo.class);
        //设置NonDeductionRepaymentInfo对象的入账公司,要求上传的excel表格的sheet名是入账公司的中文名称
        resultMap.forEach((chargeCompany, list) -> list.forEach(nonDeductionRepaymentInfo -> {
            //设置入账公司
            nonDeductionRepaymentInfo.setChargeCompany(chargeCompany);
            //设置信息是否完整,判断标准:是否有有对应contractNo
            nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.FAIL.getCode());
            //设置是否上传自动入账文件
            nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.FAIL.getCode());
            //设置入账时间:默认与还款日期一致
            nonDeductionRepaymentInfo.setAccountingDate(nonDeductionRepaymentInfo.getRepaymentTermDate());
            //设置创建时间
            nonDeductionRepaymentInfo.setGmtCreate(new Date());
            //设置修改时间
            nonDeductionRepaymentInfo.setGmtModified(nonDeductionRepaymentInfo.getGmtCreate());
            //设置创建人id
            nonDeductionRepaymentInfo.setCreateId(Utility.getLoginUserId(session));
            //设置修改人id
            nonDeductionRepaymentInfo.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
            //处理还款方式中银行转账的入账银行:银行转账-银行名称
            String repaymentMethod = nonDeductionRepaymentInfo.getRepaymentMethod();
            //用"-"切割字符串,如果是银行转账,将银行转账,银行名称分隔开
            String[] splitArray = repaymentMethod.split("-");
            nonDeductionRepaymentInfo.setRepaymentMethod(splitArray[0]);
            //判断切割后的字符串数组长度是否大于1
            if (splitArray.length > 1) {
                nonDeductionRepaymentInfo.setBankName(splitArray[1]);
            }

            //尝试查找非代扣还款信息对应的业务信息
            NonDeductionRepaymentInfoCalculationService.searchBusinessTransaction(nonDeductionRepaymentInfo);

        }));

        //保存记录
        resultMap.forEach((chargeCompany, list) -> nonDeductionRepaymentInfoRepository.save(list));
    }


    /**
     * 根据条件查询记录
     *
     * @param pageBean                                页面参数对象
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @return 返回根据条件查询到的记录
     */
    @Override
    public PageBean queryNonDeductionRepaymentInfo(PageBean pageBean, NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition) {
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 0);
        Page<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = nonDeductionRepaymentInfoRepository.findAll(getSpecification(nonDeductionRepaymentInfoQueryCondition), pageRequest);
        if (nonDeductionRepaymentInfos.hasContent()) {
            pageBean.setTotal(nonDeductionRepaymentInfos.getTotalElements());
            pageBean.setRows(nonDeductionRepaymentInfos.getContent());
            return pageBean;
        }
        return null;
    }

    /**
     * 组装查询条件
     *
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @return
     */
    private Specification<NonDeductionRepaymentInfo> getSpecification(NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition) {
        return new Specification<NonDeductionRepaymentInfo>() {
            @Override
            public Predicate toPredicate(Root<NonDeductionRepaymentInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                //是否数据完整
                String[] isIntegratedArray = nonDeductionRepaymentInfoQueryCondition.getIsIntegrated().split(",");
                predicates.add(cb.in(root.get("isIntegrated")).value(Arrays.asList(isIntegratedArray)));

                //是否已经上传自动入账文件
                String[] isUploadArray = nonDeductionRepaymentInfoQueryCondition.getIsUploaded().split(",");
                predicates.add(cb.in(root.get("isUploaded")).value(Arrays.asList(isUploadArray)));

                //还款日期筛选
                predicates.add(cb.between(root.get("repaymentTermDate"), nonDeductionRepaymentInfoQueryCondition.getRepaymentStartDate(), Utility.toMidNight(nonDeductionRepaymentInfoQueryCondition.getRepaymentEndDate())));

                //生成日期筛选
                predicates.add(cb.between(root.get("gmtCreate"), nonDeductionRepaymentInfoQueryCondition.getImportStartDate(), Utility.toMidNight(nonDeductionRepaymentInfoQueryCondition.getImportEndDate())));

                //客户名非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(nonDeductionRepaymentInfoQueryCondition.getCustomerName())) {
                    predicates.add(cb.equal(root.get("customerName"), nonDeductionRepaymentInfoQueryCondition.getCustomerName().trim()));
                }
                //合同号非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(nonDeductionRepaymentInfoQueryCondition.getContractNo())) {
                    predicates.add(cb.equal(root.get("contractNo"), nonDeductionRepaymentInfoQueryCondition.getContractNo().trim()));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }

    /**
     * 保存记录
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        setBusinessTransactionInfo(nonDeductionRepaymentInfo);
        nonDeductionRepaymentInfoRepository.save(nonDeductionRepaymentInfo);
        LOGGER.info("新增非代扣还款信息,操作人:[{}],非代扣还款记录id:[{}]", nonDeductionRepaymentInfo.getCreateId(), nonDeductionRepaymentInfo.getId());
    }

    /**
     * 更新记录
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        setBusinessTransactionInfo(nonDeductionRepaymentInfo);
        NonDeductionRepaymentInfo nonDeductionRepaymentInfoInDataBase = nonDeductionRepaymentInfoRepository.findOne(nonDeductionRepaymentInfo.getId());
        //如果修改了入账公司,并且入账银行没有修改,需要将入账银行设置为null,避免因未设置相应的入账银行错误地生成还款方式
        if (!StringUtils.equals(nonDeductionRepaymentInfo.getChargeCompany(), nonDeductionRepaymentInfoInDataBase.getChargeCompany())) {
            if (StringUtils.equals(nonDeductionRepaymentInfo.getBankName(), nonDeductionRepaymentInfoInDataBase.getBankName())) {
                nonDeductionRepaymentInfo.setBankName(null);
            }
        }
        nonDeductionRepaymentInfoRepository.saveAndFlush(nonDeductionRepaymentInfo);
        LOGGER.info("更新非代扣还款信息,操作人:[{}],非代扣还款记录id:[{}]", nonDeductionRepaymentInfo.getModifiedId(), nonDeductionRepaymentInfo.getId());
    }

    /**
     * 上传自动入账文件
     *
     * @param ids     一条记录或多条记录id
     * @param session 会话session
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadAutoAccountingFile(String ids, HttpSession session) throws IOException {
        //处理ids,先用逗号切割,然后转为List
        List<Long> idsList = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        //根据id查询对应的记录
        List<NonDeductionRepaymentInfo> all = nonDeductionRepaymentInfoRepository.findAll(idsList);
        //筛选信息完整和未上传自动入账文件的记录,如果其属性isIntegrated为1和isUploaded为0保留,其余舍弃
        List<NonDeductionRepaymentInfo> afterFilter = all.stream().filter(nonDeductionRepaymentInfo -> "1".equals(nonDeductionRepaymentInfo.getIsIntegrated()) && "0".equals(nonDeductionRepaymentInfo.getIsUploaded())).collect(Collectors.toList());
        //将非代扣还款数据转换为自动入账excel表格行元素对应实体类
        List<AutoAccountingExcelRow> autoAccountingExcelRows = afterFilter.stream().map(nonDeductionRepaymentInfo -> beanConverter.transToAutoAccountingExcelRow(nonDeductionRepaymentInfo)).collect(Collectors.toList());

        //制作excel表格
        HSSFWorkbook workBook = AutoAccountUploadService.createWorkBook(autoAccountingExcelRows);
        //上传excel表格
        AutoAccountUploadService.upload(workBook);
        //更新是否上传属性为已上传:1
        afterFilter.forEach(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.SUCCESS.getCode()));
        nonDeductionRepaymentInfoRepository.save(afterFilter);
        LOGGER.info("上传非代扣还款信息成功,操作人:[{}],上传的记录id:[{}]", Utility.getLoginUserName(session), ids);
    }

    /**
     * 拆分非代扣还款信息
     *
     * @param nonDeductionRepaymentInfos          由页面传入的非代扣还款信息
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void splitNonDeduction(List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos, Long originalNonDeductionRepaymentInfoId) {
        //查询原始非代扣还款信息
        NonDeductionRepaymentInfo originalNonDeduction = nonDeductionRepaymentInfoRepository.findOne(originalNonDeductionRepaymentInfoId);

        nonDeductionRepaymentInfos.forEach(nonDeductionRepaymentInfo -> {
            //设置非代扣还款数据的业务信息
            setBusinessTransactionInfo(nonDeductionRepaymentInfo);
            nonDeductionRepaymentInfoRepository.saveAndFlush(nonDeductionRepaymentInfo);
            //将原始非代扣还款信息中的还款金额相应减少,并更新记录
            BigDecimal originalRepaymentAmount = new BigDecimal(originalNonDeduction.getRepaymentAmount());
            BigDecimal repaymentAmount = new BigDecimal(nonDeductionRepaymentInfo.getRepaymentAmount());
            BigDecimal surplusRepaymentAmount = originalRepaymentAmount.subtract(repaymentAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            originalNonDeduction.setRepaymentAmount(surplusRepaymentAmount.toString());
            //设置更新时间和更新操作人
            originalNonDeduction.setGmtModified(new Date());
            originalNonDeduction.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
            LOGGER.info("拆分非代扣还款信息成功,操作人:[{}],被拆分的记录id:[{}],拆分出来的记录id:[{}]", nonDeductionRepaymentInfo.getCreateId(), originalNonDeductionRepaymentInfoId, nonDeductionRepaymentInfo.getId());
        });
        nonDeductionRepaymentInfoRepository.saveAndFlush(originalNonDeduction);

    }

    /**
     * 刷新CacheService,重新尝试查找匹配的业务信息
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param session   会话session
     * @param session   会话session
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retrySearchBusinessTransactionInfo(Date startDate, Date endDate, HttpSession session) {
        //首先刷新缓存服务
        CacheService.refresh();
        //查找信息不完整的非代扣还款信息
        List<NonDeductionRepaymentInfo> notIntegratedList = nonDeductionRepaymentInfoRepository.findByRepaymentTermDateBetweenAndIsIntegrated(startDate, endDate, String.valueOf(0));
        //查找匹配的业务信息
        notIntegratedList.forEach(nonDeductionRepaymentInfo -> {
            setBusinessTransactionInfo(nonDeductionRepaymentInfo);
            if (StringUtils.equals(nonDeductionRepaymentInfo.getIsIntegrated(), ConstantsEnum.SUCCESS.getCode())) {
                nonDeductionRepaymentInfo.setModifiedId(Utility.getLoginUserId(session));
                nonDeductionRepaymentInfo.setGmtModified(new Date());
            }
        });

        //筛选出找到匹配的业务信息的非代扣还款信息,并更新
        notIntegratedList.stream().filter(nonDeductionRepaymentInfo -> StringUtils.equals(nonDeductionRepaymentInfo.getIsIntegrated(), "1")).forEach(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfoRepository.saveAndFlush(nonDeductionRepaymentInfo));
    }

    /**
     * 设置非代扣还款数据的业务信息
     *
     * @param nonDeductionRepaymentInfo 非代扣还款数据
     */
    private void setBusinessTransactionInfo(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        String contractNo = nonDeductionRepaymentInfo.getContractNo();
        if (StringUtils.isEmpty(contractNo)) {
            NonDeductionRepaymentInfoCalculationService.searchBusinessTransaction(nonDeductionRepaymentInfo);
        } else {
            Map<String, BusinessTransaction> businessTransactionCacheMap = CacheService.getBusinessTransactionCacheMap();
            BusinessTransaction businessTransaction = businessTransactionCacheMap.get(contractNo);
            if (businessTransaction != null) {
                nonDeductionRepaymentInfo.setDateId(businessTransaction.getDateId());
            }
        }
        //判断数据是否完整:增加判断dateId不为空是因为拆分非代扣还款数据时会上传合同编号,这个合同编号不一定准确的,不一定能查询到对应的合同信息,
        //为了避免非代扣还款数据未查询到对应的业务信息就上传到业务系统进行入账,所以增加了额外的判断
        if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getContractNo()) && nonDeductionRepaymentInfo.getDateId() != null) {
            nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.SUCCESS.getCode());
        } else {
            nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.FAIL.getCode());
        }

    }

    /**
     * 批量拆分非代扣还款信息
     *
     * @param uploadFile                          包含拆分信息的excel文件
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     * @param session                             会话session
     * @throws Exception 异常时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSplitNonDeduction(MultipartFile uploadFile, Long originalNonDeductionRepaymentInfoId, HttpSession session) throws Exception {
        //首先将传入的包含拆分信息的excel文件转换为NonDeductionRepaymentInfo对象
        Map<String, List<NonDeductionRepaymentInfo>> resultMap = PoiUtil.readExcel(uploadFile, SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP, NonDeductionRepaymentInfo.class);

        //拆分信息
        List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = new ArrayList<>();
        //设置NonDeductionRepaymentInfo对象的入账公司,要求上传的excel表格的sheet名是入账公司的中文名称
        resultMap.forEach((chargeCompany, list) -> list.forEach(nonDeductionRepaymentInfo -> {
            //设置入账公司
            nonDeductionRepaymentInfo.setChargeCompany(chargeCompany);
            //设置信息是否完整,判断标准:是否有有对应的getContractNo
            if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getContractNo())) {
                //通过合同号查询缓存中的查询业务信息
                BusinessTransaction businessTransaction = CacheService.getBusinessTransactionCacheMap().get(nonDeductionRepaymentInfo.getContractNo());
                if (businessTransaction != null) {
                    //如果业务信息不为空,信息完整
                    nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.SUCCESS.getCode());
                    nonDeductionRepaymentInfo.setDateId(businessTransaction.getDateId());
                } else {
                    //信息不完整
                    nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.FAIL.getCode());
                }
            } else {
                //信息不完整
                nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.FAIL.getCode());
            }
            //设置是否上传自动入账文件
            nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.FAIL.getCode());
            //设置创建时间
            nonDeductionRepaymentInfo.setGmtCreate(new Date());
            //设置修改时间
            nonDeductionRepaymentInfo.setGmtModified(nonDeductionRepaymentInfo.getGmtCreate());
            //设置创建人id
            nonDeductionRepaymentInfo.setCreateId(Utility.getLoginUserId(session));
            //设置修改人id
            nonDeductionRepaymentInfo.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
            //处理还款方式中银行转账的入账银行:银行转账-银行名称
            String repaymentMethod = nonDeductionRepaymentInfo.getRepaymentMethod();
            //用"-"切割字符串,如果是银行转账,将银行转账,银行名称分隔开
            String[] splitArray = repaymentMethod.split("-");
            nonDeductionRepaymentInfo.setRepaymentMethod(splitArray[0]);
            //判断切割后的字符串数组长度是否大于1
            if (splitArray.length > 1) {
                nonDeductionRepaymentInfo.setBankName(splitArray[1]);
            }
            nonDeductionRepaymentInfos.add(nonDeductionRepaymentInfo);
        }));
        //进行拆分的方法
        splitNonDeduction(nonDeductionRepaymentInfos, originalNonDeductionRepaymentInfoId);


    }

    /**
     * 根据条件导出非代扣还款信息
     *
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @return excel表格
     */
    @Override
    public Workbook exportXLS(NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition) {
        //查询所有非代扣还款信息
        List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = nonDeductionRepaymentInfoRepository.findAll(getSpecification(nonDeductionRepaymentInfoQueryCondition));
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //设置列宽
        sheet.setDefaultColumnWidth(16);
        //创建表头
        HSSFRow rowHeader = sheet.createRow(0);
        //创建单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        //设置粗体
        font.setBold(true);
        //设置居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);
        //为表头赋值
        for (int i = 0; i < EXPORT_COLUMN_NAME.length; i++) {
            HSSFCell cell = rowHeader.createCell(i);
            cell.setCellValue(EXPORT_COLUMN_NAME[i]);
            cell.setCellStyle(cellStyle);
        }
        //填充表格数据
        HSSFRow row = null;
        HSSFCell cell = null;
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (NonDeductionRepaymentInfo nonDeductionRepaymentInfo : nonDeductionRepaymentInfos) {
            row = sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(nonDeductionRepaymentInfo.getChargeCompany());
            cell = row.createCell(1);
            cell.setCellValue(nonDeductionRepaymentInfo.getContractNo());
            cell = row.createCell(2);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(nonDeductionRepaymentInfo.getRepaymentTermDate());
            cell = row.createCell(3);
            cell.setCellValue(nonDeductionRepaymentInfo.getRepaymentOriginalInfo());
            cell = row.createCell(4);
            cell.setCellValue(nonDeductionRepaymentInfo.getCustomerName());
            cell = row.createCell(5);
            //处理还款方式,如果是有入账银行也就是银行转账,需要添加到还款方式中:银行转账-招行0366
            stringBuilder.append(nonDeductionRepaymentInfo.getRepaymentMethod());
            if(StringUtils.isNotBlank(nonDeductionRepaymentInfo.getBankName())){
                stringBuilder.append("-").append(nonDeductionRepaymentInfo.getBankName());
            }
            cell.setCellValue(stringBuilder.toString());
            //清空
            stringBuilder.delete(0, stringBuilder.length());
            cell = row.createCell(6);
            cell.setCellValue(nonDeductionRepaymentInfo.getRepaymentType());
            cell = row.createCell(7);
            cell.setCellValue(nonDeductionRepaymentInfo.getRepaymentAmount());
            cell = row.createCell(8);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(nonDeductionRepaymentInfo.getAccountingDate());
            cell = row.createCell(9);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(nonDeductionRepaymentInfo.getGmtCreate());
            i++;
        }
        return workbook;
    }


}
