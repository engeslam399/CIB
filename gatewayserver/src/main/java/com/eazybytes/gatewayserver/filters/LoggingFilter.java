package com.eazybytes.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        log.info("Incoming Request -> Method: {}, Path: {}, Query: {}, Headers:{}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                exchange.getRequest().getQueryParams(),
                exchange.getRequest().getHeaders());

        return chain.filter(exchange).doFinally(signalType -> {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Incoming Request -> Path: {}, Headers:{}, Status Code: {}, Duration:{}",
                    exchange.getRequest().getURI(),
                    exchange.getResponse().getHeaders(),
                    exchange.getResponse().getStatusCode(),
                    duration);
        });
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
