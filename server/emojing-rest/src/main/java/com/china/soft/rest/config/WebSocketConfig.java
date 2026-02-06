package com.china.soft.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker // 启用 STOMP 消息代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 1. 客户端订阅前缀：当客户端订阅 /topic/upload/taskId123 时，消息会路由到这里
        config.enableSimpleBroker("/topic");

        // 2. 服务器接收前缀：客户端发送消息到 /app/hello 时，会路由到 @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个 STOMP 协议的终端，允许所有域名的客户端连接
        registry.addEndpoint("/ws/progress").setAllowedOriginPatterns("*");
        // 如果需要兼容不支持 WebSocket 的浏览器，可以启用 SockJS
        // .withSockJS();
    }
}