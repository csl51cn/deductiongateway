package org.starlightfinancial.deductiongateway.service;

import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

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
     * @param pageBean     页面参数对象
     * @param startDate    还款开始时间
     * @param endDate      还款结束时间
     * @param customerName 客户名称
     * @param contractNo   合同号
     * @return 返回根据条件查询到的记录
     */
    PageBean queryNonDeductionRepaymentInfo(PageBean pageBean, Date startDate, Date endDate, String customerName, String contractNo);

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
     * @param ids 一条记录或多条记录id
     * @throws IOException
     */
    void uploadAutoAccountingFile(String ids) throws IOException;
}
