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
import org.starlightfinancial.deductiongateway.async.AsyncManager;
import org.starlightfinancial.deductiongateway.async.factory.AsyncTaskFactory;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.TransContent;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.TransHead;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.TransReqDataWrapper;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.request.TransReqBF0040002;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.request.TransReqBF0040003;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.request.TransReqBF0040004;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.response.TransRespBF0040002;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.response.TransRespBF0040003;
import org.starlightfinancial.deductiongateway.baofu.domain.payment.response.TransRespBF0040004;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.common.UserContext;
import org.starlightfinancial.deductiongateway.config.BaofuConfig;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssue;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfoRepository;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;
import org.starlightfinancial.deductiongateway.enums.*;
import org.starlightfinancial.deductiongateway.strategy.LoanIssueStrategy;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.Predicate;
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
     * 查询代付交易结果
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     * @return 返回请求情况
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message queryLoanIssueResult(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        SysUser user = UserContext.getUser();
        //报文内容
        TransContent<TransReqBF0040002> transContent = new TransContent<>();
        //报文体
        TransReqDataWrapper<TransReqBF0040002> transReqBF0040002TransReqDataWrapper = new TransReqDataWrapper<>();
        List<TransReqBF0040002> transReqBF0040002List = new ArrayList<>();
        loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
            TransReqBF0040002 transReqData = new TransReqBF0040002();
            LoanIssue loanIssue = findTheLastRecord(loanIssueBasicInfo);
            transReqData.setTransBatchId(loanIssue.getBatchId());
            transReqData.setTransNo(loanIssue.getTransactionNo());
            transReqBF0040002List.add(transReqData);
        });
        transReqBF0040002TransReqDataWrapper.setTransReqData(transReqBF0040002List);
        transContent.setTransReqDatas(Collections.singletonList(transReqBF0040002TransReqDataWrapper));
        try {
            String transContentJsonString = objectMapper.writeValueAsString(transContent);
            Map map = HttpClientUtil.send(baofuConfig.getClassicPayLoanIssueQueryUrl(), Collections.singletonList(new BasicNameValuePair("transContent", transContentJsonString)));
            if (map.containsKey("returnData")) {
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())
                        || StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_15.getCode())) {
                    //获取返回的结果
                    String resultArray = jsonObject.getJSONArray("result").getString(0);
                    TransContent<TransRespBF0040002> returnTransContent = objectMapper.readValue(resultArray, new TypeReference<TransContent<TransRespBF0040002>>() {
                    });
                    TransHead returnTransHead = returnTransContent.getTransHead();
                    String returnCode = returnTransHead.getReturnCode();
                    if (StringUtils.equals(returnCode, BaoFuPayReturnCodeEnum.RETURN_CODE_0000.getCode())) {
                        List<TransRespBF0040002> transReqData = returnTransContent.getTransReqDatas().get(0).getTransReqData();
                        //按照商户订单号分组,因为一笔订单可能被拆分成多笔交易
                        Map<String, List<TransRespBF0040002>> groupByTransactionNoMap = transReqData.stream().collect(Collectors.groupingBy(TransRespBF0040002::getTransNo));
                        for (LoanIssueBasicInfo loanIssueBasicInfo : loanIssueBasicInfos) {
                            LoanIssue loanIssue = findTheLastRecord(loanIssueBasicInfo);
                            List<TransRespBF0040002> transRespBF0040002s = groupByTransactionNoMap.get(loanIssue.getTransactionNo());

                            if (transRespBF0040002s.size() > 1) {
                                BigDecimal transactionFee = transRespBF0040002s.stream().map(transRespBF0040002 -> new BigDecimal(transRespBF0040002.getTransFee())).reduce(BigDecimal.ZERO, BigDecimal::add);
                                //手续费
                                loanIssue.setTransactionFee(transactionFee);
                                //判断是否放款成功:放款金额可能会超过渠道限额,变为多笔放款.例如:放款100万,渠道限额20万,此时会分5笔交易,判断放款成功数是否为5
                                Map<String, List<TransRespBF0040002>> groupByStateMap = transRespBF0040002s.stream().collect(Collectors.groupingBy(TransRespBF0040002::getState));
                                //转账成功的集合
                                List<TransRespBF0040002> success = groupByStateMap.get(LoanIssueStatusEnum.STATUS1.getBaoFuCode());
                                //转账中的集合
                                List<TransRespBF0040002> progress = groupByStateMap.get(LoanIssueStatusEnum.STATUS0.getBaoFuCode());
                                //转账失败的集合
                                List<TransRespBF0040002> fail = groupByStateMap.get(LoanIssueStatusEnum.STATUS2.getBaoFuCode());
                                //退款的集合
                                List<TransRespBF0040002> refund = groupByStateMap.get(LoanIssueStatusEnum.STATUS3.getBaoFuCode());
                                //全部成功
                                if (Objects.nonNull(success) && success.size() == transRespBF0040002s.size()) {
                                    loanIssue.setTransactionStatus(LoanIssueStatusEnum.STATUS1.getCode());
                                    Optional<Date> maxTransEndTime = success.stream().map(transRespBF0040002 -> Utility.convertToDate(transRespBF0040002.getTransEndTime(), "yyyy-MM-dd HH:mm:ss")).max((date, anotherDate) -> date != null ? date.compareTo(anotherDate) : 0);
                                    loanIssue.setTransactionEndTime(maxTransEndTime.get());
                                }
                                //处理中
                                if (Objects.nonNull(progress) && progress.size() == transRespBF0040002s.size()) {
                                    loanIssue.setTransactionStatus(LoanIssueStatusEnum.STATUS0.getCode());
                                }
                                //部分成功,部分失败
                                if (Objects.nonNull(fail) && fail.size() > 0 && Objects.nonNull(success) && success.size() > 0) {
                                    loanIssue.setTransactionStatus(LoanIssueStatusEnum.STATUS4.getCode());
                                    String transactionRemark = transRespBF0040002s.stream().map(TransRespBF0040002::getTransRemark).collect(Collectors.joining(","));
                                    loanIssue.setTransactionRemark(transactionRemark);
                                }
                                //全部失败
                                if (Objects.nonNull(fail) && fail.size() == transRespBF0040002s.size()) {
                                    loanIssue.setTransactionStatus(LoanIssueStatusEnum.STATUS2.getCode());
                                    String transactionRemark = transRespBF0040002s.stream().map(TransRespBF0040002::getTransRemark).collect(Collectors.joining(","));
                                    loanIssue.setTransactionRemark(transactionRemark);
                                }
                                //退款
                                if (Objects.nonNull(refund) && refund.size() > 0) {
                                    loanIssue.setTransactionStatus(LoanIssueStatusEnum.STATUS3.getCode());
                                }
                            } else {
                                //只有一条,未超渠道限额,直接设置状态
                                TransRespBF0040002 transRespBF0040002 = transRespBF0040002s.get(0);
                                if (StringUtils.isNotBlank(transRespBF0040002.getTransFee())) {
                                    loanIssue.setTransactionFee(new BigDecimal(transRespBF0040002.getTransFee()));
                                }
                                loanIssue.setTransactionStatus(LoanIssueStatusEnum.getCodeByBaoFuCode(transRespBF0040002.getState()));
                                loanIssue.setTransactionEndTime(Utility.convertToDate(transRespBF0040002.getTransEndTime(), "yyyy-MM-dd HH:mm:ss"));
                                loanIssue.setTransactionRemark(transRespBF0040002.getTransRemark());
                            }
                        }
                        loanIssueBasicInfoRepository.save(loanIssueBasicInfos);
                        AsyncManager.getInstance().execute(AsyncTaskFactory.sendLoanIssueNotice(loanIssueBasicInfos));
                        return Message.success();
                    } else {
                        return Message.fail("查询失败,返回的消息是:" + returnTransHead.getReturnMsg());
                    }

                } else if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_17.getCode())) {
                    JSONObject returnTransContent = jsonObject.getJSONObject("trans_content");
                    String returnMsg = returnTransContent.getJSONObject("trans_head").getString("return_msg");
                    return Message.fail("查询失败,返回的消息是:" + returnMsg);
                } else {
                    return Message.fail("查询失败");
                }
            } else {
                LOGGER.info("未获得rsb返回信息");
                return Message.fail("未获得返回信息");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询代付交易退款结果
     *
     * @param queryDate 查询日期
     * @return 请求情况
     */
    @Override
    public Message queryLoanIssueRefund(Date queryDate) {
        SysUser user = UserContext.getUser();
        //报文内容
        TransContent<TransReqBF0040003> transContent = new TransContent<>();
        //报文体
        TransReqDataWrapper<TransReqBF0040003> transReqBF0040003TransReqDataWrapper = new TransReqDataWrapper<>();
        TransReqBF0040003 transReqBF0040003 = new TransReqBF0040003();
        transReqBF0040003.setTransBeginTime(Utility.convertToString(queryDate, "yyyyMMdd"));
        transReqBF0040003.setTransEndTime(transReqBF0040003.getTransBeginTime());
        transReqBF0040003TransReqDataWrapper.setTransReqData(Collections.singletonList(transReqBF0040003));
        transContent.setTransReqDatas(Collections.singletonList(transReqBF0040003TransReqDataWrapper));
        try {
            String transContentJsonString = objectMapper.writeValueAsString(transContent);
            Map map = HttpClientUtil.send(baofuConfig.getClassicPayLoanIssueRefundUrl(), Collections.singletonList(new BasicNameValuePair("transContent", transContentJsonString)));
            if (map.containsKey("returnData")) {
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())
                        || StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_15.getCode())) {
                    //获取返回的结果
                    String resultArray = jsonObject.getJSONArray("result").getString(0);
                    TransContent<TransRespBF0040003> returnTransContent = new TransContent<>();
                    //转换为TransContent
                    returnTransContent = objectMapper.readValue(resultArray, new TypeReference<TransContent<TransRespBF0040003>>() {
                    });
                    TransHead returnTransHead = returnTransContent.getTransHead();
                    String returnCode = returnTransHead.getReturnCode();
                    if (StringUtils.equals(returnCode, BaoFuPayReturnCodeEnum.RETURN_CODE_0000.getCode())) {
                        List<TransRespBF0040003> transReqData = returnTransContent.getTransReqDatas().get(0).getTransReqData();
                        //按照商户订单号分组
                        Map<String, List<TransRespBF0040003>> groupByTransactionNoMap = transReqData.stream().collect(Collectors.groupingBy(TransRespBF0040003::getTransNo));
                        groupByTransactionNoMap.forEach((transactionNo, list) -> {
                            LoanIssueBasicInfo loanIssueBasicInfo = loanIssueBasicInfoRepository.findOne((root, query, cb) -> {
                                Predicate equal = cb.equal(root.join("loanIssue").get("transactionNo"), transactionNo);
                                return cb.and(equal);
                            });
                            LoanIssue loanIssue = findTheLastRecord(loanIssueBasicInfo);
                            //就算拆分了,如果发生退款也是全部都退
                            TransRespBF0040003 transRespBF0040003 = list.get(0);
                            loanIssue.setTransactionStatus(transRespBF0040003.getState());
                            loanIssue.setTransactionEndTime(Utility.convertToDate(transRespBF0040003.getTransEndTime(), "yyyy-MM-dd HH:mm:ss"));
                            loanIssue.setTransactionStatus(LoanIssueStatusEnum.getCodeByBaoFuCode(transRespBF0040003.getState()));
                            loanIssue.setTransactionRemark(transRespBF0040003.getTransRemark());
                            loanIssue.setModifiedId(user.getId());
                            loanIssue.setGmtModified(new Date());
                            loanIssueBasicInfoRepository.save(loanIssueBasicInfo);
                        });

                        return Message.success();
                    } else {
                        return Message.fail("查询失败,返回的消息是:" + returnTransHead.getReturnMsg());
                    }
                } else if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_17.getCode())) {
                    //{"trans_content":{"trans_head":{"return_code":"0202","return_msg":"商户不存在,请联系宝付技术支持"}}}
                    JSONObject returnTransContent = jsonObject.getJSONObject("trans_content");
                    String returnMsg = returnTransContent.getJSONObject("trans_head").getString("return_msg");
                    return Message.fail("查询失败,返回的消息是:" + returnMsg);
                } else {
                    return Message.fail("查询失败");
                }
            } else {
                LOGGER.info("未获得rsb返回信息");
                return Message.fail("未获得返回信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
            LoanIssue loanIssue = findTheLastRecord(loanIssueBasicInfo);
            transReqData.setTransNo(loanIssue.getTransactionNo());
            transReqData.setTransMoney(loanIssueBasicInfo.getIssueAmount().toString());
            transReqData.setToAccName(loanIssueBasicInfo.getToAccountName());
            transReqData.setToAccNo(loanIssueBasicInfo.getToAccountNo());
            transReqData.setToBankName(LoanIssueBankEnum.getBankNameByCode(loanIssueBasicInfo.getToBankNameId()));
            transReqData.setToProName(loanIssueBasicInfo.getToBankProvince());
            transReqData.setToCityName(loanIssueBasicInfo.getToBankCity());
            transReqData.setToAccDept(loanIssueBasicInfo.getToBankBranch());
            transReqData.setTransCardId(loanIssueBasicInfo.getIdentityNo());
            transReqData.setTransMobile(loanIssueBasicInfo.getMobileNo());
            transReqData.setTransSummary(loanIssue.getTransactionSummary());
            transReqData.setTransReserved("");
            loanIssue.setTransactionStartTime(new Date());
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
                            //设置收单状态和交易状态
                            LoanIssue loanIssue = findTheLastRecord(loanIssueBasicInfo);
                            loanIssue.setAcceptTransactionStatus(ConstantsEnum.SUCCESS.getCode());
                            loanIssue.setTransactionStatus(LoanIssueStatusEnum.STATUS0.getCode());
                        });
                        List<TransRespBF0040004> transReqData = returnTransContent.getTransReqDatas().get(0).getTransReqData();
                        loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                            LoanIssue loanIssue = findTheLastRecord(loanIssueBasicInfo);
                            //通过商户订单号筛选出宝付返回的记录
                            List<TransRespBF0040004> collect = transReqData.stream().filter(transRespBF0040004 ->
                                    StringUtils.equals(transRespBF0040004.getTransNo(), loanIssue.getTransactionNo())).collect(Collectors.toList());
                            //设置宝付批次号
                            loanIssue.setBatchId(collect.get(0).getTransBatchId());
                            //将宝付订单号使用|分隔,如:"订单号1|订单号2"
                            String transOrderIdStr = collect.stream().map(TransRespBF0040004::getTransOrderId).collect(Collectors.joining("|"));
                            loanIssue.setServiceTransactionNo(transOrderIdStr);
                        });
                    } else {
                        //交易未受理,转账失败
                        loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                            LoanIssue theLastRecord = findTheLastRecord(loanIssueBasicInfo);
                            theLastRecord.setAcceptTransactionStatus(ConstantsEnum.FAIL.getCode());
                            theLastRecord.setTransactionStatus(LoanIssueStatusEnum.STATUS2.getCode());
                        });
                    }
                    loanIssueBasicInfoRepository.save(loanIssueBasicInfos);
                    return Message.success("交易已受理");
                } else if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_17.getCode())) {
                    LOGGER.info("宝付返回的明文内容:{}", returnData);
                    loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                        findTheLastRecord(loanIssueBasicInfo).setAcceptTransactionStatus(ConstantsEnum.FAIL.getCode());
                    });
                    loanIssueBasicInfoRepository.save(loanIssueBasicInfos);
                    return Message.success("交易未受理");
                } else {
                    loanIssueBasicInfos.forEach(loanIssueBasicInfo -> {
                        findTheLastRecord(loanIssueBasicInfo).setAcceptTransactionStatus(ConstantsEnum.FAIL.getCode());
                    });
                    loanIssueBasicInfoRepository.save(loanIssueBasicInfos);
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
