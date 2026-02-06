package com.china.soft.upload.api.service;

public interface WebSocketService {


    /**
     * 推送文件上传进度给指定任务的订阅者
     * @param taskId 任务ID，作为订阅主题的一部分
     * @param percent 进度百分比 (0-100)
     * @param message 进度描述
     */
    void pushProgress(String taskId, int percent, String message);

}
