package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.dao.BusinessTransactionDao;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssue;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfoRepository;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueQueryCondition;
import org.starlightfinancial.deductiongateway.service.LoanIssueService;
import org.starlightfinancial.deductiongateway.strategy.LoanIssueStrategy;
import org.starlightfinancial.deductiongateway.strategy.LoanIssueStrategyContext;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/18 16:33
 * @Modified By:
 */
@Service
public class LoanIssueServiceImpl implements LoanIssueService {

    @Autowired
    private LoanIssueBasicInfoRepository loanIssueBasicInfoRepository;

    @Autowired
    private BusinessTransactionDao businessTransactionDao;

    @Autowired
    private BaofuConfig baofuConfig;

    @Autowired
    private LoanIssueStrategyContext loanIssueStrategyContext;


    /**
     * 保存贷款发放基本信息
     *
     * @param loanIssueBasicInfos 待入库的贷款发放基本信息
     * @return 返回已经保存后的贷款发放基本信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LoanIssueBasicInfo> saveLoanIssueBasicInfo(List<LoanIssueBasicInfo> loanIssueBasicInfos) {

        ArrayList<String> transactionNos = new ArrayList<>();
        //生成商户订单号:商户编号+14位日期+3位随机数
        int[] randomArray = Utility.randomArray(0, 999, loanIssueBasicInfos.size());
        assert randomArray != null;
        Arrays.stream(randomArray).forEach(i -> {
            transactionNos.add(baofuConfig.getClassicPayMemberId() + Utility.getTimestamp() + String.format("%03d", i));
        });

        for (int i = 0; i < loanIssueBasicInfos.size(); i++) {
            LoanIssueBasicInfo loanIssueBasicInfo = loanIssueBasicInfos.get(i);
            if (Objects.isNull(loanIssueBasicInfo.getDateId())) {
                //判断dateId是否为空,如果是,补齐数据
                loanIssueBasicInfo.setDateId(businessTransactionDao.findDateIdByContractNo(loanIssueBasicInfo.getContractNo()));
            }
            if (StringUtils.isBlank(loanIssueBasicInfo.getBusinessNo())) {
                loanIssueBasicInfo.setBusinessNo(businessTransactionDao.findBusinessNoByContractNo(loanIssueBasicInfo.getContractNo()));
            }
            loanIssueBasicInfo.setGmtCreate(new Date());
            loanIssueBasicInfo.setGmtModified(loanIssueBasicInfo.getGmtCreate());
            loanIssueBasicInfo.setModifiedId(loanIssueBasicInfo.getCreateId());
            if (StringUtils.isEmpty(loanIssueBasicInfo.getToBankProvince())) {
                loanIssueBasicInfo.setToBankProvince("");
            }
            if (StringUtils.isEmpty(loanIssueBasicInfo.getToBankCity())) {
                loanIssueBasicInfo.setToBankProvince("");
            }
            if (StringUtils.isEmpty(loanIssueBasicInfo.getToBankProvince())) {
                loanIssueBasicInfo.setToBankProvince("");
            }
            LoanIssue loanIssue = new LoanIssue();
            loanIssue.setLoanIssueBasicInfo(loanIssueBasicInfo);
            //设置订单号
            loanIssue.setTransactionNo(transactionNos.get(i));
            //设置交易发起日期
            loanIssue.setTransactionStartTime(new Date());
            loanIssue.setGmtCreate(new Date());
            loanIssue.setCreateId(loanIssueBasicInfo.getCreateId());
            loanIssue.setGmtModified(loanIssue.getGmtCreate());
            loanIssue.setModifiedId(loanIssue.getCreateId());
            loanIssueBasicInfo.setLoanIssue(loanIssue);

            loanIssueBasicInfoRepository.saveAndFlush(loanIssueBasicInfo);
        }
        return loanIssueBasicInfos;
    }

    /**
     * 贷款发放操作
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     */
    @Override
    public void loanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        //通过贷款放款渠道对集合进行分组
        Map<String, List<LoanIssueBasicInfo>> channelMap = loanIssueBasicInfos.stream().collect(Collectors.groupingBy(LoanIssueBasicInfo::getChannel));
        channelMap.forEach((channel, list) -> {
            LoanIssueStrategy loanIssueStrategy = loanIssueStrategyContext.getLoanIssueStrategy(channel);
            loanIssueStrategy.loanIssue(list);
        });
    }

    /**
     * 查询数据库中的记录
     *
     * @param pageBean                分页信息
     * @param loanIssueQueryCondition
     * @return 返回查询到的数据
     */
    @Override
    public PageBean queryLoanIssue(PageBean pageBean, LoanIssueQueryCondition loanIssueQueryCondition) {

        Specification<LoanIssueBasicInfo> specification = new Specification<LoanIssueBasicInfo>() {
            @Override
            public Predicate toPredicate(Root<LoanIssueBasicInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                ArrayList<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.between(root.get("gmtCreate"), loanIssueQueryCondition.getStartDate(), Utility.toMidNight(loanIssueQueryCondition.getEndDate())));
                //收款人名字不为空
                if (StringUtils.isNotBlank(loanIssueQueryCondition.getAccountName().trim())) {
                    predicates.add(cb.equal(root.<String>get("accountName"), loanIssueQueryCondition.getAccountName().trim()));
                }
                //合同编号不为空
                if (StringUtils.isNotBlank(loanIssueQueryCondition.getContractNo().trim())) {
                    predicates.add(cb.equal(root.<String>get("contractNo"), loanIssueQueryCondition.getContractNo().trim()));
                }
                //业务编号不为空
                if (StringUtils.isNotBlank(loanIssueQueryCondition.getBusinessNo().trim())) {
                    predicates.add(cb.equal(root.<String>get("businessNo"), loanIssueQueryCondition.getBusinessNo().trim()));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        long count = loanIssueBasicInfoRepository.count(specification);
        if (count == 0) {
            //如果查询出来的总记录数为0,直接返回null,避免后续查询代码执行
            return null;
        }
        double tempTotalPageCount = count / (pageBean.getPageSize().doubleValue());
        double totalPageCount = Math.ceil(tempTotalPageCount == 0 ? 1 : tempTotalPageCount);
        if (totalPageCount < pageBean.getPageNumber()) {
            pageBean.setPageNumber(1);
        }
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 0);
        Page<LoanIssueBasicInfo> loanIssueBasicInfos = loanIssueBasicInfoRepository.findAll(specification, pageRequest);
        if (loanIssueBasicInfos.hasContent()) {
            pageBean.setTotal(loanIssueBasicInfos.getTotalElements());
            pageBean.setRows(loanIssueBasicInfos.getContent());
            return pageBean;
        }
        return null;

    }

    /**
     * 通过id查询记录
     *
     * @param ids 记录id,比如:1,2,3,使用时切割出来
     * @return 返回查询到的记录
     */
    @Override
    public List<LoanIssueBasicInfo> queryLoanIssueListByIds(String ids) {
        //将ids切割出来,转换为Long型集合
        List<Long> idsList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        return loanIssueBasicInfoRepository.findAll(idsList);
    }
}
