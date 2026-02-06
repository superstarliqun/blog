package com.china.soft.upload.provider.service;

import com.china.soft.upload.api.service.WebSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("webSocketUploadModule")
public class WebSocketServiceImpl implements WebSocketService {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 推送文件上传进度给指定任务的订阅者
     * @param taskId 任务ID，作为订阅主题的一部分
     * @param percent 进度百分比 (0-100)
     * @param message 进度描述
     */
    @Override
    public void pushProgress(String taskId, int percent, String message) {
        // 构建要发送的消息体
        Map<String, Object> progress = new HashMap<>();
        progress.put("taskId", taskId);
        progress.put("percent", percent);
        progress.put("message", message);
        progress.put("timestamp", System.currentTimeMillis());

        // 向订阅了 /topic/upload/{taskId} 的客户端发送消息
        String destination = "/topic/upload/" + taskId;
        messagingTemplate.convertAndSend(destination, progress);
    }
}
