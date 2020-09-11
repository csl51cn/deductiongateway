package org.starlightfinancial.deductiongateway.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.service.impl.Splitter;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategyContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sili.chen on 2018/1/3
 */
@Component
public class Delivery extends Decorator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Delivery.class);

    @Autowired
    private OperationStrategyContext operationStrategyContext;

    List<MortgageDeduction> result;

    @Override
    public void doRoute() throws Exception {
        result = new ArrayList<>();
        super.doRoute();
        delivery();
    }

    private void delivery() {
        List<MortgageDeduction> list = ((Splitter) this.route).getDeductionList();
        if (list.size() > 0) {
            //过滤时有可能剔除记录,所以要判断list的元素数量大于0.
            //自动代扣存在同一代扣记录使用多个代扣渠道组合代扣(服务费和本息分别去代扣):比如代扣金额5万,中金扣2万服务费,,银联扣3万本息.
            //使用每条代扣记录的代扣渠道进行代扣,这个渠道是在使用OperationStrategy的split方法时设置的
            //先扣服务费,再扣本息,如果服务费代扣结果为余额不足,本息不再代扣,避免占用代扣次数(渠道对单卡单日余额不足的次数有限制)

            //按服务费倒序排序
            list.sort(Comparator.comparing(MortgageDeduction::getSplitData2).reversed());
            result.addAll(list);
            for (MortgageDeduction mortgageDeduction : list) {
                try {
                    OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(mortgageDeduction.getChannel());
                    operationStrategy.pay(Collections.singletonList(mortgageDeduction));
                    if (StringUtils.isNotBlank(mortgageDeduction.getErrorResult()) && mortgageDeduction.getErrorResult().contains("不足")) {
                        //只要额度/余额不足,后面不再自动代扣了
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.error("自动代扣时异常:合同号{},客户名称{},卡号{},渠道{}"
                            , mortgageDeduction.getContractNo(), mortgageDeduction.getCustomerName(), mortgageDeduction.getParam3()
                            , DeductionChannelEnum.getDescByCode(mortgageDeduction.getChannel()), e);
                }
            }
        }

    }

    public List<MortgageDeduction> getResult() {
        return result;
    }

}
