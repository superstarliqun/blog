package com.china.soft.upload.provider.service;

import cn.hutool.core.io.IoUtil;
import com.china.soft.upload.api.service.FileConfigService;
import com.china.soft.upload.api.service.FileService;
import com.china.soft.upload.core.FileClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;

    @Override
    public void test(MultipartFile file) {
        try {
            String path = "/" + file.getOriginalFilename();
            FileClient fileClient = fileConfigService.getMasterFileClient();
            String type = Files.probeContentType(Paths.get(path));
            fileClient.upload(IoUtil.readBytes(file.getInputStream()),path,type);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
