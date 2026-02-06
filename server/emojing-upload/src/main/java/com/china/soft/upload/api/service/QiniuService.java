package com.china.soft.upload.api.service;

import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.web.multipart.MultipartFile;


public interface QiniuService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    CommonResponse<String> upload(MultipartFile file);

    /**
     * 获取文件
     * @param url key的地址
     */
    String getFile(String url);
}
