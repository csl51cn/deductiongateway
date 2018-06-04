package org.starlightfinancial.deductiongateway.utility;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BaoFuRequestParams;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;

import java.math.BigDecimal;
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

    @Autowired
    private ChinaPayConfig chinaPayConfig;

    @Autowired
    private BaofuConfig baofuConfig;

    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    private SysDictRepository sysDictRepository;

    /**
     * 将MortgageDeduction转换为银联请求参数类
     *
     * @param mortgageDeduction
     * @return
     */
    public UnionPayRequestParams transToUnionPayRequestParams(MortgageDeduction mortgageDeduction) {
        UnionPayRequestParams unionPayRequestParams = new UnionPayRequestParams();
        //设置订单号
        unionPayRequestParams.setMerOrderNo(MerSeq.tickOrder());

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
        unionPayRequestParams.setOrderAmt(String.valueOf(m1 + m2));

        //设置分账三个相关参数
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && m2 != 0) {

            //设置分账信息
            StringBuilder merSplitMsg = new StringBuilder(chinaPayConfig.getExpressRealTimeMemberId());
            merSplitMsg.append("^").append(m1);
            merSplitMsg.append(";").append(mortgageDeduction.getTarget()).append("^").append(m2).append(";");
            unionPayRequestParams.setMerSplitMsg(merSplitMsg.toString());
            //设置分账方式:按金额分账
            unionPayRequestParams.setSplitMethod("0");
            //设置分账类型:实时分账
            unionPayRequestParams.setSplitType("0001");

        }
        //设置协议号
        AccountManager accountManager = accountManagerRepository.findByAccountAndSortAndContractNo(mortgageDeduction.getParam3(), 1, mortgageDeduction.getContractNo());
        unionPayRequestParams.setProtocolNo(accountManager.getUnionpayProtocolNo());

        return unionPayRequestParams;
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
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && "铠岳".equals(mortgageDeduction.getTarget().trim())) {
            mortgageDeduction.setTarget(chinaPayConfig.getClassicKaiYueMemberId());
        } else {
            mortgageDeduction.setTarget(chinaPayConfig.getClassicRunKunMemberId());
        }
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

        //设置分账信息 有服务费时才分账
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && m2 != 0) {
            if (StringUtils.equals(chinaPayConfig.getClassicKaiYueMemberId(), mortgageDeduction.getTarget())) {
                baoFuRequestParams.setShareInfo(baofuConfig.getProtocolMemberId() + "," + m1
                        + ";" + baofuConfig.getProtocolKaiYueMemberId() + "," + m2);
            } else if (StringUtils.equals(chinaPayConfig.getClassicRunKunMemberId(), mortgageDeduction.getTarget())) {
                baoFuRequestParams.setShareInfo(baofuConfig.getProtocolMemberId() + "," + m1
                        + ";" + baofuConfig.getProtocolRunKunMemberId() + "," + m2);
            }
        } else {
            //无服务费的情况
            baoFuRequestParams.setShareInfo(baofuConfig.getProtocolMemberId() + "," + baoFuRequestParams.getTxnAmt());
        }

        //设置协议号
        AccountManager accountManager = accountManagerRepository.findByAccountAndSortAndContractNo(mortgageDeduction.getParam3(), 1, mortgageDeduction.getContractNo());
        baoFuRequestParams.setProtocolNo(accountManager.getBaofuProtocolNo());

        //设置报文发送时间
        baoFuRequestParams.setSendTime(Utility.convertToString(new Date(), "yyyy-MM-dd HH:mm:ss"));

        return baoFuRequestParams;
    }

    /**
     * 将MortgageDeduction转换为GoPayBean
     *
     * @return GoPayBean
     */
    public GoPayBean transToGoPayBean(MortgageDeduction mortgageDeduction) {
        GoPayBean goPayBean = new GoPayBean();
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
        goPayBean.setOrdDesc("银联");
        //设置分账信息
        String shareData = chinaPayConfig.getClassicRunTongMemberId() + m1;
        if (StringUtils.isNotBlank(mortgageDeduction.getTarget()) && m2 != 0) {
            shareData += ";" + mortgageDeduction.getTarget() + "^" + m2 + ";";
        }
        goPayBean.setShareData(shareData);
        goPayBean.setPriv1("");
        goPayBean.setCustomIp("");
        return goPayBean;

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
