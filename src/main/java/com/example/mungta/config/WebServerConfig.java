package com.example.mungta.config;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class WebServerConfig implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    // The default value is 8192 (8K) but may result in 413 when header is too lager.    
    @Value("${server.max-http-header-size:65536}")
    private int maxHeaderSize;

    public void customize(NettyReactiveWebServerFactory container) {
        container.addServerCustomizers(builder -> builder.maxHeaderSize(maxHeaderSize));
    }

}
