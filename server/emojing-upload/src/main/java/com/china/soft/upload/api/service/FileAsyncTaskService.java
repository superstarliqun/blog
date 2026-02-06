package com.china.soft.upload.api.service;

import com.china.soft.upload.core.FileClient;

public interface FileAsyncTaskService {

    /**
     * 文件上传到服务器，项目到文件服务器开启新的线程异步进行
     * @param fileClient 文件客户端动态生成
     * @param content 上传统一设置参数 字节流
     * @param path 上传统一设置参数 地址
     * @param type 上传统一设置参数 类型
     */
    void waitUpload(FileClient fileClient, byte[] content, String path, String type);


}
