package com.dongsoj.dongsojbackendgateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 设置过滤器的过滤规则
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 在这里编写认证逻辑
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = serverHttpRequest.getURI().getPath();
        if (antPathMatcher.match("/**/inner/**",path)){
            ServerHttpResponse serverHttpResponse = exchange.getResponse();
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            DataBufferFactory dataBufferFactory = serverHttpResponse.bufferFactory();;
            DataBuffer dataBuffer = dataBufferFactory.wrap("无权限".getBytes(StandardCharsets.UTF_8));
            return serverHttpResponse.writeWith(Mono.just(dataBuffer));
        }
        return chain.filter(exchange);
    }

    /**
     * 设置过滤器的执行顺序（将优先级调制最高）
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
