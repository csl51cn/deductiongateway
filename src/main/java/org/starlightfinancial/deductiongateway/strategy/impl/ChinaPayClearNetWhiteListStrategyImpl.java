package org.starlightfinancial.deductiongateway.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.config.ChinaPayClearNetConfig;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.enums.ChinaPayClearNetBankCodeEnum;
import org.starlightfinancial.deductiongateway.strategy.WhiteListStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/7/15 13:09
 * @Modified By:
 */
@Component("chinaPayClearNet")
public class ChinaPayClearNetWhiteListStrategyImpl implements WhiteListStrategy {


    @Autowired
    private ChinaPayClearNetConfig config;

    /**
     * 生成白名单
     *
     * @param list 卡号信息
     * @return 生成白名单信息
     */
    @Override
    public Map<String, String> createWhiteList(List<AccountManager> list) {
        StringBuilder content = new StringBuilder();
        //商户号|卡号|姓名|账户类型:个人账户(企业账户)|银行编码|证件类型|证件号码|手机|邮箱  回车换行
        list.forEach(accountManager -> {
            content.append(config.getClassicMemberId()).append("|").append(accountManager.getAccount()).append("|").append(accountManager.getAccountName())
                    .append("|11|").append(ChinaPayClearNetBankCodeEnum.getCodeByBankName(accountManager.getBankName())).append("|0|")
                    .append(accountManager.getCertificateNo()).append("|").append(accountManager.getMobile()).append("|\r\n");

        });

        HashMap<String, String> map = new HashMap<>(4);
        StringBuilder fileNameStringBuilder = new StringBuilder(config.getClassicMemberId());
        //文件名格式:商户号_年月日_时分秒
        String fileName = fileNameStringBuilder.append("_").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .append("_").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"))).append(".txt").toString();
        map.put(fileNameStringBuilder.toString(), content.toString());
        return map;
    }
}
