package com.china.soft.upload.provider.service;

import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.upload.api.service.QiniuService;
import com.china.soft.upload.utils.ImageUtil;
import com.china.soft.upload.utils.QiniuUtils;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;

import static com.china.soft.commons.utils.response.CommonResponse.error;
import static com.china.soft.commons.utils.response.CommonResponse.success;


@Slf4j
@Service
public class QiniuServiceImpl implements QiniuService {

    @Resource
    private QiniuUtils qiniuUtils;

    @Resource
    private ImageUtil imageUtil;

    @Override
    public CommonResponse<String> upload(MultipartFile file) {
        if (file.isEmpty()) {
            return  success("文件为空！");
        }
        String fileName = imageUtil.resetFileName(file);
        try {
            FileInputStream uploadFile = (FileInputStream) file.getInputStream();
            String path = qiniuUtils.upload(uploadFile, fileName);
            return success(path);
        } catch (IOException e) {
            e.printStackTrace();
            return error(501,"上传失败");
        }
    }

    @Override
    public String getFile(String urlPath) {
        try {
            DownloadUrl url = new DownloadUrl("file.wuliqun.cn", false, "dog.png");
            url.setAttname("dog.png") // 配置 attname
                    .setFop("imageView2/2/w/320/h/480") // 配置 fop
                    .setStyle("thumb", "-", ""); // 配置 style
            // 带有效期
            long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
            long deadline = System.currentTimeMillis()/1000 + expireInSeconds;
            Auth auth = Auth.create("-vcYpDaBgOi9avF5652M1Q7Ebv532b7EuNe00M-W", "TAkK9VKiyhcwkJeeznLTUDsOPV7TvEbERaRqqIfX");
            return url.buildURL(auth, deadline);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

