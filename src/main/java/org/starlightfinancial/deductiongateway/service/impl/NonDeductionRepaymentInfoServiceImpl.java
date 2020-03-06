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
import org.starlightfinancial.deductiongateway.config.AllInPayConfig;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.domain.remote.ExemptInfo;
import org.starlightfinancial.deductiongateway.dto.SurplusTotalAmount;
import org.starlightfinancial.deductiongateway.enums.*;
import org.starlightfinancial.deductiongateway.exception.nondeduction.AlreadyUploadedUpdateException;
import org.starlightfinancial.deductiongateway.exception.nondeduction.FieldFormatCheckException;
import org.starlightfinancial.deductiongateway.service.*;
import org.starlightfinancial.deductiongateway.utility.*;
import org.starlightfinancial.deductiongateway.vo.NonDeductionRepaymentInfoQueryCondition;

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

    @Autowired
    private AllInPayConfig allInPayConfig;

    @Autowired
    AccountManagerRepository accountManagerRepository;
    @Autowired
    private MultiOverdueService multiOverdueService;
    @Autowired
    private ExemptInfoService exemptInfoService;
    @Autowired
    private FailEntryAccountService failEntryAccountService;
    /**
     * 新增非代扣还款数据时的Excel列名与字段映射
     */
    private static HashMap<String, String> UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP = new HashMap<>();

    /**
     * 拆分非代扣还款数据时的Excel列名与字段映射
     */
    private static HashMap<String, String> SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP = new HashMap<>();
    /**
     * 导出非代扣还款数据表头
     */
    private static String[] EXPORT_COLUMN_NAME = new String[]{"入账公司", "合同编号", "还款时间", "还款原始信息", "单位/个人还款",
            "还款方式", "还款类别", "还款金额", "入账时间", "导入时间", "入账备注"};

    static {
        //新增非代扣还款数据时的Excel列名与字段映射
        UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款时间", "repaymentTermDate");
        UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款原始信息", "repaymentOriginalInfo");
        UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP.put("单位/个人还款", "customerName");
        UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款方式", "repaymentMethod");
        UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款类别", "repaymentType");
        UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款金额", "repaymentAmount");

        //拆分非代扣还款数据时的Excel列名与字段映射
        SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款时间", "repaymentTermDate");
        SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款原始信息", "repaymentOriginalInfo");
        SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP.put("单位/个人还款", "customerName");
        SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP.put("还款金额", "repaymentAmount");
        SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP.put("入账时间", "accountingDate");
        SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP.put("合同编号", "contractNo");
        SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP.put("入账备注", "remark");
    }


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
        Map<String, List<NonDeductionRepaymentInfo>> resultMap = PoiUtil.readExcel(uploadFile,
                UPLOAD_COLUMN_NAME_AND_FIELD_NAME_MAP, NonDeductionRepaymentInfo.class);
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
            //判断系统自动匹配到业务信息是否成功
            if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getContractNo())) {
                //是否系统自动匹配到业务信息设置为是
                nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.SUCCESS.getCode());
            } else {
                //设置是否系统自动匹配到业务信息为否
                nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.FAIL.getCode());
            }


        }));

        //保存记录
        resultMap.forEach((chargeCompany, list) -> nonDeductionRepaymentInfoRepository.save(list));
    }


    /**
     * 根据条件查询记录
     *
     * @param pageBean                                页面参数对象
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @param session                                 会话session
     * @return 返回根据条件查询到的记录
     */
    @Override
    public PageBean queryNonDeductionRepaymentInfo(PageBean pageBean,
                                                   NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition, HttpSession session) {
        Specification<NonDeductionRepaymentInfo> specification =
                getSpecification(nonDeductionRepaymentInfoQueryCondition);
        //不分页查询所有数据,获得根据条件查询出来还款总额
        List<NonDeductionRepaymentInfo> allNoPaging = nonDeductionRepaymentInfoRepository.findAll(specification);
        String totalRepaymentAmount = allNoPaging.stream().map(NonDeductionRepaymentInfo::getRepaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
        session.setAttribute("totalRepaymentAmount", totalRepaymentAmount);
        //分页查询所有数据
        long count = nonDeductionRepaymentInfoRepository.count(specification);
        if (count == 0) {
            //如果查询出来的总记录数为0,直接返回null,避免后续查询代码执行
            return null;
        }
        double tempTotalPageCount = count / (pageBean.getPageSize().doubleValue());
        double totalPageCount = Math.ceil(tempTotalPageCount == 0 ? 1 : tempTotalPageCount);
        if (totalPageCount < pageBean.getPageNumber()) {
            pageBean.setPageNumber(1);
        }
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 0, "gmtModified");
        Page<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos =
                nonDeductionRepaymentInfoRepository.findAll(specification, pageRequest);
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
            public Predicate toPredicate(Root<NonDeductionRepaymentInfo> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                //是否数据完整
                String[] isIntegratedArray = nonDeductionRepaymentInfoQueryCondition.getIsIntegrated().split(",");
                predicates.add(cb.in(root.get("isIntegrated")).value(Arrays.asList(isIntegratedArray)));

                //是否已经上传自动入账文件
                String[] isUploadArray = nonDeductionRepaymentInfoQueryCondition.getIsUploaded().split(",");
                predicates.add(cb.in(root.get("isUploaded")).value(Arrays.asList(isUploadArray)));

                //还款日期筛选
                predicates.add(cb.between(root.get("repaymentTermDate"),
                        nonDeductionRepaymentInfoQueryCondition.getRepaymentStartDate(),
                        Utility.toMidNight(nonDeductionRepaymentInfoQueryCondition.getRepaymentEndDate())));

                //生成日期筛选
                predicates.add(cb.between(root.get("gmtCreate"),
                        nonDeductionRepaymentInfoQueryCondition.getImportStartDate(),
                        Utility.toMidNight(nonDeductionRepaymentInfoQueryCondition.getImportEndDate())));

                //客户名非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(nonDeductionRepaymentInfoQueryCondition.getCustomerName())) {
                    predicates.add(cb.equal(root.get("customerName"),
                            nonDeductionRepaymentInfoQueryCondition.getCustomerName().trim()));
                }
                //合同号非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(nonDeductionRepaymentInfoQueryCondition.getContractNo())) {
                    predicates.add(cb.equal(root.get("contractNo"),
                            nonDeductionRepaymentInfoQueryCondition.getContractNo().trim()));
                }

                //入账公司判断是否是全部,如果是0是全部入账公司不加限制,如果不是0,根据传入的条件查询
                if (!ConstantsEnum.FAIL.getCode().equals(nonDeductionRepaymentInfoQueryCondition.getChargeCompany())) {
                    predicates.add(cb.equal(root.get("chargeCompany"),
                            nonDeductionRepaymentInfoQueryCondition.getChargeCompany()));
                }

                //还款方式判断是否是全部,如果是0是全部还款方式不加限制,如果不是0,根据传入的条件查询
                if (!ConstantsEnum.FAIL.getCode().equals(nonDeductionRepaymentInfoQueryCondition.getRepaymentMethod())) {
                    predicates.add(cb.equal(root.get("repaymentMethod"),
                            nonDeductionRepaymentInfoQueryCondition.getRepaymentMethod()));
                }

                //入账银行判断是否是全部,如果是0是全部入账银行不加限制,如果不是0,根据传入的条件查询
                if (!ConstantsEnum.FAIL.getCode().equals(nonDeductionRepaymentInfoQueryCondition.getBankName())) {
                    predicates.add(cb.equal(root.get("bankName"),
                            nonDeductionRepaymentInfoQueryCondition.getBankName()));
                }

                //入账银行判断是否是全部,如果是0是全部入账银行不加限制,如果不是0,根据传入的条件查询
                if (!ConstantsEnum.FAIL.getCode().equals(nonDeductionRepaymentInfoQueryCondition.getRepaymentType())) {
                    predicates.add(cb.equal(root.get("repaymentType"),
                            nonDeductionRepaymentInfoQueryCondition.getRepaymentType()));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }

    /**
     * 保存记录
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     * @param session                   会话session
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, HttpSession session) {
        //补充业务信息
        trim(nonDeductionRepaymentInfo);
        supplementBusinessTransactionInfo(nonDeductionRepaymentInfo);
        nonDeductionRepaymentInfoRepository.save(nonDeductionRepaymentInfo);
        LOGGER.info("新增非代扣还款信息,操作人:[{}],非代扣还款记录id:[{}]", Utility.getLoginUserName(session),
                nonDeductionRepaymentInfo.getId());
    }

    /**
     * 更新记录
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     * @param session                   会话session
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, HttpSession session) {
        NonDeductionRepaymentInfo nonDeductionRepaymentInfoInDataBase = nonDeductionRepaymentInfoRepository.findOne(nonDeductionRepaymentInfo.getId());
        if (StringUtils.equals(nonDeductionRepaymentInfoInDataBase.getIsUploaded(), ConstantsEnum.SUCCESS.getCode())) {
            throw new AlreadyUploadedUpdateException();
        }

        trim(nonDeductionRepaymentInfo);
        if (!StringUtils.equals(ConstantsEnum.SUCCESS.getCode(), nonDeductionRepaymentInfo.getIsUploaded())) {

            if (StringUtils.isBlank(nonDeductionRepaymentInfo.getContractNo())) {
                //非系统匹配成功的情况
                //判断是否有合同号,如果没有合同号,将是否系统自动匹配业务信息设置为否,如果通过系统自动匹配成功的,后面会设置为是
                nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.FAIL.getCode());
            } else {
                //判断数据库中的记录合同编号是否和更新记录的一致,如果不一致,设置手动匹配
                if (!StringUtils.equals(nonDeductionRepaymentInfo.getContractNo(), nonDeductionRepaymentInfoInDataBase.getContractNo())) {
                    nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.FAIL.getCode());
                } else {
                    //如果合同号无修改但修改了还款人,需要将非代扣还款信息的合同编号设置为空,后续代码会自动设置匹配状态以及重新匹配合同号,
                    //避免起初还款人录入错误,系统已自动匹配合同号,修改还款人姓名后不再匹配合同号,入账时入到错误的业务中的情况
                    if (!StringUtils.equals(nonDeductionRepaymentInfoInDataBase.getCustomerName(), nonDeductionRepaymentInfo.getCustomerName())) {
                        nonDeductionRepaymentInfo.setContractNo(null);
                    }
                }
            }
        }

        supplementBusinessTransactionInfo(nonDeductionRepaymentInfo);

        //如果修改了入账公司,并且入账银行没有修改,需要将入账银行设置为null,避免因未设置相应的入账银行错误地生成还款方式
        if (!StringUtils.equals(nonDeductionRepaymentInfo.getChargeCompany(),
                nonDeductionRepaymentInfoInDataBase.getChargeCompany())) {
            if (StringUtils.equals(nonDeductionRepaymentInfo.getBankName(),
                    nonDeductionRepaymentInfoInDataBase.getBankName())) {
                nonDeductionRepaymentInfo.setBankName(null);
            }
        }
        String differenceFieldValue = Utility.compareObjectFieldValue(nonDeductionRepaymentInfoInDataBase, nonDeductionRepaymentInfo);
        nonDeductionRepaymentInfoRepository.saveAndFlush(nonDeductionRepaymentInfo);
        LOGGER.info("更新非代扣还款信息,操作人:[{}],非代扣还款记录id:[{}],更新情况:{}", Utility.getLoginUserName(session),
                nonDeductionRepaymentInfo.getId(), differenceFieldValue);
    }

    /**
     * 上传自动入账文件
     *
     * @param nonDeductionRepaymentInfos 需要处理的非代扣还款信息
     * @param session                    会话session
     * @throws IOException               io异常时抛出
     * @throws FieldFormatCheckException 非代扣还款数据属性格式不符合预期时抛出
     * @throws ClassNotFoundException    深复制异常时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAutoAccountingFile(List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos, HttpSession session) throws IOException,
            FieldFormatCheckException, ClassNotFoundException {

        //筛选信息完整和未上传自动入账文件的记录,如果其属性isIntegrated为1和isUploaded为0保留,其余舍弃
        List<NonDeductionRepaymentInfo> afterFilter =
                nonDeductionRepaymentInfos.stream().filter(nonDeductionRepaymentInfo -> "1".equals(nonDeductionRepaymentInfo.getIsIntegrated()) && "0".equals(nonDeductionRepaymentInfo.getIsUploaded())).collect(Collectors.toList());

        //入账公司,还款方式,还款类别,入账银行(银行转账时)格式校验
        checkFieldFormat(afterFilter);
        //深复制一份以更新数据库中记录
        List<NonDeductionRepaymentInfo> original = Utility.deepCopy(afterFilter);
        //合并同一合同号的不同渠道还款,不同的还款类别分开处理
        HashMap<String, NonDeductionRepaymentInfo> principalAndInterestContractNoNonDeductionRepaymentInfoMap =
                new HashMap<>(10);
        HashMap<String, NonDeductionRepaymentInfo> serviceFeeContractNoNonDeductionRepaymentInfoMap = new HashMap<>(10);
        HashMap<String, NonDeductionRepaymentInfo> evaluationFeeContractNoNonDeductionRepaymentInfoMap =
                new HashMap<>(10);
        afterFilter.forEach(
                nonDeductionRepaymentInfo -> {
                    if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentType(),
                            RepaymentTypeEnum.PRINCIPAL_AND_INTEREST.getDesc())) {
                        //还款类别为本息
                        mergeNonDeductionRepaymentInfo(principalAndInterestContractNoNonDeductionRepaymentInfoMap,
                                nonDeductionRepaymentInfo);
                    } else if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentType(),
                            RepaymentTypeEnum.SERVICE_FEE.getDesc())) {
                        //还款类别为服务费
                        mergeNonDeductionRepaymentInfo(serviceFeeContractNoNonDeductionRepaymentInfoMap,
                                nonDeductionRepaymentInfo);
                    } else {
                        //还款类别为调查评估费
                        mergeNonDeductionRepaymentInfo(evaluationFeeContractNoNonDeductionRepaymentInfoMap,
                                nonDeductionRepaymentInfo);
                    }
                }
        );

        //将合并后的map转换为list
        ArrayList<NonDeductionRepaymentInfo> noDuplicateList =
                new ArrayList<>(principalAndInterestContractNoNonDeductionRepaymentInfoMap.values());
        noDuplicateList.addAll(serviceFeeContractNoNonDeductionRepaymentInfoMap.values());
        noDuplicateList.addAll(evaluationFeeContractNoNonDeductionRepaymentInfoMap.values());

        noDuplicateList.sort((o1, o2) -> {
            if (o1.getAccountingDate().before(o2.getAccountingDate())) {
                return -1;
            } else if (o1.getAccountingDate().after(o2.getAccountingDate())) {
                return 1;
            }
            return 0;
        });
        //############还款日为2020.1-2020.3部分客户因疫情豁免了罚息,入账时需要判断是否能直接结清
        ArrayList<ExemptInfo> exemptInfos = new ArrayList<ExemptInfo>();
        Iterator<NonDeductionRepaymentInfo> iterator = noDuplicateList.iterator();
        while (iterator.hasNext()) {
            NonDeductionRepaymentInfo nonDeductionRepaymentInfo = iterator.next();
            AccountManager accountManager = accountManagerRepository.findBySortAndContractNo(1, nonDeductionRepaymentInfo.getContractNo());
            //判断是否是标记客户
            if (Objects.nonNull(accountManager) && StringUtils.equals(accountManager.getExemptFlag(), Constant.ENABLED_TRUE.toString())) {
                //是本息还是服务费
                int repaymentType = 0;
                if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentType(), "本息")) {
                    repaymentType = 1212;
                } else if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentType(), "服务费")) {
                    repaymentType = 1214;
                } else {
                    //如果是其他费用种类,跳过
                    continue;
                }
                SurplusTotalAmount surplusTotalAmount = multiOverdueService.obtainExemptInfo(accountManager
                        , nonDeductionRepaymentInfo.getRepaymentAmount(), nonDeductionRepaymentInfo.getAccountingDate(), repaymentType);
                if (surplusTotalAmount.getOverdueFlag()) {
                    //如果有逾期了,需要判断金额是否能结清,如果不能要打印日志,不直接入账
                    if (surplusTotalAmount.getClearFlag()) {
                        //如果能够结清
                        exemptInfos.addAll(surplusTotalAmount.getExemptInfos());
                    } else {
                        //如果不能够结清,给出提示
                        return amountIsNotSufficient(original, iterator, nonDeductionRepaymentInfo);
                    }
                }
            }
        }
        if (noDuplicateList.size() == 0) {
            //如果没有需要上传入账文件的记录,不再执行后续的操作
            return "1";
        }
        // 豁免信息入库
        if (exemptInfos.size() > 0) {
            exemptInfoService.save(exemptInfos);
        }
        //############新增结束

        //将非代扣还款数据转换为自动入账excel表格行元素对应实体类
        List<AutoAccountingExcelRow> autoAccountingExcelRows =
                noDuplicateList.stream().map(nonDeductionRepaymentInfo -> beanConverter.transToAutoAccountingExcelRow(nonDeductionRepaymentInfo)).collect(Collectors.toList());
        //制作excel表格
        HSSFWorkbook workBook = AutoAccountUploadService.createWorkBook(autoAccountingExcelRows);
        //上传excel表格
        AutoAccountUploadService.upload(workBook);

        //更新是否上传属性为已上传:1,计算收银宝还款的手续费
        original.forEach(nonDeductionRepaymentInfo -> {
            nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.SUCCESS.getCode());
            nonDeductionRepaymentInfo.setGmtModified(new Date());
            nonDeductionRepaymentInfo.setModifiedId(Utility.getLoginUserId(session));
            calculateHandlingCharge(nonDeductionRepaymentInfo);
        });

        nonDeductionRepaymentInfoRepository.save(original);
        LOGGER.info("上传非代扣还款信息成功,操作人:[{}],上传的记录id:{}", Utility.getLoginUserName(session), afterFilter.stream().map(NonDeductionRepaymentInfo::getId).collect(Collectors.toList()));
        return "1";
    }

    /**
     * 还款金额不足以结清的操作
     *
     * @param original
     * @param iterator
     * @param nonDeductionRepaymentInfo
     * @return
     */
    private String amountIsNotSufficient(List<NonDeductionRepaymentInfo> original, Iterator<NonDeductionRepaymentInfo> iterator, NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        //从将要转换为excel的list中移除记录
        iterator.remove();
        //从原始的list中移除不能足额还款的记录
        original.removeIf(deduction -> StringUtils.equals(deduction.getContractNo(), nonDeductionRepaymentInfo.getContractNo()));
        String str = "合同编号:" + nonDeductionRepaymentInfo.getContractNo() + ",还款人:" + nonDeductionRepaymentInfo.getCustomerName() + ",已还金额不足以结清";
        LOGGER.debug(str + ",未上传入账文件,非代扣渠道");
        return str + ",本次操作所有还款都未上传入账文件";
    }

    /**
     * 计算手续费
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     */
    private void calculateHandlingCharge(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentMethod(),
                NonDeductionRepaymentMethodEnum.THIRD_PARTY_PAYMENT.getValue())) {
            //如果还款方式是通联收银宝,计算手续费,手续费四舍五入保留两位小数
            nonDeductionRepaymentInfo.setHandlingCharge(nonDeductionRepaymentInfo.getRepaymentAmount().multiply(allInPayConfig.getHandlingChargeRate()).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            nonDeductionRepaymentInfo.setHandlingCharge(BigDecimal.ZERO);
        }
    }

    /**
     * 合并同一合同号的不同渠道还款
     *
     * @param contractNoNonDeductionRepaymentInfoMap 合同号与非代扣还款信息的映射
     * @param nonDeductionRepaymentInfo              非代扣还款信息
     */
    private void mergeNonDeductionRepaymentInfo(HashMap<String, NonDeductionRepaymentInfo> contractNoNonDeductionRepaymentInfoMap, NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        //使用合同编号和入账日期做contractNoNonDeductionRepaymentInfoMap的key
        String key = nonDeductionRepaymentInfo.getContractNo() + nonDeductionRepaymentInfo.getAccountingDate();
        NonDeductionRepaymentInfo existedNonDeductionRepaymentInfo = contractNoNonDeductionRepaymentInfoMap.get(key);
        if (existedNonDeductionRepaymentInfo == null) {
            contractNoNonDeductionRepaymentInfoMap.put(key, nonDeductionRepaymentInfo);
        } else {
            BigDecimal existedRepaymentAmount = existedNonDeductionRepaymentInfo.getRepaymentAmount();
            BigDecimal newRepaymentAmount = nonDeductionRepaymentInfo.getRepaymentAmount();
            existedNonDeductionRepaymentInfo.setRepaymentAmount(existedRepaymentAmount.add(newRepaymentAmount));
        }
    }

    /**
     * 入账公司,还款方式,还款类别,入账银行(银行转账时)格式校验
     *
     * @param nonDeductionRepaymentInfos 非代扣还款数据
     * @throws FieldFormatCheckException 如果格式校验不通过时抛出
     */
    private void checkFieldFormat(List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos) throws FieldFormatCheckException {
        nonDeductionRepaymentInfos.forEach(nonDeductionRepaymentInfo -> {

            //判断入账公司名格式是否符合预期格式
            boolean chargeCompanyMeetExpectation = Arrays.stream(ChargeCompanyEnum.values()).anyMatch(chargeCompany -> StringUtils.equals(nonDeductionRepaymentInfo.getChargeCompany(), chargeCompany.getValue()));
            if (!chargeCompanyMeetExpectation) {
                throw new FieldFormatCheckException("合同号为[" + nonDeductionRepaymentInfo.getContractNo() + "]," +
                        "单位/个人还款为[" + nonDeductionRepaymentInfo.getCustomerName() + "]的入账公司格式有误");
            }
            //判断还款方式格式是否符合预期格式
            boolean repaymentMethodMeetExpectation = Arrays.stream(NonDeductionRepaymentMethodEnum.values()).anyMatch(repaymentMethod -> StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentMethod(), repaymentMethod.getValue()));
            if (!repaymentMethodMeetExpectation) {
                throw new FieldFormatCheckException("合同号为[" + nonDeductionRepaymentInfo.getContractNo() + "]," +
                        "单位/个人还款为[" + nonDeductionRepaymentInfo.getCustomerName() + "]的还款方式格式有误");
            }

            //判断还款类型格式是否符合预期格式
            boolean repaymentTypeMeetExpectation = Arrays.stream(RepaymentTypeEnum.values()).anyMatch(repaymentType -> StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentType(), repaymentType.getDesc()));
            if (!repaymentTypeMeetExpectation) {
                throw new FieldFormatCheckException("合同号为[" + nonDeductionRepaymentInfo.getContractNo() + "]," +
                        "单位/个人还款为[" + nonDeductionRepaymentInfo.getCustomerName() + "]的还款类别格式有误");
            }

            //银行转行或POS机刷卡需要验证入账银行名格式
            boolean hasAccountBank = StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentMethod(),
                    NonDeductionRepaymentMethodEnum.BANK_TRANSFER.getValue())
                    || StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentMethod(),
                    NonDeductionRepaymentMethodEnum.POS.getValue());
            if (hasAccountBank) {
                //判断入账银行名格式是否符合预期格式
                boolean bankMeetExpectation =
                        Arrays.stream(AccountBankEnum.values()).anyMatch(accountBank -> StringUtils.equals(nonDeductionRepaymentInfo.getBankName(), accountBank.getValue()));
                if (!bankMeetExpectation) {
                    throw new FieldFormatCheckException("合同号为[" + nonDeductionRepaymentInfo.getContractNo() + "]," +
                            "单位/个人还款为[" + nonDeductionRepaymentInfo.getCustomerName() + "]的入账银行名格式有误");
                }
            }
        });
    }

    /**
     * 拆分非代扣还款信息
     *
     * @param nonDeductionRepaymentInfos          传入的将要保存的非代扣还款信息
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     * @param session                             会话session
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void splitNonDeduction(List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos,
                                  Long originalNonDeductionRepaymentInfoId, HttpSession session) {
        //查询原始非代扣还款信息
        NonDeductionRepaymentInfo originalNonDeduction =
                nonDeductionRepaymentInfoRepository.findOne(originalNonDeductionRepaymentInfoId);
        nonDeductionRepaymentInfos.forEach(nonDeductionRepaymentInfo -> {
            trim(nonDeductionRepaymentInfo);
            //判断是否有合同号,如果有合同号,将是否系统自动匹配业务信息设置为否
            if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getContractNo())) {
                nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.FAIL.getCode());
            }
            //补充非代扣还款数据的业务信息
            supplementBusinessTransactionInfo(nonDeductionRepaymentInfo);
            nonDeductionRepaymentInfoRepository.saveAndFlush(nonDeductionRepaymentInfo);
            //将原始非代扣还款信息中的还款金额相应减少,并更新记录
            BigDecimal originalRepaymentAmount = originalNonDeduction.getRepaymentAmount();
            BigDecimal repaymentAmount = nonDeductionRepaymentInfo.getRepaymentAmount();
            BigDecimal surplusRepaymentAmount = originalRepaymentAmount.subtract(repaymentAmount).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            originalNonDeduction.setRepaymentAmount(surplusRepaymentAmount);
            //设置更新时间和更新操作人
            originalNonDeduction.setGmtModified(new Date());
            originalNonDeduction.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
            LOGGER.info("拆分非代扣还款信息成功,操作人:[{}],被拆分的记录id:[{}],拆分出来的记录id:[{}]", Utility.getLoginUserName(session),
                    originalNonDeductionRepaymentInfoId, nonDeductionRepaymentInfo.getId());
        });
        nonDeductionRepaymentInfoRepository.saveAndFlush(originalNonDeduction);

    }

    /**
     * 刷新CacheService,重新尝试查找匹配的业务信息
     *
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @param session                                 会话session
     * @param session                                 会话session
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retrySearchBusinessTransactionInfo(NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition, HttpSession session) {
        //首先刷新缓存服务
        CacheService.refresh();
        //查找信息不完整的非代扣还款信息
        List<NonDeductionRepaymentInfo> notIntegratedList =
                nonDeductionRepaymentInfoRepository.findByRepaymentTermDateBetweenAndIsIntegrated(nonDeductionRepaymentInfoQueryCondition.getRepaymentStartDate(), nonDeductionRepaymentInfoQueryCondition.getRepaymentEndDate(), String.valueOf(0));
        //查找匹配的业务信息
        notIntegratedList.forEach(nonDeductionRepaymentInfo -> {
            //判断是否有合同号,如果没有合同号,将是否系统自动匹配业务信息设置为否,如果通过系统自动匹配成功的,后面会设置为是
            if (StringUtils.isBlank(nonDeductionRepaymentInfo.getContractNo())) {
                nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.FAIL.getCode());
            }
            supplementBusinessTransactionInfo(nonDeductionRepaymentInfo);
            if (StringUtils.equals(nonDeductionRepaymentInfo.getIsIntegrated(), ConstantsEnum.SUCCESS.getCode())) {
                nonDeductionRepaymentInfo.setModifiedId(Utility.getLoginUserId(session));
                nonDeductionRepaymentInfo.setGmtModified(new Date());
            }
        });

        //筛选出找到匹配的业务信息的非代扣还款信息,并更新
        notIntegratedList.stream().filter(nonDeductionRepaymentInfo -> StringUtils.equals(nonDeductionRepaymentInfo.getIsIntegrated(), "1"))
                .forEach(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfoRepository.saveAndFlush(nonDeductionRepaymentInfo));
    }

    /**
     * 补充非代扣还款数据的业务信息
     *
     * @param nonDeductionRepaymentInfo 非代扣还款数据
     */
    private void supplementBusinessTransactionInfo(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        String contractNo = nonDeductionRepaymentInfo.getContractNo();
        if (StringUtils.isBlank(contractNo)) {
            NonDeductionRepaymentInfoCalculationService.searchBusinessTransaction(nonDeductionRepaymentInfo);
            //判断是否自动匹配到了业务信息
            if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getContractNo())) {
                nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.SUCCESS.getCode());
            }
        } else {
            Map<String, BusinessTransaction> businessTransactionCacheMap =
                    CacheService.getBusinessTransactionCacheMap();
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
        //计算手续费
        calculateHandlingCharge(nonDeductionRepaymentInfo);

    }

    /**
     * 批量拆分非代扣还款信息
     *
     * @param uploadFile                          包含拆分信息的excel文件
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     * @param session                             会话session
     * @throws NoSuchFieldException   读取excel有异常时抛出
     * @throws InstantiationException 读取excel有异常时抛出
     * @throws IllegalAccessException 读取excel有异常时抛出
     * @throws IOException            读取excel有异常时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSplitNonDeduction(MultipartFile uploadFile, Long originalNonDeductionRepaymentInfoId,
                                       HttpSession session) throws NoSuchFieldException, InstantiationException,
            IllegalAccessException, IOException {
        //首先将传入的包含拆分信息的excel文件转换为map<sheetName,List<NonDeductionRepaymentInfo>>,其中sheetName为入账公司名
        Map<String, List<NonDeductionRepaymentInfo>> resultMap = PoiUtil.readExcel(uploadFile,
                SPLIT_COLUMN_NAME_AND_FIELD_NAME_MAP, NonDeductionRepaymentInfo.class);
        NonDeductionRepaymentInfo originalNonDeduction = nonDeductionRepaymentInfoRepository.findOne(originalNonDeductionRepaymentInfoId);
        //拆分信息
        List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = new ArrayList<>();
        //设置NonDeductionRepaymentInfo对象的入账公司,要求上传的excel表格的sheet名是入账公司的中文名称
        resultMap.forEach((chargeCompany, list) -> list.forEach(nonDeductionRepaymentInfo -> {
            //设置入账公司
            nonDeductionRepaymentInfo.setChargeCompany(chargeCompany);
            //设置信息是否完整,判断标准:是否有有对应的合同号
            if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getContractNo())) {
                //通过合同号查询缓存中的查询业务信息
                BusinessTransaction businessTransaction =
                        CacheService.getBusinessTransactionCacheMap().get(nonDeductionRepaymentInfo.getContractNo());
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
            //设置入账日期
            if (nonDeductionRepaymentInfo.getAccountingDate() == null) {
                //判断入账日期是否为null,如果为null设置为还款日期
                nonDeductionRepaymentInfo.setAccountingDate(nonDeductionRepaymentInfo.getRepaymentTermDate());
            }
            //还款方式,还款类别,入账银行使用被拆分记录的信息
            nonDeductionRepaymentInfo.setRepaymentMethod(originalNonDeduction.getRepaymentMethod());
            nonDeductionRepaymentInfo.setRepaymentType(originalNonDeduction.getRepaymentType());
            nonDeductionRepaymentInfo.setBankName(originalNonDeduction.getBankName());
            //设置被拆分记录的Id
            nonDeductionRepaymentInfo.setOriginalId(originalNonDeductionRepaymentInfoId);
            nonDeductionRepaymentInfos.add(nonDeductionRepaymentInfo);
        }));
        //进行拆分的方法
        splitNonDeduction(nonDeductionRepaymentInfos, originalNonDeductionRepaymentInfoId, session);

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
        List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos =
                nonDeductionRepaymentInfoRepository.findAll(getSpecification(nonDeductionRepaymentInfoQueryCondition));
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
            if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getBankName())) {
                stringBuilder.append("-").append(nonDeductionRepaymentInfo.getBankName());
            }
            cell.setCellValue(stringBuilder.toString());
            //清空
            stringBuilder.delete(0, stringBuilder.length());
            cell = row.createCell(6);
            cell.setCellValue(nonDeductionRepaymentInfo.getRepaymentType());
            cell = row.createCell(7);
            cell.setCellValue(nonDeductionRepaymentInfo.getRepaymentAmount().toString());
            cell = row.createCell(8);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(nonDeductionRepaymentInfo.getAccountingDate());
            cell = row.createCell(9);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(nonDeductionRepaymentInfo.getGmtCreate());
            cell = row.createCell(10);
            cell.setCellValue(nonDeductionRepaymentInfo.getRemark());
            i++;
        }
        return workbook;
    }

    /**
     * 修改上传入账文件状态:已上传修改为未上传,未上传修改为已上传
     *
     * @param ids     一个或多个记录的id
     * @param session 会话session
     */
    @Override
    public void modifyUploadStatus(String ids, HttpSession session) {
        //处理ids,先用逗号切割,然后转为List
        List<Long> idsList = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        //根据id查询对应的记录
        List<NonDeductionRepaymentInfo> all = nonDeductionRepaymentInfoRepository.findAll(idsList);

        //设置成已上传的id
        ArrayList<Long> setUploaded = new ArrayList<>();
        //设置成未上传的id
        ArrayList<Long> setNonUploaded = new ArrayList<>();
        all.forEach(nonDeductionRepaymentInfo -> {
            if (StringUtils.equals(nonDeductionRepaymentInfo.getIsUploaded(), ConstantsEnum.SUCCESS.getCode())) {
                nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.FAIL.getCode());

                setNonUploaded.add(nonDeductionRepaymentInfo.getId());
            } else {
                nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.SUCCESS.getCode());
                setUploaded.add(nonDeductionRepaymentInfo.getId());
            }
            nonDeductionRepaymentInfo.setGmtModified(new Date());
            nonDeductionRepaymentInfo.setModifiedId(Utility.getLoginUserId(session));
            nonDeductionRepaymentInfoRepository.saveAndFlush(nonDeductionRepaymentInfo);
        });

        LOGGER.info("修改上传入账文件状态成功,操作人:[{}],修改为已上传记录id:{},修改为未上传记录id:{}", Utility.getLoginUserName(session), setUploaded, setNonUploaded);
    }

    /**
     * 根据id查询非代扣还款信息
     *
     * @param ids 要查询的id
     * @return 返回查询到的非代扣还款信息
     */
    @Override
    public List<NonDeductionRepaymentInfo> listNonDeductions(String ids) {
        //处理ids,先用逗号切割,然后转为List
        List<Long> idsList = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        //根据id查询对应的记录
        return nonDeductionRepaymentInfoRepository.findAll(idsList);
    }

    /**
     * 清除非代扣还款信息属性的两端空格
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     */
    private void trim(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getContractNo())) {
            nonDeductionRepaymentInfo.setContractNo(nonDeductionRepaymentInfo.getContractNo().trim());
        }
        if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getCustomerName())) {
            nonDeductionRepaymentInfo.setCustomerName(nonDeductionRepaymentInfo.getCustomerName().trim());
        }

    }
}
