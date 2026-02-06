package com.china.soft.srcurity.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: SpringSecurity白名单资源路径配置
 * @author MingYue
 * @Date 2023/9/15
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.ignored")
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();

}
