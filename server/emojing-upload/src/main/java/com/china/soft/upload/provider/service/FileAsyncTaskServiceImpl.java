package com.china.soft.upload.provider.service;

import com.china.soft.commons.exception.BusinessException;
import com.china.soft.upload.api.service.FileAsyncTaskService;
import com.china.soft.upload.api.service.WebSocketService;
import com.china.soft.upload.core.FileClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileAsyncTaskServiceImpl implements FileAsyncTaskService {

    // ... 其他依赖 ...
    @Resource
    private WebSocketService webSocketService;

    @Override
    @Async
    public void waitUpload(FileClient fileClient, byte[] content, String path, String type) {
        try {
            webSocketService.pushProgress("123123",92,"正在连接存储服务器...");
            // ... 其他依赖 ...
            fileClient.upload(content,path,type);
            System.out.println("上传完啦");
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }
    }
}
