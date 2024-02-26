package com.cloud.filter;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求中的用户信息，这里假设用户信息在请求头中的 Authorization 字段中
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 对用户信息进行校验，这里假设校验函数为 validateUser()
        if (validateUser(authorizationHeader)) {
            // 用户信息合法，放行请求
            return chain.filter(exchange);
        } else {
            // 用户信息不合法，拦截请求并返回401 Unauthorized
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    // 校验用户信息的方法，可以根据实际情况实现
    private boolean validateUser(String authorizationHeader) {
        // 在这里进行用户信息的验证逻辑，比如解析JWT token，验证用户名密码等
        // 如果验证成功，返回 true，否则返回 false
        // 示例代码中简单地检查Authorization头是否存在且不为空
        //return authorizationHeader != null && !authorizationHeader.isEmpty();
        return true;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
