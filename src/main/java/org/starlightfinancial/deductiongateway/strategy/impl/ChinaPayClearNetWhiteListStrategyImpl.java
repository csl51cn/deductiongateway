package org.starlightfinancial.deductiongateway.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.config.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
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
    private ChinaPayConfig chinaPayConfig;

    /**
     * 生成白名单
     *
     * @param list 卡号信息
     * @return 生成白名单信息
     */
    @Override
    public Map<String, String> createWhiteList(List<AccountManager> list) {
        StringBuilder content = new StringBuilder();
        //持卡人证件类型|持卡人证件号|持卡人姓名|持卡人卡号|手机号码|联系地址|Email地址  回车换行
        list.forEach(accountManager -> {
            content.append("01|").append(accountManager.getCertificateNo()).append("|").append(accountManager.getAccountName())
                    .append("|").append(accountManager.getAccount()).append("|").append(accountManager.getMobile()).append("||")
                    .append("\r\n");

        });

        HashMap<String, String> map = new HashMap<>(4);
        StringBuilder fileNameStringBuilder = new StringBuilder(chinaPayConfig.getClassicMerId());
        //文件名格式例子:商户号_20190709_001_V
        String fileName = fileNameStringBuilder.append("_").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .append("_").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"))).append("_V.txt").toString();
        map.put(fileNameStringBuilder.toString(), content.toString());
        return map;
    }
}
