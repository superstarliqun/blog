package com.china.soft.system.kafka;

import cn.hutool.json.JSONUtil;
import com.china.soft.commons.utils.RedisUtil;
import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.system.api.service.ArticleService;
import com.china.soft.system.provider.dal.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ViewRecordConsumer {

    @Autowired
    private RedisUtil redisTemplate;

    @Autowired
    private ArticleService articleService;

    // 监听名为 "view-record-topic" 的 Topic
    // groupId 自动使用 application.yml 中的配置

    /**
     * 注释：【核心业务逻辑】
     * 1. 解析消息，获取 articleId 和 ip
     * 2. 调用 Redis 服务对该 articleId 的 PV 进行 INCR (+1) 操作
     * 3. 调用 Redis 服务使用 HyperLogLog (HLL) 对 IP 进行去重统计 (UV)
     * 4. ...其他业务逻辑
     */
    @KafkaListener(topics = "view-record-topic", groupId = "my-blog-consumer-group")
    public void listen(String message) {
        log.info("收到观看记录消息{}", message);
        // @MingYue：存储一张表


    }
}