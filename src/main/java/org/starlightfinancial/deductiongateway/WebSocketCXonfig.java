package org.starlightfinancial.deductiongateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author: SiliChen
 * @Description:
 * @Date: Created in 15:05 2018/9/29
 * @Modified By:
 */
@Configuration
public class WebSocketCXonfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
