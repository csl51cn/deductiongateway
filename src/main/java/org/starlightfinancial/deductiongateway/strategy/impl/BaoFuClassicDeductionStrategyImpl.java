package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.RequestParams;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 宝付代扣渠道
 * @date: Created in 2018/6/5 10:42
 * @Modified By:
 */
@Service("0005")
public class BaoFuClassicDeductionStrategyImpl implements OperationStrategy {


    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private BaofuConfig baofuConfig;


    @Autowired
    private ChinaPayConfig chinaPayConfig;


    @Autowired
    private BeanConverter beanConverter;

    private static final BigDecimal FIVE_THOUSAND = new BigDecimal(5000);

    private static final BigDecimal FIFTY_THOUSAND = new BigDecimal(50000);


    /**
     * 查询是否签约
     *
     * @param id 记录id
     * @return 返回包含查询结果的Message对象
     */
    @Override
    public Message queryIsSigned(Integer id) {
        return null;
    }

    /**
     * 发送短信验证码
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回包含短信发送结果的Message对象
     */
    @Override
    public Message sendSignSmsCode(AccountManager accountManager) {
        return null;
    }

    /**
     * 签约
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回签约结果的Message对象
     */
    @Override
    public Message sign(AccountManager accountManager) {
        return null;
    }

    /**
     * 代扣
     *
     * @param mortgageDeductions mortgageDeduction列表
     * @throws Exception 执行代扣异常
     */
    @Override
    public void pay(List<MortgageDeduction> mortgageDeductions) throws Exception {
        for (MortgageDeduction mortgageDeduction : mortgageDeductions) {

            RequestParams requestParams = beanConverter.transToRequestParams(mortgageDeduction);
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
                    calculateHandlingCharge(mortgageDeduction);
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
                String respCode = parse.getObject("resp_code", String.class);
                mortgageDeduction.setResult(respCode);
                mortgageDeduction.setErrorResult(respMsg);
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                //保存订单号
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            }
        }
    }

    /**
     * 查询代扣结果
     *
     * @param mortgageDeduction 代扣记录
     * @return 返回包含代扣查询结果Message对象
     */
    @Override
    public Message queryPayResult(MortgageDeduction mortgageDeduction) {
        return null;
    }

    /**
     * 计算并设置手续费
     *
     * @param mortgageDeduction 代扣记录
     */
    @Override
    public void calculateHandlingCharge(MortgageDeduction mortgageDeduction) {
        BigDecimal totalAmount = mortgageDeduction.getSplitData1().add(mortgageDeduction.getSplitData2());
        if (totalAmount.compareTo(FIVE_THOUSAND) <= 0) {
            //代扣金额≤5000
            mortgageDeduction.setHandlingCharge(baofuConfig.getLevelOne());
        } else if (totalAmount.compareTo(FIFTY_THOUSAND) <= 0) {
            //代扣金额>5000 & 代扣金额 <=50000
            mortgageDeduction.setHandlingCharge(baofuConfig.getLevelTwo());
        } else {
            //代扣金额>50000
            mortgageDeduction.setHandlingCharge(baofuConfig.getLevelThree());
        }
    }


}
