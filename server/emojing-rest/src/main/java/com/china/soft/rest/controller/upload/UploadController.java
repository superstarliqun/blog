package com.china.soft.rest.controller.upload;

import cn.hutool.core.io.IoUtil;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.system.provider.service.S3FileServiceImpl;
import com.china.soft.upload.api.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * @Description: 文件操作前端控制器
 * @author MingYue
 * @Date 2023/10/9
 */
@RestController
@RequestMapping("/minio")
public class UploadController {

    @Autowired
    private S3FileServiceImpl s3FileService;

    @Resource
    private FileService fileService;

    @Value("${s3.domain}")
    private String domain;


    @PostMapping("/test")
    @PermitAll
    public CommonResponse<Boolean> test(MultipartFile file){
        fileService.test(file);
        return success(true);
    }


    @PostMapping("/upload")
    public CommonResponse upload(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        Optional<Bucket> articleImage = s3FileService.getBucket("image");
        if(articleImage.equals(Optional.empty())){
            s3FileService.createBucket("image");
        }
        String fileName = calculateFileHash(file) + file.getOriginalFilename();
        s3FileService.putObject("image",fileName,file.getInputStream(), file.getContentType());
        S3Object s3Object = s3FileService.getObject("image", fileName);
        String url = s3Object.getObjectContent().getHttpRequest().getURI().getPath().toString();
        url = domain +url;
        return success(url);
    }

    /**
     * 转化文件唯一标识
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String calculateFileHash(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;
        try {
            byte[] bytes = file.getBytes();
            md.update(bytes);
        } catch (IOException e) {
            // 处理文件读取异常
            e.printStackTrace();
            throw e;
        }
        byte[] hashBytes = md.digest();
        StringBuilder hexString = new StringBuilder();

        for (byte hashByte : hashBytes) {
            hexString.append(String.format("%02x", hashByte));
        }

        return hexString.toString();
    }
}
