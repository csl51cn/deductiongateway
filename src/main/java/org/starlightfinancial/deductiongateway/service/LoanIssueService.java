package org.starlightfinancial.deductiongateway.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueQueryCondition;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 代付操作Service接口
 * @date: Created in 2018/9/18 16:32
 * @Modified By:
 */
public interface LoanIssueService {

    /**
     * 保存贷款发放基本信息
     *
     * @param loanIssueBasicInfos 待入库的贷款发放基本信息
     * @return 返回已经保存后的贷款发放基本信息
     */
    List<LoanIssueBasicInfo> saveLoanIssueBasicInfo(List<LoanIssueBasicInfo> loanIssueBasicInfos);

    /**
     * 贷款发放操作
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     */
    void loanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos);


    /**
     * 查询数据库中的记录
     *
     * @param pageBean                分页信息
     * @param loanIssueQueryCondition
     * @return 返回查询到的数据
     */
    PageBean queryLoanIssue(PageBean pageBean, LoanIssueQueryCondition loanIssueQueryCondition);


    /**
     * 通过id查询记录
     *
     * @param ids 记录id,比如:1,2,3,使用时切割出来
     * @return 返回查询到的记录
     */
    List<LoanIssueBasicInfo> queryLoanIssueListByIds(String ids);

    /**
     * 查询放款结果
     *
     * @param ids 记录id
     * @return 返回执行结果
     */
    Message queryLoanIssueResult(String ids);


    /**
     * 查询贷款退款结果
     *
     * @param queryDate 查询日期,只允许查询某一天内的记录
     * @return 返回执行结果
     */
    Message queryLoanIssueRefund(Date queryDate);


    /**
     * 根据条件导出数据
     *
     * @param loanIssueQueryCondition 查询条件
     * @return excel表格
     */
    Workbook exportXLS(LoanIssueQueryCondition loanIssueQueryCondition);

    /**
     * 更新
     *
     * @param loanIssueBasicInfo 贷款发放基本信息
     */
    void updateLoanIssue(LoanIssueBasicInfo loanIssueBasicInfo);

    /**
     * 复核金额时,比对密码
     *
     * @param password 密码
     * @return
     */
    boolean checkLoanIssue(String password);
}
