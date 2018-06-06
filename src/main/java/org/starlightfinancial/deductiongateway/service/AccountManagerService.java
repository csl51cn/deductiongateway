package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.List;

/**
 * 卡号管理Service接口
 *
 * @author senlin.deng
 */
public interface AccountManagerService {

    /**
     * 按条件分页查询记录
     *
     * @param contractNo  合同编号
     * @param bizNo       业务编号
     * @param accountName 账户名
     * @param pageBean    分页对象
     * @return
     */
    PageBean queryAccount(String contractNo, String bizNo, String accountName, PageBean pageBean);

    /**
     * 更新记录
     *
     * @param accountManager 代扣卡对象
     */
    void updateAccount(AccountManager accountManager);

    /**
     * 查找最后一条记录
     *
     * @return
     */
    List findLastAccount();

    /**
     * 添加代扣卡信息
     *
     * @param bizNo 业务编号
     */
    Message addAccount(String bizNo);

    /**
     * 银联-查询是否签约
     *
     * @param id 记录的id
     * @return
     */
    Message unionPayIsSigned(Integer id);


    /**
     * 银联-发送签约短信
     *
     * @param id              记录的id
     * @param account         银行卡号
     * @param certificateType 证件类型
     * @param certificateNo   证件号码
     * @param accountName     账户名
     * @param mobile          手机号
     * @return
     */
    Message unionPaySendSignSmsCode(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile);

    /**
     * 银联--签约
     *
     * @param id              记录id
     * @param account         账户卡号
     * @param certificateType 证件类型
     * @param certificateNo   证件号码
     * @param accountName     账户名
     * @param mobile          手机号
     * @param smsCode         短信验证码
     * @param merOrderNo      短信验证码对应的订单号
     * @return
     */
    Message unionPaySign(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile, String smsCode, String merOrderNo);


    /**
     * 宝付-查询是否签约
     *
     * @param id 记录的id
     * @return
     */
    Message baoFuIsSigned(Integer id);

    /**
     * 宝付--发送签约短信
     *
     * @param id              记录的id
     * @param account         银行卡号
     * @param certificateType 证件类型
     * @param certificateNo   证件号码
     * @param accountName     账户名
     * @param mobile          手机号
     * @return
     */
    Message baoFuSendSignSmsCode(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile);

    /**
     * 宝付-签约
     *
     * @param id         记录id
     * @param smsCode    短信验证码
     * @param uniqueCode 预签约唯一码
     * @return
     */
    Message baoFuSign(Integer id, String smsCode, String uniqueCode);
}
