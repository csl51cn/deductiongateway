package org.starlightfinancial.deductiongateway.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayClearNetConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.BaoFuRequestParams;
import org.starlightfinancial.deductiongateway.baofu.domain.RequestParams;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.domain.local.ErrorCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.enums.RsbCodeEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategyContext;
import org.starlightfinancial.deductiongateway.utility.*;
import org.starlightfinancial.rpc.hessian.entity.common.RequestResult;
import org.starlightfinancial.rpc.hessian.entity.cpcn.request.Tx2011Req;
import org.starlightfinancial.rpc.hessian.entity.cpcn.response.Tx2011Res;
import org.starlightfinancial.rpc.hessian.service.cpcn.BaseService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sili.chen on 2018/1/3
 */
@Component
public class Delivery extends Decorator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Delivery.class);
    @Value("${batch.route.use}")
    private String router;

    @Autowired
    HttpClientUtil httpClientUtil;

    @Autowired
    ChinaPayConfig chinaPayConfig;

    @Autowired
    BaofuConfig baofuConfig;

    @Autowired
    BeanConverter beanConverter;

    @Autowired
    private OperationStrategyContext operationStrategyContext;

    @Autowired
    private ChinaPayClearNetConfig chinaPayClearNetConfig;


    List<MortgageDeduction> result;

    @Override
    public void doRoute() throws Exception {
        result = new ArrayList<>();
        super.doRoute();
        delivery();
    }

    private void delivery() {
        List<MortgageDeduction> list = ((Assembler) this.route).getResult();
        if ("UNIONPAY".equals(router)) {
            deliveryChinaPayClearNetClassic(list);
        } else if ("BAOFU".equals(router)) {
            deliveryBaoFu(list);
        }

    }


//    private void deliveryUnionPayDelay(List<MortgageDeduction> list) {
//        for (MortgageDeduction mortgageDeduction : list) {
//
//            ChinaPayDelayRequestParams chinaPayDelayRequestParams = beanConverter.transToChinaPayDelayRequestParams(mortgageDeduction);
//            mortgageDeduction.setOrdId(chinaPayDelayRequestParams.getMerOrderNo());
//            mortgageDeduction.setMerId(chinaPayConfig.getExpressRealTimeMemberId());
//            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
//            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getOrderDesc());
//            mortgageDeduction.setPlanNo(0);
//            //type为0表示已发起过代扣，type为1时未发起过代扣
//            mortgageDeduction.setType("0");
//            mortgageDeduction.setPayTime(new Date());
//            mortgageDeduction.setSplitType(chinaPayDelayRequestParams.getSplitType());
//            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getCode());
//
//            // 应对httpClientUtil返回抛异常的情况,将订单号保存,以保证我方数据库记录和银联的记录一致,方便排查错误
//            try {
//                Map map = httpClientUtil.send(chinaPayConfig.getExpressDelayUrl(), chinaPayDelayRequestParams.transToNvpList());
//                String returnData = (String) map.get("returnData");
//                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
//                String errorCodeDesc = ChinaPayReturnCodeEnum.getValueByCode(jsonObject.getString("error_code"));
//                mortgageDeduction.setErrorResult(StringUtils.isEmpty(errorCodeDesc) ? jsonObject.getString("reason") : errorCodeDesc);
//                mortgageDeduction.setResult(jsonObject.getString("error_code"));
//                //返回0014表示数据接收成功,如果不为0014可以交易设置为失败
//                if (!StringUtils.equals(jsonObject.getString("error_code"), "0014")) {
//                    mortgageDeduction.setIssuccess("0");
//                }
//                result.add(mortgageDeduction);
//            } catch (Exception e) {
//                e.printStackTrace();
//                result.add(mortgageDeduction);
//            }
//        }
//    }

    /**
     * 银联白名单代扣
     *
     * @param list 待代扣的MortgageDeduction数据
     */
    private void deliveryUnionPay(List<MortgageDeduction> list) {
        for (MortgageDeduction mortgageDeduction : list) {
            GoPayBean goPayBean = beanConverter.transToGoPayBean(mortgageDeduction);
            mortgageDeduction.setOrdId(goPayBean.getOrdId());
            mortgageDeduction.setMerId(goPayBean.getMerId());
            mortgageDeduction.setCuryId(goPayBean.getCuryId());
            mortgageDeduction.setOrderDesc(goPayBean.getOrdDesc());
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            mortgageDeduction.setPayTime(new Date());
            //设置渠道信息
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_CLASSIC_DEDUCTION.getCode());
            String chkValue = UnionPayUtil.sign(goPayBean.getMerId(), goPayBean.createStringBuffer(), chinaPayConfig.getClassicPfxFile());
            if (StringUtils.isEmpty(chkValue) || chkValue.length() != 256) {
                LOGGER.debug("银联报文签名异常,订单号:{},合同号:{}", mortgageDeduction.getOrdId(), mortgageDeduction.getContractNo());
                mortgageDeduction.setErrorResult("银联报文签名异常");
                result.add(mortgageDeduction);
                continue;
            }
            goPayBean.setChkValue(chkValue);

            try {
                Map map = HttpClientUtil.send(chinaPayConfig.getClassicUrl(), goPayBean.aggregationToList());
                String returnData = (String) map.get("returnData");
                String payStat = returnData.substring(returnData.indexOf("PayStat") + 16, returnData.indexOf("PayStat") + 20);
                mortgageDeduction.setResult(payStat);
                if (StringUtils.equals(Constant.SUCCESS, payStat)) {
                    mortgageDeduction.setIssuccess("1");
                    //计算手续费
                    operationStrategyContext.calculateHandlingCharge(mortgageDeduction);
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
                mortgageDeduction.setErrorResult(ErrorCodeEnum.getValueByCode(payStat));
                result.add(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                //保存订单号
                result.add(mortgageDeduction);
            }
        }
    }

    /**
     * 宝付协议支付
     *
     * @param list 待代扣的MortgageDeduction数据
     */
    private void deliveryBaoFu(List<MortgageDeduction> list) {
        for (MortgageDeduction mortgageDeduction : list) {
            BaoFuRequestParams baoFuRequestParams = beanConverter.transToBaoFuRequestParams(mortgageDeduction);

            mortgageDeduction.setMerId(baofuConfig.getProtocolMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setPayTime(Utility.convertToDate(baoFuRequestParams.getSendTime(), "yyyy-MM-dd HH:mm:ss"));
            mortgageDeduction.setSplitType("");
            mortgageDeduction.setChannel(DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getCode());
            try {
                Map map = HttpClientUtil.send(baofuConfig.getProtocolUrl(), baoFuRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                String errorCodeDesc = BFErrorCodeEnum.getValueByCode(jsonObject.getString("error_code"));
                mortgageDeduction.setErrorResult(StringUtils.isEmpty(errorCodeDesc) ? jsonObject.getString("reason") : errorCodeDesc);
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                result.add(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                result.add(mortgageDeduction);
            }
        }
    }

    /**
     * 宝付代扣
     *
     * @param list 待代扣的MortgageDeduction数据
     */
    private void deliveryBaoFuClassic(List<MortgageDeduction> list) {
        for (MortgageDeduction mortgageDeduction : list) {

            RequestParams requestParams = null;
            try {
                requestParams = beanConverter.transToRequestParams(mortgageDeduction);
            } catch (UnsupportedEncodingException e) {
                LOGGER.debug("宝付代扣:代扣基本数据转换为请求宝付参数异常,合同号:[{}]", e, mortgageDeduction.getContractNo());
                continue;
            }
            mortgageDeduction.setOrdId(requestParams.getContent().getTransId());
            mortgageDeduction.setMerId(requestParams.getContent().getMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.BAO_FU_CLASSIC_DEDUCTION.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            mortgageDeduction.setPayTime(new Date());
            //设置渠道信息
            mortgageDeduction.setChannel(DeductionChannelEnum.BAO_FU_CLASSIC_DEDUCTION.getCode());

            try {
                Map map = HttpClientUtil.send(baofuConfig.getClassicUrl(), requestParams.switchToNvpList());
                String returnData = (String) map.get("returnData");
                returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getClassicCerFile());
                returnData = SecurityUtil.Base64Decode(returnData);
                JSONObject parse = (JSONObject) JSONObject.parse(returnData);
                String respMsg = parse.getObject("resp_msg", String.class);
                if (StringUtils.equals("交易成功", respMsg)) {
                    mortgageDeduction.setIssuccess("1");
                    //计算并设置手续费
                    operationStrategyContext.getOperationStrategy(DeductionChannelEnum.BAO_FU_CLASSIC_DEDUCTION.getCode()).calculateHandlingCharge(mortgageDeduction);
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
                String respCode = parse.getObject("resp_code", String.class);
                mortgageDeduction.setResult(respCode);
                mortgageDeduction.setErrorResult(respMsg);
                result.add(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                //保存订单号
                result.add(mortgageDeduction);
            }
        }
    }

    /**
     * 中金支付单笔代扣
     *
     * @param list 待代扣的MortgageDeduction数据
     */
    private void deliveryChinaPayClearNetClassic(List<MortgageDeduction> list) {
        for (MortgageDeduction mortgageDeduction : list) {
            Tx2011Req tx2011Req = beanConverter.transToTx2011Req(mortgageDeduction);
            mortgageDeduction.setOrdId(tx2011Req.getTxSN());
            mortgageDeduction.setMerId(tx2011Req.getInstitutionID());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_CLEAR_NET_DEDUCTION.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            //设置渠道信息
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_CLEAR_NET_DEDUCTION.getCode());
            mortgageDeduction.setPayTime(new Date());
            try {
                BaseService tx2011Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class, chinaPayClearNetConfig.getClassicDeductionUrl());
                RequestResult requestResult = tx2011Service.doBusiness(tx2011Req);
                if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                    Tx2011Res tx2011Response = (Tx2011Res) requestResult.getResult().get(0);
                    int status = tx2011Response.getStatus();
                    //status为20是正在处理,不用特别设置状态,默认就是
                    if (status == 30) {
                        //代扣成功
                        mortgageDeduction.setErrorResult("交易成功");
                        mortgageDeduction.setResult(tx2011Response.getCode());
                        mortgageDeduction.setIssuccess("1");
                        //计算并设置手续费
                        operationStrategyContext.getOperationStrategy(DeductionChannelEnum.CHINA_PAY_CLEAR_NET_DEDUCTION.getCode()).calculateHandlingCharge(mortgageDeduction);
                    } else if (status == 40) {
                        //代扣失败
                        JSONObject jsonObject = XmlUtils.documentToJSONObject(tx2011Response.getResponsePlainText());
                        JSONObject body   = (JSONObject) jsonObject.getJSONArray("Body").get(0);
                        mortgageDeduction.setErrorResult(body.getString("ResponseMessage"));
                        mortgageDeduction.setResult(body.getString("ResponseCode"));
                        mortgageDeduction.setIssuccess("0");
                    }
                } else {
                    mortgageDeduction.setErrorResult(requestResult.getReason());
                    mortgageDeduction.setResult(requestResult.getErrorCode());
                    mortgageDeduction.setIssuccess("0");
                }
                result.add(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                //保存订单号
                result.add(mortgageDeduction);
            }
        }
    }

    public List<MortgageDeduction> getResult() {
        return result;
    }

}
