package com.china.soft.upload.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

@Component
public class QiniuUtils {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private Auth auth;

    @Value("${qiniu.bucket}")
    private String bucketName;

    @Value("${qiniu.domain}")
    private String url;

    public String upload(FileInputStream file, String fileName) throws QiniuException {
        String token = auth.uploadToken(bucketName);
        fileName = "myimage/"+fileName;
        Response res = uploadManager.put(file, fileName, token, null, null);
        if (!res.isOK()) {
            throw new RuntimeException("上传七牛云出错:" + res);
        }
        return url+"/"+fileName;
    }

    public String update1(ByteArrayInputStream byteArrayInputStream, String fileName) throws QiniuException {
        String token = auth.uploadToken(bucketName);
        fileName = "myimage/"+fileName;
        Response res = uploadManager.put(byteArrayInputStream, fileName, token, null, null);
        if (!res.isOK()) {
            throw new RuntimeException("上传七牛云出错:" + res);
        }
        return url+"/"+fileName;
    }
}

