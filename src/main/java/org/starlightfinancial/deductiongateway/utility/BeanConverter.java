package org.starlightfinancial.deductiongateway.utility;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.ServiceCompanyConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.BaoFuRequestParams;
import org.starlightfinancial.deductiongateway.baofu.domain.DataContent;
import org.starlightfinancial.deductiongateway.baofu.domain.RequestParams;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.enums.ChinaPayCertTypeEnum;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 提供MortgageDeduction, AutoBatchDeduction, 银联, 宝付请求参数类之间相互转换的方法
 * @date: Created in 2018/5/17 15:20
 * @Modified By:
 */
@Component
public class BeanConverter {

    private static final Logger log = LoggerFactory.getLogger(BeanConverter.class);

    @Autowired
    private ChinaPayConfig chinaPayConfig;

    @Autowired
    private BaofuConfig baofuConfig;

    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    private SysDictRepository sysDictRepository;

    @Autowired
    private ServiceCompanyConfig serviceCompanyConfig;

    /**
     * 将MortgageDeduction转换为银联请求参数类  银联快捷支付
     *
     * @param mortgageDeduction
     * @return
     */
    public ChinaPayRealTimeRequestParams transToChinaPayRealTimeRequestParams(MortgageDeduction mortgageDeduction) {
        ChinaPayRealTimeRequestParams chinaPayRealTimeRequestParams = new ChinaPayRealTimeRequestParams();
        //设置订单号
        chinaPayRealTimeRequestParams.setMerOrderNo(MerSeq.tickOrder());

        //设置订单金额
        String amount1 = mortgageDeduction.getSplitData1().toString();
        String amount2 = mortgageDeduction.getSplitData2().toString();
        int m1 = 0;
        if (StringUtils.isNotBlank(amount1)) {
            m1 = new BigDecimal(amount1).movePointRight(2).intValue();
        }
        int m2 = 0;
        if (StringUtils.isNotBlank(amount2)) {
            m2 = new BigDecimal(amount2).movePointRight(2).intValue();
        }
        chinaPayRealTimeRequestParams.setOrderAmt(String.valueOf(m1 + m2));

        //设置分账三个相关参数
        //设置分账方式:按金额分账
        chinaPayRealTimeRequestParams.setSplitMethod("0");
        //设置分账类型:实时分账
        chinaPayRealTimeRequestParams.setSplitType("0001");
        StringBuilder merSplitMsg = new StringBuilder(chinaPayConfig.getExpressRealTimeRunTongMemberId());
        merSplitMsg.append("^").append(m1);
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && m2 != 0) {
            //设置分账信息
            merSplitMsg.append(";").append(serviceCompanyConfig.getServiceCompanyCode(mortgageDeduction.getTarget(), DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getCode())).append("^").append(m2).append(";");

        }
        chinaPayRealTimeRequestParams.setMerSplitMsg(merSplitMsg.toString());
        AccountManager accountManager = accountManagerRepository.findByAccountAndSortAndContractNo(mortgageDeduction.getParam3(), 1, mortgageDeduction.getContractNo());
        //首先判断协议号是否存在,如果不存在使用五要素进行交易,五要素:卡号，证件类型 ，证件号，姓名，手机号
        if(StringUtils.isNotBlank(accountManager.getUnionpayProtocolNo())){
            //设置协议号
            chinaPayRealTimeRequestParams.setProtocolNo(accountManager.getUnionpayProtocolNo());
        }else{
            //设置卡号
            chinaPayRealTimeRequestParams.setCardNo(mortgageDeduction.getParam3());
            //设置证件类型
            chinaPayRealTimeRequestParams.setCertType(ChinaPayCertTypeEnum.CERT_TYPE_01.getCode());
            //设置证件号码
            chinaPayRealTimeRequestParams.setCertNo(mortgageDeduction.getParam6());
            //设置账户名
            chinaPayRealTimeRequestParams.setAccName(mortgageDeduction.getParam4());
            //设置
            chinaPayRealTimeRequestParams.setMobile(accountManager.getMobile());
        }
        return chinaPayRealTimeRequestParams;
    }

    /**
     * 将AutoBatchDeduction转换为MortgageDeduction
     *
     * @param autoBatchDeduction
     * @return
     */
    public MortgageDeduction transToMortgageDeduction(AutoBatchDeduction autoBatchDeduction) {
        MortgageDeduction mortgageDeduction = new MortgageDeduction();
        //设置合同编号
        mortgageDeduction.setContractNo(autoBatchDeduction.getContractNo());
        //设置银行名称,此时为中文
        mortgageDeduction.setParam1(autoBatchDeduction.getBankName());
        //设置卡折类型
        mortgageDeduction.setParam2("0");
        //设置卡号
        mortgageDeduction.setParam3(autoBatchDeduction.getAccout());
        //设置客户名称
        mortgageDeduction.setCustomerName(autoBatchDeduction.getCustomerName());
        //设置持卡人姓名
        mortgageDeduction.setParam4(autoBatchDeduction.getCustomerName());
        //设置本息
        mortgageDeduction.setSplitData1(autoBatchDeduction.getBxAmount());
        //设置服务费
        mortgageDeduction.setSplitData2(autoBatchDeduction.getFwfAmount());
        //设置服务费的管理公司

        //处理服务费管理公司
//        if (StringUtils.isNotBlank(autoBatchDeduction.getFwfCompamny()) && "铠岳".equals(autoBatchDeduction.getFwfCompamny().trim())) {
//            mortgageDeduction.setTarget(chinaPayConfig.getClassicKaiYueMemberId());
//        } else {
//            mortgageDeduction.setTarget(chinaPayConfig.getClassicRunKunMemberId());
//        }
        mortgageDeduction.setTarget(autoBatchDeduction.getFwfCompamny());
        //证件类型
        mortgageDeduction.setParam5(autoBatchDeduction.getCertificateType());
        //证件号
        mortgageDeduction.setParam6(autoBatchDeduction.getCertificateNo());

        //设置支付状态:0:失败,1:成功,2:暂无结果
        mortgageDeduction.setIssuccess("2");
        //是否发起过交易:0:是,1:否
        mortgageDeduction.setType("1");
        mortgageDeduction.setPlanNo(-1);
        mortgageDeduction.setIsoffs("0");
        //创建时间
        mortgageDeduction.setCreateDate(new Date());
        mortgageDeduction.setSplitType("0001");
        mortgageDeduction.setCreatId(113);
        mortgageDeduction.setParam1(handleBankName(mortgageDeduction.getParam1()));
        mortgageDeduction.setParam5(handleCertificateType(mortgageDeduction.getParam5()));
        return mortgageDeduction;
    }


    /**
     * 将MortgageDeduction转换为BaoFuRequestParams
     *
     * @param mortgageDeduction
     * @return
     */
    public BaoFuRequestParams transToBaoFuRequestParams(MortgageDeduction mortgageDeduction) {
        BaoFuRequestParams baoFuRequestParams = new BaoFuRequestParams();
        //设置订单号
        baoFuRequestParams.setTransId(MerSeq.tickOrder());
        //设置订单金额

        //设置订单金额
        String amount1 = mortgageDeduction.getSplitData1().toString();
        String amount2 = mortgageDeduction.getSplitData2().toString();
        int m1 = 0;
        if (StringUtils.isNotBlank(amount1)) {
            m1 = new BigDecimal(amount1).movePointRight(2).intValue();
        }
        int m2 = 0;
        if (StringUtils.isNotBlank(amount2)) {
            m2 = new BigDecimal(amount2).movePointRight(2).intValue();
        }
        baoFuRequestParams.setTxnAmt(String.valueOf(m1 + m2));
        StringBuilder shareInfo = new StringBuilder();
        shareInfo.append(baofuConfig.getProtocolMemberId()).append(",").append(m1);
        //设置分账信息 有服务费时才分账
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && m2 != 0) {
            shareInfo.append(";").append(serviceCompanyConfig.getServiceCompanyCode(mortgageDeduction.getTarget(), DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getCode()));
            shareInfo.append(",").append(m2);
        }
        baoFuRequestParams.setShareInfo(shareInfo.toString());

        //设置协议号
        AccountManager accountManager = accountManagerRepository.findByAccountAndSortAndContractNo(mortgageDeduction.getParam3(), 1, mortgageDeduction.getContractNo());
        baoFuRequestParams.setProtocolNo(accountManager.getBaofuProtocolNo());

        //设置报文发送时间
        baoFuRequestParams.setSendTime(Utility.convertToString(new Date(), "yyyy-MM-dd HH:mm:ss"));

        return baoFuRequestParams;
    }

    /**
     * 将MortgageDeduction转换为GoPayBean  银联白名单代扣
     *
     * @return GoPayBean
     */
    public GoPayBean transToGoPayBean(MortgageDeduction mortgageDeduction) {
        GoPayBean goPayBean = new GoPayBean();

        //商户号
        goPayBean.setMerId(chinaPayConfig.getClassicMerId());
        //订单交易币种
        goPayBean.setCuryId(chinaPayConfig.getCuryId());
        //版本号
        goPayBean.setVersion(chinaPayConfig.getClassicVersion());
        //后台交易接收URL地址
        goPayBean.setBgRetUrl(chinaPayConfig.getClassicBgRetUrl());
        //页面交易接收URL地址
        goPayBean.setPageRetUrl(chinaPayConfig.getClassicPageRetUrl());
        //支付网关号
        goPayBean.setGateId(chinaPayConfig.getClassicGateId());
        //分账类型
        goPayBean.setShareType(chinaPayConfig.getClassicType());
        //设置合同编号
        goPayBean.setContractId(mortgageDeduction.getContractNo());
        //设置客户名称
        goPayBean.setCustomerName(mortgageDeduction.getCustomerName());
        //设置合同编号
        goPayBean.setContractNo(mortgageDeduction.getContractNo());
        //设置服务费的管理公司
        goPayBean.setOrgManagerId(mortgageDeduction.getOrdId());
        //设置还款计划的id
        goPayBean.setRePlanId("");
        goPayBean.setSplitData1(mortgageDeduction.getSplitData1());
        goPayBean.setSplitData2(mortgageDeduction.getSplitData2());
        goPayBean.setBusiId("");
        goPayBean.setOrdId(MerSeq.tickOrder());
        String amount1 = mortgageDeduction.getSplitData1().toString();
        String amount2 = mortgageDeduction.getSplitData2().toString();
        int m1 = 0;
        if (StringUtils.isNotBlank(amount1)) {
            m1 = new BigDecimal(amount1).movePointRight(2).intValue();
        }
        int m2 = 0;
        if (StringUtils.isNotBlank(amount2)) {
            m2 = new BigDecimal(amount2).movePointRight(2).intValue();
        }
        goPayBean.setOrdAmt(m1 + m2 + "");

        //分账信息
        StringBuilder shareData = new StringBuilder();
        shareData.append(chinaPayConfig.getClassicRunTongMemberId()).append("^").append(m1);
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && m2 != 0) {
            shareData.append(";").append(serviceCompanyConfig.getServiceCompanyCode(mortgageDeduction.getTarget(), DeductionChannelEnum.CHINA_PAY_CLASSIC_DEDUCTION.getCode()))
                    .append("^").append(m2).append(";");
        }
        goPayBean.setShareData(shareData.toString());

        //开户行号
        goPayBean.setParam1(mortgageDeduction.getParam1());
        //卡折标志
        goPayBean.setParam2(mortgageDeduction.getParam2());
        //卡号/折号
        goPayBean.setParam3(mortgageDeduction.getParam3());
        //持卡人姓名
        goPayBean.setParam4(mortgageDeduction.getParam4());
        //证件类型
        goPayBean.setParam5(mortgageDeduction.getParam5());
        //证件号
        goPayBean.setParam6(mortgageDeduction.getParam6());
        goPayBean.setParam7("");
        goPayBean.setParam8("");
        goPayBean.setParam9("");
        goPayBean.setParam10("");
        goPayBean.setOrdDesc(DeductionChannelEnum.CHINA_PAY_CLASSIC_DEDUCTION.getOrderDesc());
        goPayBean.setPriv1("");
        goPayBean.setCustomIp("");
        return goPayBean;

    }

    /**
     * 将MortgageDeduction 转换为 RequestParams  宝付代扣
     *
     * @param mortgageDeduction
     * @return 宝付代扣请求参数RequestParams
     */
    public RequestParams transToRequestParams(MortgageDeduction mortgageDeduction) throws UnsupportedEncodingException {

        RequestParams requestParams = new RequestParams();
        requestParams.setVersion(baofuConfig.getClassicVersion());
        requestParams.setTerminalId(baofuConfig.getClassicTerminalId());
        requestParams.setTxnType(baofuConfig.getClassicTxnType());
        requestParams.setTxnSubType(baofuConfig.getClassicTxnSubType());
        requestParams.setMemberId(baofuConfig.getClassicMemberId());
        requestParams.setDataType(baofuConfig.getClassicDataType());
        requestParams.setContractNo(mortgageDeduction.getContractNo());
        requestParams.setBxAmount(mortgageDeduction.getSplitData1());
        requestParams.setFwfAmount(mortgageDeduction.getRsplitData2());


        DataContent dataContent = new DataContent();
        dataContent.setAccNo(mortgageDeduction.getParam3());
        dataContent.setIdCardType("01");
        dataContent.setIdCard(mortgageDeduction.getParam6());
        dataContent.setIdHolder(mortgageDeduction.getCustomerName());
        dataContent.setMobile("");
        dataContent.setValidDate("");
        dataContent.setValidNo("");
        dataContent.setTransId(MerSeq.tickOrder());

        String amount1 = mortgageDeduction.getSplitData1().toString();
        String amount2 = mortgageDeduction.getSplitData2().toString();
        int m1 = 0;
        if (StringUtils.isNotBlank(amount1)) {
            m1 = new BigDecimal(amount1).movePointRight(2).intValue();
        }
        int m2 = 0;
        if (StringUtils.isNotBlank(amount2)) {
            m2 = new BigDecimal(amount2).movePointRight(2).intValue();
        }

        dataContent.setTxnAmt(String.valueOf(m1 + m2));
        dataContent.setTradeDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        dataContent.setAdditionalInfo("");
        dataContent.setReqReserved("");
        dataContent.setTransSerialNo("TSN" + System.currentTimeMillis());


        //设置分账信息 有服务费时才分账
        StringBuilder shareInfo = new StringBuilder();
        shareInfo.append(baofuConfig.getClassicMemberId()).append(",").append(m1);
        if (mortgageDeduction.getSplitData2().doubleValue() > 0) {
            shareInfo.append(";").append(serviceCompanyConfig.getServiceCompanyCode(mortgageDeduction.getTarget(), DeductionChannelEnum.BAO_FU_CLASSIC_DEDUCTION.getCode()))
                    .append(m2);
        }
        dataContent.setShareInfo(shareInfo.toString());
        // 分账手续费从本息账户扣除
        dataContent.setFeeMemberId(baofuConfig.getClassicMemberId());

        dataContent.setTxnSubType(baofuConfig.getClassicTxnSubType());
        dataContent.setBizType(baofuConfig.getClassicBizType());
        dataContent.setTerminalId(baofuConfig.getClassicTerminalId());
        dataContent.setMemberId(baofuConfig.getClassicMemberId());
        dataContent.setPayCode(BankCodeEnum.getCodeById(mortgageDeduction.getParam1()));
        dataContent.setPayCm(baofuConfig.getClassicPayCm());
        dataContent.setNotifyUrl(baofuConfig.getClassicNotifyUrl());

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dataContent);
        String contentData = jsonObject.toString();
        log.info("宝付代扣contentData:{}", contentData);
        String base64str = SecurityUtil.Base64Encode(contentData);
        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, baofuConfig.getClassicPfxFile(), baofuConfig.getClassicPriKey());
        requestParams.setDataContent(data_content);
        requestParams.setContent(dataContent);

        return requestParams;
    }


    /**
     * 将MortgageDeduction转换为银联请求参数类  银联新无卡代扣
     *
     * @param mortgageDeduction
     * @return
     */
    public ChinaPayDelayRequestParams transToChinaPayDelayRequestParams(MortgageDeduction mortgageDeduction) {

        ChinaPayDelayRequestParams chinaPayDelayRequestParams = new ChinaPayDelayRequestParams();

        //设置订单号
        chinaPayDelayRequestParams.setMerOrderNo(MerSeq.tickOrder());

        //设置订单金额
        String amount1 = mortgageDeduction.getSplitData1().toString();
        String amount2 = mortgageDeduction.getSplitData2().toString();
        int m1 = 0;
        if (StringUtils.isNotBlank(amount1)) {
            m1 = new BigDecimal(amount1).movePointRight(2).intValue();
        }
        int m2 = 0;
        if (StringUtils.isNotBlank(amount2)) {
            m2 = new BigDecimal(amount2).movePointRight(2).intValue();
        }
        chinaPayDelayRequestParams.setOrderAmt(String.valueOf(m1 + m2));

        //设置分账三个相关参数
        //设置分账方式:按金额分账
        chinaPayDelayRequestParams.setSplitMethod("0");
        //设置分账类型:实时分账
        chinaPayDelayRequestParams.setSplitType("0001");
        StringBuilder merSplitMsg = new StringBuilder(chinaPayConfig.getExpressRealTimeRunTongMemberId());
        merSplitMsg.append("^").append(m1);
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && m2 != 0) {
            //设置分账信息
            merSplitMsg.append(";").append(serviceCompanyConfig.getServiceCompanyCode(mortgageDeduction.getTarget(), DeductionChannelEnum.CHINA_PAY_EXPRESS_DELAY.getCode())).append("^").append(m2).append(";");
        }

        chinaPayDelayRequestParams.setMerSplitMsg(merSplitMsg.toString());

        //设置卡号
        chinaPayDelayRequestParams.setCardNo(mortgageDeduction.getParam3());
        //设置证件类型
        chinaPayDelayRequestParams.setCertType(ChinaPayCertTypeEnum.CERT_TYPE_01.getCode());
        //设置证件号码
        chinaPayDelayRequestParams.setCertNo(mortgageDeduction.getParam6());
        //设置账户名
        chinaPayDelayRequestParams.setAccName(mortgageDeduction.getParam4());
        return chinaPayDelayRequestParams;
    }


    private String handleBankName(String bankName) {
        List<SysDict> openBankList = sysDictRepository.findByDicType(DictionaryType.MERID_SOURCE);
        for (SysDict sysDict : openBankList) {
            if (StringUtils.equals(bankName, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }

        return "";
    }

    private String handleCertificateType(String certificateType) {
        List<SysDict> cTypeLst = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
        for (SysDict sysDict : cTypeLst) {
            if (StringUtils.equals(certificateType, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }

        return "";
    }


}
