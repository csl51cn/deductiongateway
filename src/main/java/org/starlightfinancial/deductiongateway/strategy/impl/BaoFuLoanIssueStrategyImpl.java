package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.TransContent;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.TransHead;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.TransReqDataWrapper;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.request.TransReqBF0040004;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.response.TransRespBF0040004;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfoRepository;
import org.starlightfinancial.deductiongateway.enums.*;
import org.starlightfinancial.deductiongateway.strategy.LoanIssueStrategy;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Senlin.Deng
 * @Description: 宝付代付策略实现类
 * @date: Created in 2018/9/20 14:47
 * @Modified By:
 */
@Component("1001")
public class BaoFuLoanIssueStrategyImpl implements LoanIssueStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaoFuLoanIssueStrategyImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        objectMapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
    }

    @Autowired
    private LoanIssueBasicInfoRepository loanIssueBasicInfoRepository;


    @Autowired
    private BaofuConfig baofuConfig;


    /**
     * 贷款发放
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message loanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        if (loanIssueBasicInfos.size() > 5) {
            //每次操作的记录数不能操作5条,只有自动放款时可能会超过5条记录,此时不需要返回message,
            //通过本系统前端操作放款时,前端做记录不超过5条的校验
            List<List<LoanIssueBasicInfo>> lists = Utility.groupListByQuantity(loanIssueBasicInfos, 5);
            lists.forEach(this::doLoanIssue);
            return null;
        } else {
            return doLoanIssue(loanIssueBasicInfos);
        }
    }

    /**
     * 实际做代付交易的方法
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     */
    private Message doLoanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        //报文内容
        TransContent<TransReqBF0040004> transContent = new TransContent<>();
        //报文头
        TransHead transHead = new TransHead();
        transHead.setTransCount(Integer.toString(loanIssueBasicInfos.size()));
        BigDecimal totalMoney = loanIssueBasicInfos.stream().map(LoanIssueBasicInfo::getIssueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        totalMoney = totalMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        transHead.setTransTotalMoney(Double.toString(totalMoney.doubleValue()));
        transContent.setTransHead(transHead);
        //报文体
        TransReqDataWrapper<TransReqBF0040004> transReqBF0040004TransReqDataWrapper = new TransReqDataWrapper<>();
        ArrayList<TransReqBF0040004> transReqBF0040004ArrayList = new ArrayList<>();
        loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
            TransReqBF0040004 transReqData = new TransReqBF0040004();
            transReqData.setTransNo(loanIssueBasicInfo.getLoanIssue().getTransactionNo());
            transReqData.setTransMoney(loanIssueBasicInfo.getIssueAmount().toString());
            transReqData.setToAccName(loanIssueBasicInfo.getToAccountName());
            transReqData.setToAccNo(loanIssueBasicInfo.getToAccountNo());
            transReqData.setToBankName(LoanIssueBankEnum.getBankNameByCode(loanIssueBasicInfo.getToBankNameId()));
            transReqData.setToProName(loanIssueBasicInfo.getToBankProvince());
            transReqData.setToCityName(loanIssueBasicInfo.getToBankCity());
            transReqData.setToAccDept(loanIssueBasicInfo.getToBankBranch());
            transReqData.setTransCardId(loanIssueBasicInfo.getIdentityNo());
            transReqData.setTransMobile(loanIssueBasicInfo.getMobileNo());
            transReqData.setTransSummary(loanIssueBasicInfo.getRemark());
            transReqData.setTransReserved("");
            loanIssueBasicInfo.getLoanIssue().setTransactionStartTime(new Date());
            transReqBF0040004ArrayList.add(transReqData);
        });
        transReqBF0040004TransReqDataWrapper.setTransReqData(transReqBF0040004ArrayList);
        transContent.setTransReqDatas(Collections.singletonList(transReqBF0040004TransReqDataWrapper));

        try {
            String transContentJsonString = objectMapper.writeValueAsString(transContent);
            Map map = HttpClientUtil.send(baofuConfig.getClassicPayLoanIssueUrl(), Collections.singletonList(new BasicNameValuePair("transContent", transContentJsonString)));
            if (map.containsKey("returnData")) {
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())
                        || StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_15.getCode())) {
                    //获取返回的结果
                    String resultArray = jsonObject.getJSONArray("result").getString(0);
                    TransContent<TransRespBF0040004> returnTransContent = new TransContent<>();
                    //转换为TransContent
                    returnTransContent = objectMapper.readValue(resultArray, new TypeReference<TransContent<TransRespBF0040004>>() {
                    });
                    TransHead returnTransHead = returnTransContent.getTransHead();
                    String returnCode = returnTransHead.getReturnCode();
                    if (StringUtils.equals(returnCode, BaoFuPayReturnCodeEnum.RETURN_CODE_0000.getCode())) {
                        //交易已受理,更新状态,将返回来的批次号和宝付订单号保存下来
                        loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                            loanIssueBasicInfo.getLoanIssue().setAcceptTransactionStatus(ConstantsEnum.SUCCESS.getCode());
                            loanIssueBasicInfo.getLoanIssue().setTransactionStatus(LoanIssueStatusEnum.STATUS0.getCode());
                        });
                        List<TransRespBF0040004> transReqData = returnTransContent.getTransReqDatas().get(0).getTransReqData();
                        loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                            //通过商户订单号筛选出宝付返回的记录
                            List<TransRespBF0040004> collect = transReqData.stream().filter(transRespBF0040004 ->
                                    StringUtils.equals(transRespBF0040004.getTransNo(), loanIssueBasicInfo.getLoanIssue().getTransactionNo())).collect(Collectors.toList());
                            //设置宝付批次号
                            loanIssueBasicInfo.getLoanIssue().setBatchId(collect.get(0).getTransBatchId());
                            //将宝付订单号使用|分隔,如:"订单号1|订单号2"
                            String transOrderIdStr = collect.stream().map(TransRespBF0040004::getTransOrderId).collect(Collectors.joining("|"));
                            loanIssueBasicInfo.getLoanIssue().setServiceTransactionNo(transOrderIdStr);
                        });
                    } else {
                        //交易未受理
                        loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                            loanIssueBasicInfo.getLoanIssue().setAcceptTransactionStatus(ConstantsEnum.FAIL.getCode());
                        });
                    }
                    loanIssueBasicInfoRepository.save(loanIssueBasicInfos);
                    return Message.success("交易已受理");
                } else if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_17.getCode())) {
                    LOGGER.info("宝付返回的明文内容:{}", returnData);
                    loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                        loanIssueBasicInfo.getLoanIssue().setAcceptTransactionStatus(ConstantsEnum.FAIL.getCode());
                    });
                    return Message.success("交易未受理");
                } else {
                    loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                        loanIssueBasicInfo.getLoanIssue().setAcceptTransactionStatus(ConstantsEnum.FAIL.getCode());
                    });
                    return Message.success("交易未受理");
                }
            } else {
                LOGGER.info("未获得rsb返回信息");
                return Message.fail("未获得返回信息");
            }
        } catch (Exception e) {
            LOGGER.error("代付请求时出现错误", e);
            return Message.fail("请求失败");
        }

    }


}
