package com.china.soft.rest.controller.system;

import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.upload.api.service.QiniuService;
import com.qiniu.common.QiniuException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * @author niuben
 */
@Api(tags = "七牛云接口")
@Slf4j
@RestController
@RequestMapping("/qiniu")
public class QiniuController {

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation("上传图片")
    @PostMapping(value = "/upload")
    @PermitAll
    public CommonResponse<String> upload(@RequestParam("file") MultipartFile file) {
        return qiniuService.upload(file);
    }

    @ApiOperation("获取图片地址")
    @GetMapping("/getFile")
    @PermitAll
    public CommonResponse<String> getFile(@RequestParam("url")String url) throws QiniuException {
        String  urlResult= qiniuService.getFile(url);
        return success(urlResult);
    }
}

