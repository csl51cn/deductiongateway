package org.starlightfinancial.deductiongateway.strategy.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.Constant;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.UnionPayUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 银联白名单代扣渠道操作策略
 * @date: Created in 2018/6/4 11:17
 * @Modified By:
 */
@Component("0003")
public class ChinaPayClassicDeductionStrategyImpl implements OperationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChinaPayClassicDeductionStrategyImpl.class);

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private BeanConverter beanConverter;


    @Autowired
    private ChinaPayConfig chinaPayConfig;


    /**
     * 查询是否签约
     *
     * @param Id 记录id
     * @return 返回包含查询结果的Message对象
     */
    @Override
    public Message queryIsSigned(Integer Id) {
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
     * @return 返回包含代扣执行情况的Message对象
     */
    @Override
    public void pay(List<MortgageDeduction> mortgageDeductions) throws Exception {
        for (MortgageDeduction mortgageDeduction : mortgageDeductions) {
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

            String chkValue = UnionPayUtil.sign(goPayBean.getMerId(), goPayBean.createStringBuffer(),chinaPayConfig.getClassicPfxFile());
            if (StringUtils.isEmpty(chkValue) || chkValue.length() != 256) {
                LOGGER.debug("银联报文签名异常,订单号:{},合同号:{}",mortgageDeduction.getOrdId(),mortgageDeduction.getContractNo() );
                mortgageDeduction.setErrorResult("银联报文签名异常");
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
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
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
                mortgageDeduction.setErrorResult(ErrorCodeEnum.getValueByCode(payStat));
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


}
