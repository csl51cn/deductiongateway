package org.starlightfinancial.deductiongateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 服务费公司配置
 * @date: Created in 2018/6/5 9:36
 * @Modified By:
 */
@Configuration
@ConfigurationProperties(prefix = "service.config")
public class ServiceCompanyConfig {
    private Map<String, Map<String, String>> serviceCompanyMap;


    public Map<String, Map<String, String>> getServiceCompanyMap() {
        return serviceCompanyMap;
    }

    public void setServiceCompanyMap(Map<String, Map<String, String>> serviceCompanyMap) {
        this.serviceCompanyMap = serviceCompanyMap;
    }

    /**
     * 根据服务费公司和渠道返回服务费公司商户号
     *
     * @param serviceCompany 服务费公司中文名
     * @param channel        渠道
     * @return 对应条件的服务费公司商户号
     */
    public String getServiceCompanyCode(String serviceCompany, String channel) {

        Map<String, String> channelCodeMap = serviceCompanyMap.getOrDefault(serviceCompany, serviceCompanyMap.get("铠岳"));
        return channelCodeMap.getOrDefault(channel, null);
    }


}
