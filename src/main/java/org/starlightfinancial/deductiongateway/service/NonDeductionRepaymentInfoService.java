package org.starlightfinancial.deductiongateway.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfoQueryCondition;
import org.starlightfinancial.deductiongateway.exception.nondeduction.FieldFormatCheckException;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息Service
 * @date: Created in 2018/7/17 17:19
 * @Modified By:
 */
public interface NonDeductionRepaymentInfoService {
    /**
     * 非代扣还款信息文件导入
     *
     * @param uploadFile 文件
     * @param session    会话session
     * @throws Exception 处理文件导入异常
     */
    void importFile(MultipartFile uploadFile, HttpSession session) throws Exception;

    /**
     * 根据条件查询记录
     *
     * @param pageBean                                页面参数对象
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @param session                                 会话session
     * @return 返回根据条件查询到的记录
     */
    PageBean queryNonDeductionRepaymentInfo(PageBean pageBean, NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition, HttpSession session);

    /**
     * 保存记录
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     */
    void saveNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo);

    /**
     * 更新记录
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     */
    void updateNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo);


    /**
     * 上传自动入账文件
     *
     * @param ids     一条记录或多条记录id
     * @param session 会话session
     * @throws IOException               io异常时抛出
     * @throws FieldFormatCheckException 非代扣还款数据属性格式不符合预期时抛出
     */
    void uploadAutoAccountingFile(String ids, HttpSession session) throws IOException, FieldFormatCheckException;


    /**
     * 拆分非代扣还款信息
     *
     * @param nonDeductionRepaymentInfos          由页面传入的非代扣还款信息
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     */
    void splitNonDeduction(List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos, Long originalNonDeductionRepaymentInfoId);

    /**
     * 刷新CacheService,重新尝试查找匹配的业务信息
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param session   会话session
     */
    void retrySearchBusinessTransactionInfo(Date startDate, Date endDate, HttpSession session);

    /**
     * 批量拆分非代扣还款信息
     *
     * @param uploadFile                          包含拆分信息的excel文件
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     * @param session                             会话session
     * @throws Exception 异常时抛出
     */
    void batchSplitNonDeduction(MultipartFile uploadFile, Long originalNonDeductionRepaymentInfoId, HttpSession session) throws Exception;

    /**
     * 根据条件导出非代扣还款信息
     *
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @return excel表格
     */
    Workbook exportXLS(NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition);
}
