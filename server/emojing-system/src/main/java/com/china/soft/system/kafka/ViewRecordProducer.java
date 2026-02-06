package com.china.soft.system.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
public class ViewRecordProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String VIEW_RECORD_TOPIC = "view-record-topic";

    public ViewRecordProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送观看记录消息，使用 ListenableFuture 实现回调
     */
    @Async
    public void sendViewRecord(String articleId, String ipAddress) {
        LocalDateTime now = LocalDateTime.now();
        long tsNow = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String message = String.format("{\"articleId\":\"%s\", \"ip\":\"%s\", \"timestamp\":%d}", articleId, ipAddress, tsNow);

        // 1. 调用 send() 方法，它返回 ListenableFuture
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(VIEW_RECORD_TOPIC, articleId, message);

        // 2. 使用 addCallback 添加成功和失败的处理器
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            // 消息发送成功时调用
            @Override
            public void onSuccess(SendResult<String, String> result) {
                // 通过 result.getRecordMetadata() 获取发送元数据
                log.info("发送成功！Topic: {}, Partition{},Offset: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
            // 消息发送失败时调用
            @Override
            public void onFailure(Throwable e) {
                log.error("Kafka发送浏览记录失败", e);
            }
        });
    }
}