package com.dongsoj.dongsojbackendgateway.filter;


import com.dongsoj.dongsojbackendcommon.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public class AuthJwtFilter implements GlobalFilter, Ordered {

    @Value("#{'${gateway.excludedUrls}'.split(',')}")
    private List<String> excludedUrls;


    /**
     * 过滤器核心代码
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1、首先收取请求头中的请求地址,排除不需要权限校验的链接
        String path = exchange.getRequest().getURI().getPath();
        if (excludedUrls.contains(path)){
            return chain.filter(exchange);
        }
        // 2、校验token
        // 2.1、从请求头中获取token
        String authToken = exchange.getRequest().getHeaders().getFirst("AuthToken");
        if (!StringUtils.isEmpty(authToken)){
            // 2.2、这是为了在处理 JWT（JSON Web Token）时，将签名和载荷部分分离
            authToken = authToken.replace("Bearer ", "");
        }
        ServerHttpResponse response = exchange.getResponse();
        boolean verifyToken = JwtUtils.verifyToken(authToken);
        if (!verifyToken){
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("errCode", 401);
            responseData.put("errMessage","用户未登录");
            return responseError(response,responseData);
        }
        return chain.filter(exchange);
    }

    /**
     * 将Map数据进行向JSON数据进行展示
     * @param response
     * @param responseData
     * @return
     */
    public Mono<Void> responseError(ServerHttpResponse response, Map<String,Object> responseData){
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data = new byte[0];
        try{
            data = objectMapper.writeValueAsBytes(responseData);
        }catch (Exception e){
            e.printStackTrace();
        }
        DataBuffer buffer = response.bufferFactory().wrap(data);
        // 设置响应状态码
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 设置响应头
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
