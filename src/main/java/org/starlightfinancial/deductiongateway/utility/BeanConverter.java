package org.starlightfinancial.deductiongateway.utility;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.domain.remote.RepaymentInfo;
import org.starlightfinancial.deductiongateway.enums.*;
import org.starlightfinancial.deductiongateway.service.CacheService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    private static final List<DeductionChannelEnum> CHINA_PAY;
    private static final List<DeductionChannelEnum> BAO_FU;

    static {
        CHINA_PAY = new ArrayList<>();
        CHINA_PAY.add(DeductionChannelEnum.CHINA_PAY_CLASSIC_DEDUCTION);
        CHINA_PAY.add(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME);
        CHINA_PAY.add(DeductionChannelEnum.CHINA_PAY_EXPRESS_DELAY);

        BAO_FU = new ArrayList<>();
        BAO_FU.add(DeductionChannelEnum.BAO_FU_CLASSIC_DEDUCTION);
        BAO_FU.add(DeductionChannelEnum.BAO_FU_PROTOCOL_PAY);
    }

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
        if (StringUtils.isNotBlank(accountManager.getUnionpayProtocolNo())) {
            //设置协议号
            chinaPayRealTimeRequestParams.setProtocolNo(accountManager.getUnionpayProtocolNo());
        } else {
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
        //设置银行名称,此时为中文,本方法后面会处理
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
        //证件号:转换为大写,一般情况白名单中的身份证号末位的x是大写,自动代扣发起时也需要转换为大写,特殊情况手动处理
        mortgageDeduction.setParam6(autoBatchDeduction.getCertificateNo().toUpperCase());
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
        //设置是否上传自动入账表格
        mortgageDeduction.setIsUploaded(String.valueOf(0));
        return mortgageDeduction;
    }


    /**
     * 将MortgageDeduction转换为BaoFuRequestParams
     *
     * @param mortgageDeduction 代扣数据
     * @return 宝付交易参数
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
        goPayBean.setOrgManagerId(serviceCompanyConfig.getServiceCompanyCode(mortgageDeduction.getTarget(), DeductionChannelEnum.CHINA_PAY_CLASSIC_DEDUCTION.getCode()));
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
     * @param mortgageDeduction 代扣信息
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
                    .append(",").append(m2);
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

        AccountManager accountManager = accountManagerRepository.findByContractNoAndAccountAndAccountNameAndCertificateNo(mortgageDeduction.getContractNo(), dataContent.getAccNo(), dataContent.getIdHolder(), dataContent.getIdCard());
        if (Objects.nonNull(accountManager)){
            //手机号
            dataContent.setMobile(accountManager.getMobile());
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dataContent);
        String contentData = jsonObject.toString();
        log.info("宝付代扣contentData:{}", contentData);
        String base64str = SecurityUtil.Base64Encode(contentData);
        String dataContentEncrypted = RsaCodingUtil.encryptByPriPfxFile(base64str, baofuConfig.getClassicPfxFile(), baofuConfig.getClassicPriKey());
        requestParams.setDataContent(dataContentEncrypted);
        requestParams.setContent(dataContent);

        return requestParams;
    }


    /**
     * 将MortgageDeduction转换为银联请求参数类  银联新无卡代扣
     *
     * @param mortgageDeduction 代扣信息
     * @return 银联请求参数类
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

    /**
     * 将代扣数据转换为自动入账excel表格行元素对应实体类
     *
     * @param mortgageDeduction 代扣数据
     * @return 自动入账excel表格行元素对应实体类
     */
    public AutoAccountingExcelRow transToAutoAccountingExcelRow(MortgageDeduction mortgageDeduction) {
        AutoAccountingExcelRow autoAccountingExcelRow = new AutoAccountingExcelRow();
        //客户名称
        autoAccountingExcelRow.setCustomerName(mortgageDeduction.getCustomerName());
        //合同编号
        autoAccountingExcelRow.setContractNo(mortgageDeduction.getContractNo());
        //还款日期
        autoAccountingExcelRow.setRepaymentTermDate(Utility.convertToString(mortgageDeduction.getPayTime(), "yyyy/MM/dd"));
        //入账日期
        autoAccountingExcelRow.setAccountingDate(autoAccountingExcelRow.getRepaymentTermDate());
        //本息
        autoAccountingExcelRow.setPrincipalAndInterest(mortgageDeduction.getSplitData1());
        //服务费
        autoAccountingExcelRow.setServiceFee(mortgageDeduction.getSplitData2());
        //调查评估费设置为0
        autoAccountingExcelRow.setEvaluationFee(BigDecimal.ZERO);
        //服务费入账公司
        //处理服务费管理公司:最初数据库保存的是ChinaPayClassicDeduction 对应的服务费公司商户号(数字), 对接银联新无卡重构代码后保存的是公司名称(汉字)
        if (mortgageDeduction.getTarget() != null && StringUtils.isNumeric(mortgageDeduction.getTarget())) {
            //为数字的时候需要转换
            if (chinaPayConfig.getClassicKaiYueMemberId().equals(mortgageDeduction.getTarget().trim())) {
                autoAccountingExcelRow.setServiceFeeChargeCompany("铠岳");
            } else if (chinaPayConfig.getClassicRunKunMemberId().equals(mortgageDeduction.getTarget().trim())) {
                autoAccountingExcelRow.setServiceFeeChargeCompany("润坤");
            }
        } else {
            //为汉字的情况,直接设置
            autoAccountingExcelRow.setServiceFeeChargeCompany(mortgageDeduction.getTarget());
        }
        //是否代扣
        autoAccountingExcelRow.setIsDeduction("是");
        //还款方式
        String repaymentMethod = DeductionChannelEnum.getDescByCode(mortgageDeduction.getChannel());
        if (repaymentMethod == null) {
            repaymentMethod = DeductionChannelEnum.getOrderDescByCode(mortgageDeduction.getChannel()) + "代扣";
        }
        autoAccountingExcelRow.setRepaymentMethod(repaymentMethod);

        return autoAccountingExcelRow;
    }

    /**
     * 将非代扣还款数据转换为自动入账excel表格行元素对应实体类
     *
     * @param nonDeductionRepaymentInfo 非代扣还款数据
     * @return 自动入账excel表格行元素对应实体类
     */
    public AutoAccountingExcelRow transToAutoAccountingExcelRow(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        AutoAccountingExcelRow autoAccountingExcelRow = new AutoAccountingExcelRow();
        CacheService.refresh();
        BusinessTransaction businessTransaction = CacheService.getBusinessTransactionCacheMap().get(nonDeductionRepaymentInfo.getContractNo());
        //客户名称
        autoAccountingExcelRow.setCustomerName(businessTransaction.getSubject());
        //合同编号
        autoAccountingExcelRow.setContractNo(nonDeductionRepaymentInfo.getContractNo());
        //还款日期
        autoAccountingExcelRow.setRepaymentTermDate(Utility.convertToString(nonDeductionRepaymentInfo.getRepaymentTermDate(), "yyyy/MM/dd"));
        //入账日期
        autoAccountingExcelRow.setAccountingDate(Utility.convertToString(nonDeductionRepaymentInfo.getAccountingDate(), "yyyy/MM/dd"));

        if (RepaymentTypeEnum.PRINCIPAL_AND_INTEREST.getDesc().equals(nonDeductionRepaymentInfo.getRepaymentType())) {
            //本息
            autoAccountingExcelRow.setPrincipalAndInterest(nonDeductionRepaymentInfo.getRepaymentAmount());
            autoAccountingExcelRow.setServiceFee(BigDecimal.ZERO);
            autoAccountingExcelRow.setEvaluationFee(BigDecimal.ZERO);
        }

        if (RepaymentTypeEnum.SERVICE_FEE.getDesc().equals(nonDeductionRepaymentInfo.getRepaymentType())) {
            //服务费
            autoAccountingExcelRow.setServiceFee(nonDeductionRepaymentInfo.getRepaymentAmount());
            autoAccountingExcelRow.setServiceFeeChargeCompany(nonDeductionRepaymentInfo.getChargeCompany());

            autoAccountingExcelRow.setPrincipalAndInterest(BigDecimal.ZERO);
            autoAccountingExcelRow.setEvaluationFee(BigDecimal.ZERO);
        }

        if (RepaymentTypeEnum.EVALUATION_FEE.getDesc().equals(nonDeductionRepaymentInfo.getRepaymentType())) {
            //调查评估费
            autoAccountingExcelRow.setEvaluationFee(nonDeductionRepaymentInfo.getRepaymentAmount());
            autoAccountingExcelRow.setEvaluationFeeChargeCompany(nonDeductionRepaymentInfo.getChargeCompany());

            autoAccountingExcelRow.setPrincipalAndInterest(BigDecimal.ZERO);
            autoAccountingExcelRow.setServiceFee(BigDecimal.ZERO);

        }
        //是否代扣
        autoAccountingExcelRow.setIsDeduction("否");
        //还款方式
        StringBuilder repaymentMethod = new StringBuilder(nonDeductionRepaymentInfo.getRepaymentMethod());
        if (StringUtils.isNotBlank(nonDeductionRepaymentInfo.getBankName())) {
            repaymentMethod.append("-").append(nonDeductionRepaymentInfo.getBankName());
        }
        autoAccountingExcelRow.setRepaymentMethod(repaymentMethod.toString());
        autoAccountingExcelRow.setReason(nonDeductionRepaymentInfo.getRemark());
        return autoAccountingExcelRow;


    }

    /**
     * 处理银行参数
     *
     * @param bankName 银行名称
     * @return 银行代码
     */
    private String handleBankName(String bankName) {
        List<SysDict> openBankList = sysDictRepository.findByDicType(DictionaryType.MERID_SOURCE);
        for (SysDict sysDict : openBankList) {
            if (StringUtils.equals(bankName, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }

        return "";
    }

    /**
     * 处理证件类型
     *
     * @param certificateType 证件类型汉字描述
     * @return 证件类型代码
     */
    private String handleCertificateType(String certificateType) {
        List<SysDict> cTypeLst = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
        for (SysDict sysDict : cTypeLst) {
            if (StringUtils.equals(certificateType, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }

        return "";
    }

    /**
     * 将代扣还款信息MortgageDeduction转换为还款信息RepaymentInfo
     *
     * @param mortgageDeduction 代扣还款信息
     * @return 还款信息RepaymentInfo
     */
    public List<RepaymentInfo> transToRepaymentInfo(MortgageDeduction mortgageDeduction) {
        ArrayList<RepaymentInfo> repaymentInfos = new ArrayList<>();
        RepaymentInfo principalAndInterestRepaymentInfo = new RepaymentInfo();
        BusinessTransaction businessTransaction = CacheService.getBusinessTransactionCacheMap().get(mortgageDeduction.getContractNo());
        if (Objects.nonNull(businessTransaction)) {
            //设置dateId
            principalAndInterestRepaymentInfo.setDateId(businessTransaction.getDateId());
            //设置客户名称
            principalAndInterestRepaymentInfo.setCustomerName(businessTransaction.getSubject());
        } else {
            //设置客户名称
            principalAndInterestRepaymentInfo.setCustomerName("客户");
        }

        //设置合同号
        principalAndInterestRepaymentInfo.setContractNo(mortgageDeduction.getContractNo());
        //设置还款日期
        principalAndInterestRepaymentInfo.setRepaymentTermDate(Utility.convertToDate(mortgageDeduction.getPayTime().toString(), "yyyy-MM-dd"));

        //设置还款方式
        principalAndInterestRepaymentInfo.setRepaymentMethod(DeductionChannelEnum.getOrderDescByCode(mortgageDeduction.getChannel()) + "代扣");
        //设置创建时间
        principalAndInterestRepaymentInfo.setGmtCreate(new Date());
        //设置创建人
        principalAndInterestRepaymentInfo.setCreateId(14);
        //设置修改时间
        principalAndInterestRepaymentInfo.setGmtModified(principalAndInterestRepaymentInfo.getGmtCreate());
        //设置最后一个修改人id
        principalAndInterestRepaymentInfo.setModifiedId(principalAndInterestRepaymentInfo.getCreateId());
        //设置是否是代扣
        principalAndInterestRepaymentInfo.setIsDeduction(ConstantsEnum.SUCCESS.getCode());
        //设置原始id
        principalAndInterestRepaymentInfo.setOriginalId(Long.valueOf(mortgageDeduction.getId()));
        //设置入账公司
        principalAndInterestRepaymentInfo.setChargeCompany(ChargeCompanyEnum.RUN_TONG.getValue());
        //设置本息还款金额
        principalAndInterestRepaymentInfo.setRepaymentAmount(mortgageDeduction.getSplitData1());
        //设置还款类别
        principalAndInterestRepaymentInfo.setRepaymentType(String.valueOf(RepaymentTypeEnum.PRINCIPAL_AND_INTEREST.getCode()));
        //设置是否是从其他记录拆分出来的:否-0
        principalAndInterestRepaymentInfo.setCreateFromAnother(ConstantsEnum.FAIL.getCode());
        //判断是否是银联代扣
        boolean isChinaPay = CHINA_PAY.stream().anyMatch(deductionChannelEnum -> StringUtils.equals(deductionChannelEnum.getCode(), mortgageDeduction.getChannel()));
        if (mortgageDeduction.getSplitData2().compareTo(BigDecimal.ZERO) > 0) {
            //如果存在服务费,分两种情况处理:1.银联两个入账账户按比例扣除手续费;2.宝付是润通账户扣除手续费
            //创建服务费还款记录
            RepaymentInfo serviceFeeRepaymentInfo = new RepaymentInfo();
            BeanUtils.copyProperties(principalAndInterestRepaymentInfo, serviceFeeRepaymentInfo);
            //设置服务费金额
            serviceFeeRepaymentInfo.setRepaymentAmount(mortgageDeduction.getSplitData2());
            //设置服务费入账公司
            serviceFeeRepaymentInfo.setChargeCompany(mortgageDeduction.getTarget());
            //设置服务费入账银行:铠岳和润坤的银联,宝付渠道各自都是使用的相同银行进行商户开户的
            if (StringUtils.equals(serviceFeeRepaymentInfo.getChargeCompany(), ChargeCompanyEnum.KAI_YUE.getValue())) {
                //铠岳
                serviceFeeRepaymentInfo.setBankName(AccountBankEnum.KAI_YUE_CCB_0334.getCode());
            } else {
                //润坤
                serviceFeeRepaymentInfo.setBankName(AccountBankEnum.RUN_KUN_CMBC_0702.getCode());
            }
            //设置还款类别
            serviceFeeRepaymentInfo.setRepaymentType(String.valueOf(RepaymentTypeEnum.SERVICE_FEE.getCode()));

            if (isChinaPay) {
                //设置本息手续费
                BigDecimal totalAmount = mortgageDeduction.getSplitData1().add(mortgageDeduction.getSplitData2());
                //本息占总金额中的比例,保留五位小数,四舍五入
                BigDecimal principalAndInterestProportion = mortgageDeduction.getSplitData1().divide(totalAmount, 5, BigDecimal.ROUND_HALF_UP);
                //本息入账方手续费,保留两位小数,四舍五入
                BigDecimal principalAndInterestHandlingCharge = mortgageDeduction.getHandlingCharge().multiply(principalAndInterestProportion).setScale(2, BigDecimal.ROUND_HALF_UP);
                principalAndInterestRepaymentInfo.setHandlingCharge(principalAndInterestHandlingCharge);
                //设置本息入账银行账户
                principalAndInterestRepaymentInfo.setBankName(AccountBankEnum.RUN_TONG_CMBC_0356.getCode());
                //设置服务费入账公司手续费
                serviceFeeRepaymentInfo.setHandlingCharge(mortgageDeduction.getHandlingCharge().subtract(principalAndInterestHandlingCharge).setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                //宝付只在润通账户扣除手续费
                principalAndInterestRepaymentInfo.setHandlingCharge(mortgageDeduction.getHandlingCharge());
                //设置本息入账银行账户
                principalAndInterestRepaymentInfo.setBankName(AccountBankEnum.RUN_TONG_CMBC_0356.getCode());
                serviceFeeRepaymentInfo.setHandlingCharge(BigDecimal.ZERO);
            }
            repaymentInfos.add(serviceFeeRepaymentInfo);

        } else {
            //不存在服务费,直接设置手续费
            principalAndInterestRepaymentInfo.setHandlingCharge(mortgageDeduction.getHandlingCharge());
            //设置本息入账银行账户
            if (isChinaPay) {
                //银联
                principalAndInterestRepaymentInfo.setBankName(AccountBankEnum.RUN_TONG_CMBC_0356.getCode());
            } else {
                //宝付
                principalAndInterestRepaymentInfo.setBankName(AccountBankEnum.RUN_TONG_CMBC_0356.getCode());
            }

        }
        if (principalAndInterestRepaymentInfo.getRepaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
            //如果本息还款大于0
            repaymentInfos.add(principalAndInterestRepaymentInfo);
        }

        return repaymentInfos;
    }

    /**
     * 非代扣还款数据NonDeductionRepaymentInfo转换为还款数据RepaymentInfo
     *
     * @param nonDeductionRepaymentInfo 非代扣还款数据 NonDeductionRepaymentInfo
     * @return 返回还款数据
     */
    public RepaymentInfo transToRepaymentInfo(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        RepaymentInfo repaymentInfo = new RepaymentInfo();
        BusinessTransaction businessTransaction = CacheService.getBusinessTransactionCacheMap().get(nonDeductionRepaymentInfo.getContractNo());
        //设置入账公司
        repaymentInfo.setChargeCompany(nonDeductionRepaymentInfo.getChargeCompany());
        //设置dateId
        repaymentInfo.setDateId(nonDeductionRepaymentInfo.getDateId());
        //设置合同号
        repaymentInfo.setContractNo(nonDeductionRepaymentInfo.getContractNo());
        //设置还款日期
        repaymentInfo.setRepaymentTermDate(nonDeductionRepaymentInfo.getRepaymentTermDate());
        //设置客户名称
        if (businessTransaction != null) {
            //如果匹配到业务信息的
            repaymentInfo.setCustomerName(businessTransaction.getSubject());
        } else {
            //如果没有匹配到业务信息
            repaymentInfo.setCustomerName("客户");
        }
        //设置还款方式
        repaymentInfo.setRepaymentMethod(nonDeductionRepaymentInfo.getRepaymentMethod());
        //设置还款类别
        repaymentInfo.setRepaymentType(String.valueOf(RepaymentTypeEnum.getCodeByDesc(nonDeductionRepaymentInfo.getRepaymentType())));
        //设置入账银行:现金不用设置,银行转账,收银宝,POS刷卡需要设置
        if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentMethod(), NonDeductionRepaymentMethodEnum.BANK_TRANSFER.getValue())
                || StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentMethod(),
                NonDeductionRepaymentMethodEnum.POS.getValue())) {
            //银行转账或者POS机刷卡
            repaymentInfo.setBankName(AccountBankEnum.getCodeByValue(nonDeductionRepaymentInfo.getBankName()));
        } else if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentMethod(), NonDeductionRepaymentMethodEnum.THIRD_PARTY_PAYMENT.getValue())) {
            //收银宝,需要根据入账公司来确定入账银行
            if (StringUtils.equals(nonDeductionRepaymentInfo.getChargeCompany(), ChargeCompanyEnum.RUN_TONG.getValue())) {
                //润通
                repaymentInfo.setBankName(AccountBankEnum.RUN_TONG_CMBC_0356.getCode());
            }

            if (StringUtils.equals(nonDeductionRepaymentInfo.getChargeCompany(), ChargeCompanyEnum.KAI_YUE.getValue())) {
                //铠岳
                repaymentInfo.setBankName(AccountBankEnum.KAI_YUE_CCB_0334.getCode());
            }

            if (StringUtils.equals(nonDeductionRepaymentInfo.getChargeCompany(), ChargeCompanyEnum.RUN_KUN.getValue())) {
                //润坤
                repaymentInfo.setBankName(AccountBankEnum.RUN_KUN_CMBC_0702.getCode());
            }
            if (StringUtils.equals(nonDeductionRepaymentInfo.getChargeCompany(), ChargeCompanyEnum.THIRD_PARTY_CONSULT.getValue())) {
                //第三方咨询公司
                repaymentInfo.setBankName(AccountBankEnum.THIRD_PARTY_CONSULT_BCQ_6559.getCode());
            }
        }

        //设置还款金额
        repaymentInfo.setRepaymentAmount(nonDeductionRepaymentInfo.getRepaymentAmount());
        //设置手续费
        if (Objects.isNull(nonDeductionRepaymentInfo.getHandlingCharge())) {
            repaymentInfo.setHandlingCharge(BigDecimal.ZERO);
        } else {
            repaymentInfo.setHandlingCharge(nonDeductionRepaymentInfo.getHandlingCharge());
        }
        //设置是否是被拆分出来生成的的:如果originalId不为空,说明当前记录是从其他记录拆分出来的.如果为空,说明不是
        if(Objects.nonNull(nonDeductionRepaymentInfo.getOriginalId())){
            repaymentInfo.setCreateFromAnother(ConstantsEnum.SUCCESS.getCode());
        }else{
            repaymentInfo.setCreateFromAnother(ConstantsEnum.FAIL.getCode());
        }
        //设置创建时间
        repaymentInfo.setGmtCreate(new Date());
        //设置创建人
        repaymentInfo.setCreateId(14);
        //设置修改时间
        repaymentInfo.setGmtModified(repaymentInfo.getGmtCreate());
        //设置最后一个修改人id
        repaymentInfo.setModifiedId(repaymentInfo.getCreateId());
        //设置是否是代扣
        repaymentInfo.setIsDeduction(ConstantsEnum.FAIL.getCode());
        //设置原始id
        repaymentInfo.setOriginalId(nonDeductionRepaymentInfo.getId());
        return repaymentInfo;
    }

    public static List<LoanIssueBasicInfoExcelRow> transToLoanIssueBasicInfoExcelRowList(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        ArrayList<LoanIssueBasicInfoExcelRow> loanIssueBasicInfoExcelRows = new ArrayList<>();
        for (LoanIssueBasicInfo loanIssueBasicInfo : loanIssueBasicInfos) {
            List<LoanIssue> loanIssues = loanIssueBasicInfo.getLoanIssues();
            for (LoanIssue loanIssue : loanIssues) {
                LoanIssueBasicInfoExcelRow loanIssueBasicInfoExcelRow = new LoanIssueBasicInfoExcelRow();
                loanIssueBasicInfoExcelRow.setBusinessNo(loanIssueBasicInfo.getBusinessNo());
                loanIssueBasicInfoExcelRow.setContractNo(loanIssueBasicInfo.getContractNo());
                loanIssueBasicInfoExcelRow.setToAccountName(loanIssueBasicInfo.getToAccountName());
                loanIssueBasicInfoExcelRow.setToAccountNo(loanIssueBasicInfo.getToAccountNo());
                loanIssueBasicInfoExcelRow.setIssueAmount(loanIssueBasicInfo.getIssueAmount());
                loanIssueBasicInfoExcelRow.setToBankNameId(loanIssueBasicInfo.getToBankNameId());
                loanIssueBasicInfoExcelRow.setToBankProvince(loanIssueBasicInfo.getToBankProvince());
                loanIssueBasicInfoExcelRow.setToBankCity(loanIssueBasicInfo.getToBankCity());
                loanIssueBasicInfoExcelRow.setToBankBranch(loanIssueBasicInfo.getToBankBranch());
                loanIssueBasicInfoExcelRow.setIdentityNo(loanIssueBasicInfo.getIdentityNo());
                loanIssueBasicInfoExcelRow.setMobileNo(loanIssueBasicInfo.getMobileNo());
                loanIssueBasicInfoExcelRow.setChannel(loanIssueBasicInfo.getChannel());
                loanIssueBasicInfoExcelRow.setTransactionNo(loanIssue.getTransactionNo());
                loanIssueBasicInfoExcelRow.setTransactionStatus(loanIssue.getTransactionStatus());
                loanIssueBasicInfoExcelRow.setTransactionStartTime(loanIssue.getTransactionStartTime());
                loanIssueBasicInfoExcelRow.setTransactionEndTime(loanIssue.getTransactionEndTime());
                loanIssueBasicInfoExcelRow.setGmtCreate(loanIssue.getGmtCreate());
                loanIssueBasicInfoExcelRows.add(loanIssueBasicInfoExcelRow);
            }
        }
        return loanIssueBasicInfoExcelRows;


    }


}
