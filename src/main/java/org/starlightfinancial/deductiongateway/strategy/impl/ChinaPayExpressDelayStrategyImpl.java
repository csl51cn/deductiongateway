package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.ChinaPayDelayRequestParams;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.enums.ChinaPayReturnCodeEnum;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 银联新无卡代扣
 * @date: Created in 2018/6/5 11:37
 * @Modified By:
 */
@Service("0004")
public class ChinaPayExpressDelayStrategyImpl implements OperationStrategy {

    @Resource(name = "0001")
    private OperationStrategy operationStrategy;

    @Autowired
    BeanConverter beanConverter;

    @Autowired
    ChinaPayConfig chinaPayConfig;


    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

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

            ChinaPayDelayRequestParams chinaPayDelayRequestParams = beanConverter.transToChinaPayDelayRequestParams(mortgageDeduction);
            mortgageDeduction.setOrdId(chinaPayDelayRequestParams.getMerOrderNo());
            mortgageDeduction.setMerId(chinaPayConfig.getExpressRealTimeMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setIsoffs("0");
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_EXPRESS_DELAY.getCode());
            mortgageDeduction.setPayTime(new Date());

            try {
                Map map = HttpClientUtil.send(chinaPayConfig.getExpressDelayUrl(), chinaPayDelayRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");

                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                String errorCodeDesc = ChinaPayReturnCodeEnum.getValueByCode(jsonObject.getString("error_code"));
                mortgageDeduction.setErrorResult(StringUtils.isEmpty(errorCodeDesc) ? jsonObject.getString("reason") : errorCodeDesc);
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                //返回0014表示数据接收成功,如果不为0014可以交易设置为失败
                if (!StringUtils.equals(jsonObject.getString("error_code"), "0014")) {
                    mortgageDeduction.setIssuccess("0");
                }
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
        return operationStrategy.queryPayResult(mortgageDeduction);
    }

}
