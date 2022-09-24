package com.example.mungta.config;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class WebServerConfig implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    // The default value is 8192 (8K) but may result in 413 when header is too lager.
    // Enlarge the header size to 16384 (16K) which is enough for most cases.
    private static final int MAX_HEADER_SIZE = 40000;

    public void customize(NettyReactiveWebServerFactory factory) {
        factory.addServerCustomizers(server ->
                server.httpRequestDecoder(decoder -> decoder.maxHeaderSize(MAX_HEADER_SIZE)));
    }
}
