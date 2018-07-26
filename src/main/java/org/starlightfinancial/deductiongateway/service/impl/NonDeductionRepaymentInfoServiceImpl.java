package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfoRepository;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
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

    private static final HashMap<String, String> COLUMN_NAME_AND_FIELD_NAME_MAP = new HashMap<String, String>(6) {
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
        Map<String, List<NonDeductionRepaymentInfo>> resultMap = PoiUtil.readExcel(uploadFile, COLUMN_NAME_AND_FIELD_NAME_MAP, NonDeductionRepaymentInfo.class);
        //设置NonDeductionRepaymentInfo对象的入账公司,要求上传的excel表格的sheet名是入账公司的中文名称
        resultMap.forEach((chargeCompany, list) -> list.forEach(nonDeductionRepaymentInfo -> {
            //设置入账公司
            nonDeductionRepaymentInfo.setChargeCompany(chargeCompany);
            //设置信息是否完整,判断标准:是否有有对应的dateId
            nonDeductionRepaymentInfo.setIsIntegrated(String.valueOf(0));
            //设置是否上传自动入账文件
            nonDeductionRepaymentInfo.setIsUploaded(String.valueOf(0));
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
     * @param pageBean     页面参数对象
     * @param startDate    还款开始时间
     * @param endDate      还款结束时间
     * @param customerName 客户名称
     * @param contractNo   合同号
     * @param isIntegrated 是否完整
     * @return 返回根据条件查询到的记录
     */
    @Override
    public PageBean queryNonDeductionRepaymentInfo(PageBean pageBean, Date startDate, Date endDate, String customerName, String contractNo, String isIntegrated) {
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 0);
        Page<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = nonDeductionRepaymentInfoRepository.findAll(new Specification<NonDeductionRepaymentInfo>() {
            @Override
            public Predicate toPredicate(Root<NonDeductionRepaymentInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                String[] split = isIntegrated.split(",");
                predicates.add(cb.in(root.get("isIntegrated")).value(Arrays.asList(split)));
                predicates.add(cb.greaterThanOrEqualTo(root.get("repaymentTermDate"), startDate));
                predicates.add(cb.lessThanOrEqualTo(root.get("repaymentTermDate"), endDate));
                //客户名非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(customerName)) {
                    predicates.add(cb.equal(root.get("customerName"), customerName));
                }
                //合同号非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(contractNo)) {
                    predicates.add(cb.equal(root.get("contractNo"), contractNo));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        }, pageRequest);
        if (nonDeductionRepaymentInfos.hasContent()) {
            pageBean.setTotal(nonDeductionRepaymentInfos.getTotalElements());
            pageBean.setRows(nonDeductionRepaymentInfos.getContent());
            return pageBean;
        }
        return null;
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
        afterFilter.forEach(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfo.setIsUploaded(String.valueOf("1")));
        nonDeductionRepaymentInfoRepository.save(afterFilter);
        LOGGER.info("上传非代扣还款信息成功,操作人:[{}],上传的记录id:[{}]", Utility.getLoginUserName(session), ids);
    }

    /**
     * 拆分非代扣还款信息
     *
     * @param nonDeductionRepaymentInfo           由页面传入的非代扣还款信息
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void splitNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, Long originalNonDeductionRepaymentInfoId) {
        //查询原始非代扣还款信息
        NonDeductionRepaymentInfo originalNonDeduction = nonDeductionRepaymentInfoRepository.findOne(originalNonDeductionRepaymentInfoId);

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

        nonDeductionRepaymentInfoRepository.saveAndFlush(originalNonDeduction);
        LOGGER.info("拆分非代扣还款信息成功,操作人:[{}],被拆分的记录id:[{}],拆分出来的记录id:[{}]", nonDeductionRepaymentInfo.getCreateId(), originalNonDeductionRepaymentInfoId, nonDeductionRepaymentInfo.getId());


    }

    /**
     * 设置非代扣还款数据的业务信息
     *
     * @param nonDeductionRepaymentInfo 非代扣还款数据
     */
    private void setBusinessTransactionInfo(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        String contractNo = nonDeductionRepaymentInfo.getContractNo();
        if (contractNo == null) {
            NonDeductionRepaymentInfoCalculationService.searchBusinessTransaction(nonDeductionRepaymentInfo);
        } else {
            Map<String, BusinessTransaction> businessTransactionCacheMap = CacheService.getBusinessTransactionCacheMap();
            BusinessTransaction businessTransaction = businessTransactionCacheMap.get(contractNo);
            if (businessTransaction != null) {
                nonDeductionRepaymentInfo.setDateId(businessTransaction.getDateId());
            }
        }
        //判断数据是否完整
        if (nonDeductionRepaymentInfo.getDateId() != null) {
            nonDeductionRepaymentInfo.setIsIntegrated(String.valueOf(1));
        } else {
            nonDeductionRepaymentInfo.setIsIntegrated(String.valueOf(0));
        }

    }


}
