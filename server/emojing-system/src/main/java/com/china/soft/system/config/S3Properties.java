package com.china.soft.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: S3读取配置类
 * @author MingYue
 * @Date 2023/10/9
 */
@Data
@ConfigurationProperties(prefix = "s3")
@Component
public class S3Properties {
    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     *  path-style nginx 反向代理和S3默认支持
     * 模式 {http://bucketname.endpoint}  -- true
     * 模式 {http://endpoint/bucketname}  -- false
     */
    private Boolean pathStyleAccess = false;

    /**
     * 区域
     */
    private String region;

    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码
     */
    private String secretKey;

    /**
     * 最大线程数，默认： 100
     */
    private Integer maxConnections = 50;


}