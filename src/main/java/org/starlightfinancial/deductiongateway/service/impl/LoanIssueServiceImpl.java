package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.config.BaofuConfig;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.dao.BusinessTransactionDao;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.enums.LoanIssueBankEnum;
import org.starlightfinancial.deductiongateway.enums.LoanIssueChannelEnum;
import org.starlightfinancial.deductiongateway.enums.LoanIssueStatusEnum;
import org.starlightfinancial.deductiongateway.service.LoanIssueService;
import org.starlightfinancial.deductiongateway.service.SystemService;
import org.starlightfinancial.deductiongateway.strategy.LoanIssueStrategy;
import org.starlightfinancial.deductiongateway.strategy.LoanIssueStrategyContext;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private LoanIssueRepository loanIssueRepository;

    @Autowired
    private BusinessTransactionDao businessTransactionDao;

    @Autowired
    private BaofuConfig baofuConfig;

    @Autowired
    private LoanIssueStrategyContext loanIssueStrategyContext;

    @Autowired
    private SystemService systemService;

    private static final String CHECK_USER = "hong.li";

    /**
     * 导出资金代付数据表头
     */
    private static final String[] EXPORT_COLUMN_NAME = new String[]{"订单号", "业务编号", "合同编号", "收款人姓名", "收款账号", "收款账户所属银行",
            "开户行", "身份证", "手机", "放款金额", "交易结果", "生成时间", "发起交易时间", "交易结束时间"};

    /**
     * 保存贷款发放基本信息
     *
     * @param loanIssueBasicInfos 待入库的贷款发放基本信息
     * @return 返回已经保存后的贷款发放基本信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LoanIssueBasicInfo> saveLoanIssueBasicInfo(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        for (LoanIssueBasicInfo loanIssueBasicInfo : loanIssueBasicInfos) {
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
            LoanIssue loanIssue = createLoanIssue(loanIssueBasicInfo);
            loanIssueBasicInfo.setLoanIssues(Collections.singletonList(loanIssue));

            loanIssueBasicInfoRepository.saveAndFlush(loanIssueBasicInfo);
        }
        return loanIssueBasicInfos;
    }

    /**
     * 生成 loanIssue对象
     *
     * @param loanIssueBasicInfo 放款基本信息
     * @return 返回loanIssue对象
     */
    private LoanIssue createLoanIssue(LoanIssueBasicInfo loanIssueBasicInfo) {
        LoanIssue loanIssue = new LoanIssue();
        loanIssue.setLoanIssueBasicInfo(loanIssueBasicInfo);
        loanIssue.setGmtCreate(new Date());
        loanIssue.setCreateId(loanIssueBasicInfo.getCreateId());
        loanIssue.setGmtModified(loanIssue.getGmtCreate());
        loanIssue.setModifiedId(loanIssue.getCreateId());
        loanIssue.setIsLast(ConstantsEnum.SUCCESS.getCode());
        loanIssue.setTransactionSummary(loanIssueBasicInfo.getToAccountName() + "  " + loanIssueBasicInfo.getContractNo());
        return loanIssue;
    }

    /**
     * 贷款发放操作
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        ArrayList<String> transactionNos = new ArrayList<>();
        //生成商户订单号:商户编号+14位日期+3位随机数
        int[] randomArray = Utility.randomArray(0, 999, loanIssueBasicInfos.size());
        assert randomArray != null;
        Arrays.stream(randomArray).forEach(i -> {
            transactionNos.add(baofuConfig.getClassicPayMemberId() + Utility.getTimestamp() + String.format("%03d", i));
        });
        for (int i = 0; i < loanIssueBasicInfos.size(); i++) {
            LoanIssueBasicInfo loanIssueBasicInfo = loanIssueBasicInfos.get(i);
            List<LoanIssue> loanIssues = loanIssueBasicInfo.getLoanIssues();
            AtomicBoolean needCreateLoanIssue = new AtomicBoolean(false);
            loanIssues.forEach(loanIssue -> {
                if (StringUtils.equals(loanIssue.getTransactionStatus(), LoanIssueStatusEnum.STATUS2.getCode()) || StringUtils.equals(loanIssue.getTransactionStatus(), LoanIssueStatusEnum.STATUS3.getCode())) {
                    //当交易状态是失败或者退款时,需要将原有的记录标记为不是最新并生成一条新的记录
                    loanIssue.setIsLast(ConstantsEnum.FAIL.getCode());
                    needCreateLoanIssue.set(true);
                }
            });

            LoanIssue loanIssue;
            if (needCreateLoanIssue.get()) {
                //需要重新生成的情况
                loanIssue = createLoanIssue(loanIssueBasicInfo);
                loanIssueBasicInfo.getLoanIssues().add(loanIssue);
            } else {
                //此时是只有一条记录的情况
                loanIssue = loanIssues.get(0);
            }
            //设置订单号
            loanIssue.setTransactionNo(transactionNos.get(i));
            //设置交易发起日期
            loanIssue.setTransactionStartTime(new Date());
            //保存订单号
            loanIssueBasicInfoRepository.saveAndFlush(loanIssueBasicInfo);
        }

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

        Specification<LoanIssueBasicInfo> specification = getLoanIssueBasicInfoSpecification(loanIssueQueryCondition, true);
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
     * 组装查询条件
     *
     * @param loanIssueQueryCondition 查询条件
     * @param onlyQueryLast           是否只查询最新记录
     * @return
     */
    private Specification<LoanIssueBasicInfo> getLoanIssueBasicInfoSpecification(LoanIssueQueryCondition loanIssueQueryCondition, boolean onlyQueryLast) {
        return (root, query, cb) -> {
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
            if (onlyQueryLast) {
                predicates.add(cb.equal(root.join("loanIssues", JoinType.LEFT).get("isLast"), "1"));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
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
        return loanIssueBasicInfoRepository.findDistinctByIdIn(idsList);
    }

    /**
     * 查询放款结果
     *
     * @param ids 记录id
     */
    @Override
    public Message queryLoanIssueResult(String ids) {
        List<LoanIssueBasicInfo> loanIssueBasicInfos = queryLoanIssueListByIds(ids);
        //通过贷款放款渠道对集合进行分组
        Map<String, List<LoanIssueBasicInfo>> channelMap = loanIssueBasicInfos.stream().collect(Collectors.groupingBy(LoanIssueBasicInfo::getChannel));
        ArrayList<Message> messages = new ArrayList<>();
        channelMap.forEach((channel, list) -> {
            LoanIssueStrategy loanIssueStrategy = loanIssueStrategyContext.getLoanIssueStrategy(channel);
            Message message = loanIssueStrategy.queryLoanIssueResult(list);
            messages.add(message);
        });

        List<Message> success = messages.stream().filter(message -> StringUtils.equals(message.getCode(), ConstantsEnum.REQUEST_SUCCESS.getCode())).collect(Collectors.toList());
        List<Message> fail = messages.stream().filter(message -> StringUtils.equals(message.getCode(), ConstantsEnum.REQUEST_FAIL.getCode())).collect(Collectors.toList());
        if (fail.size() > 0 && success.size() > 0) {
            return Message.fail("部分操作失败,请查看收单状态");
        } else if (fail.size() == 0) {
            return Message.success();
        } else {
            return Message.fail("操作失败");
        }
    }

    /**
     * 查询代付交易退款结果
     *
     * @param queryDate 查询日期,只允许查询某一天内的记录
     */
    @Override
    public Message queryLoanIssueRefund(Date queryDate) {
        LoanIssueStrategy loanIssueStrategy = loanIssueStrategyContext.getLoanIssueStrategy(LoanIssueChannelEnum.BAO_FU.getCode());
        return loanIssueStrategy.queryLoanIssueRefund(queryDate);
    }

    /**
     * 根据条件导出数据
     *
     * @param loanIssueQueryCondition 查询条件
     * @return excel表格
     */
    @Override
    public Workbook exportXLS(LoanIssueQueryCondition loanIssueQueryCondition) {
        //查询所有非代扣还款信息
        List<LoanIssueBasicInfo> loanIssueBasicInfos =
                loanIssueBasicInfoRepository.findAll(getLoanIssueBasicInfoSpecification(loanIssueQueryCondition, false));
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
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        int i = 1;
        List<LoanIssueBasicInfoExcelRow> loanIssueBasicInfoExcelRows = BeanConverter.transToLoanIssueBasicInfoExcelRowList(loanIssueBasicInfos);
        StringBuilder stringBuilder = new StringBuilder();
        for (LoanIssueBasicInfoExcelRow loanIssueBasicInfoRow : loanIssueBasicInfoExcelRows) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(loanIssueBasicInfoRow.getTransactionNo());
            cell = row.createCell(1);
            cell.setCellValue(loanIssueBasicInfoRow.getBusinessNo());
            cell = row.createCell(2);
            cell.setCellValue(loanIssueBasicInfoRow.getContractNo());
            cell = row.createCell(3);
            cell.setCellValue(loanIssueBasicInfoRow.getToAccountName());
            cell = row.createCell(4);
            cell.setCellValue(loanIssueBasicInfoRow.getToAccountNo());
            cell = row.createCell(5);
            cell.setCellValue(LoanIssueBankEnum.getBankNameByCode(loanIssueBasicInfoRow.getToBankNameId()));
            cell = row.createCell(6);
            stringBuilder.append(loanIssueBasicInfoRow.getToBankProvince()).append(loanIssueBasicInfoRow.getToBankCity()).append(loanIssueBasicInfoRow.getToBankBranch());
            cell.setCellValue(stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
            cell = row.createCell(7);
            cell.setCellValue(loanIssueBasicInfoRow.getIdentityNo());
            cell = row.createCell(8);
            cell.setCellValue(loanIssueBasicInfoRow.getMobileNo());
            cell = row.createCell(9);
            cell.setCellValue(loanIssueBasicInfoRow.getIssueAmount().toString());
            cell = row.createCell(10);
            cell.setCellValue(LoanIssueStatusEnum.getDescByCode(loanIssueBasicInfoRow.getTransactionStatus()));
            cell = row.createCell(11);
            cell.setCellValue(loanIssueBasicInfoRow.getGmtCreate());
            cell.setCellStyle(dateStyle);
            cell = row.createCell(12);

            if (Objects.nonNull(loanIssueBasicInfoRow.getTransactionStartTime())) {
                cell.setCellValue(loanIssueBasicInfoRow.getTransactionStartTime());
                cell.setCellStyle(dateStyle);
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(13);

            if (Objects.nonNull(loanIssueBasicInfoRow.getTransactionEndTime())) {
                System.out.println(loanIssueBasicInfoRow.getTransactionEndTime() + " " + loanIssueBasicInfoRow.getBusinessNo());
                cell.setCellValue(loanIssueBasicInfoRow.getTransactionEndTime());
                cell.setCellStyle(dateStyle);
            } else {
                cell.setCellValue("");
            }
            i++;
        }
        return workbook;
    }

    /**
     * 更新
     *
     * @param loanIssueBasicInfo 贷款发放基本信息
     */
    @Override
    public void updateLoanIssue(LoanIssueBasicInfo loanIssueBasicInfo) {
        loanIssueBasicInfoRepository.saveAndFlush(loanIssueBasicInfo);
        LoanIssue loanIssue = findTheLastRecord(loanIssueBasicInfo);
        loanIssue.setTransactionSummary(loanIssueBasicInfo.getToAccountName() + " " + loanIssueBasicInfo.getContractNo());
        loanIssue.setLoanIssueBasicInfo(loanIssueBasicInfo);
        loanIssueRepository.saveAndFlush(loanIssue);
    }

    /**
     * 复核金额时,比对密码
     *
     * @param password 密码
     * @return
     */
    @Override
    public boolean checkLoanIssue(String password) {
        //目前只有指定的用户具有复核权限
        SysUser loginUser = systemService.findSysUser(CHECK_USER, password);
        return loginUser == null;
    }

    private LoanIssue findTheLastRecord(LoanIssueBasicInfo loanIssueBasicInfo) {
        List<LoanIssue> loanIssues = loanIssueBasicInfo.getLoanIssues();
        List<LoanIssue> collect = loanIssues.stream().filter(loanIssue -> StringUtils.equals(loanIssue.getIsLast(), ConstantsEnum.SUCCESS.getCode()) || Objects.isNull(loanIssue.getAcceptTransactionStatus())).collect(Collectors.toList());
        return collect.get(0);
    }
}
