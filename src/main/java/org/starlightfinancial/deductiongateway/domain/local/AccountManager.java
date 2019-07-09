package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 卡号管理实体类
 *
 * @author senlin.deng
 */
@Data
@Entity(name = "BU_ACCOUNT_MANAGER")
public class AccountManager {


    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "dateid")
    private Integer dateId;

    /**
     * 合同编号
     */
    @Column(name = "contractno")
    private String contractNo;

    /**
     * 业务编号
     */
    @Column(name = "bizno")
    private String bizNo;

    /**
     * 开户行
     */
    @Column(name = "bankname")
    private String bankName;

    /**
     * 账户名
     */
    @Column(name = "accountname")
    private String accountName;

    /**
     * 账户卡号
     */
    @Column(name = "account")
    private String account;

    /**
     * 证件类型
     */
    @Column(name = "certificatetype")
    private String certificateType;

    /**
     * 证件号码
     */
    @Column(name = "certificateno")
    private String certificateNo;


    /**
     * 扣款顺序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 修改时间
     */
    @Column(name = "changetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date changeTime;

    /**
     * 放款时间
     */
    @Column(name = "loandate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date loanDate;

    /**
     * 标志是否开启自动代扣,0:关闭,1:开启
     */
    @Column(name = "isenabled")
    private Integer isEnabled = 1;


    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 银联是否签约,0:未签约,1:已签约
     */
    @Column(name = "unionpay_is_signed")
    private Integer unionpayIsSigned = 0;

    /**
     * 宝付是否签约,0:未签约,1:已签约
     */
    @Column(name = "baofu_is_signed")
    private Integer baofuIsSigned = 0;

    /**
     * 银联协议号
     */
    @Column(name = "unionpay_protocol_no")
    private String unionpayProtocolNo;


    /**
     * 宝付协议号
     */
    @Column(name = "baofu_protocol_no")
    private String baofuProtocolNo;

    /**
     * 客户id
     */
    @Column(name = "customerid")
    private String customerId;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "createtime")
    private Date createTime;

    /**
     * 中金支付是否签约,0:未签约,1:已签约
     */
    @Column(name = "cpcn_is_signed")
    private Integer chinaPayClearNetIsSigned;

    /**
     * 中金支付绑定流水号
     */
    @Column(name = "cpcn_protocol_no")
    private String chinaPayClearNetProtocolNo;


    /**
     * 银联商业委托是否签约,0:未签约,1:已签约
     */
//    @Column(name="cpcn_is_signed")
    @Transient
    private Integer unionpayCommercialEntrustIsSigned;

    /**
     * 银联商业委托绑定流水号
     */
//    @Column(name = "cpcn_protocol_no")
    @Transient
    private String unionpayCommercialEntrustProtocolNo;

    /**
     * 平安商委是否签约
     */
    @Column(name = "pingan_is_signed")
    private String pingAnCommercialEntrustIsSigned;

    /**
     * 平安商委协议号
     */
    @Column(name = "pingan_protocol_no")
    private String pingAnCommercialEntrustProtocolNo;

    /**
     * 平安付会员号
     */
    @Column(name="pingan_customer_Id")
    private String  pingAnCustomerId;

    /**
     * 签约短信验证码
     */
    @Transient
    private String smsCode;
    /**
     * 短信验证码对应的订单号
     */
    @Transient
    private String merOrderNo;


}
