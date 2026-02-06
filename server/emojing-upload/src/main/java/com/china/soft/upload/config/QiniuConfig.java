package com.china.soft.upload.config;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
/**
 * @Description: 七牛云配置文件
 * @author MingYue
 * @Date 2024/4/17
 */

public class QiniuConfig {

    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Bean
    public Auth getAuth(){
        return Auth.create(accessKey,secretKey);
    }

    @Bean
    public UploadManager getUploadManager(){
        return new UploadManager(new Configuration());
    }
}

