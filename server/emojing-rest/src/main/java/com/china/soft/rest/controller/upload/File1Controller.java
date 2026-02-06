package com.china.soft.rest.controller.upload;

import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.upload.api.service.FileAsyncTaskService;
import com.china.soft.upload.api.service.FileConfigService;
import com.china.soft.upload.core.FileClient;
import com.china.soft.upload.utils.MultipartFileHashUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;


import java.nio.file.Files;
import java.nio.file.Paths;

import static com.china.soft.commons.utils.response.CommonResponse.success;

@RestController
@RequestMapping("/file")
public class File1Controller {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileAsyncTaskService fileAsyncTaskService;

    /**
     * 文件上传前置检查
     * @param file
     * @return
     */
    @PostMapping("/check")
    @PermitAll
    public CommonResponse<String> check(@RequestParam("file") MultipartFile file) {
           FileClient fileClient = fileConfigService.getMasterFileClient();
           try {
               String fileSha256 = MultipartFileHashUtil.getFileSha256(file);
               fileClient.check(fileSha256,file.getSize(),file.getOriginalFilename());
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
           return success("");
    }

    /**
     * 文件直接上传
     * @param file
     * @return
     */
    @PostMapping("/upload/direct")
    @PermitAll
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse<String> direct(@RequestParam("file") MultipartFile file) throws Exception {
        FileClient fileClient = fileConfigService.getMasterFileClient();
        String path = "/" + file.getOriginalFilename();
        String type = Files.probeContentType(Paths.get(path));
//        fileClient.upload(IoUtil.readBytes(file.getInputStream()),path,type);
        fileAsyncTaskService.waitUpload(fileClient,file.getBytes(),path,type);
        return success("");
    }

    /**
     * 文件分片上传
     * @param file
     * @return
     */
    @PostMapping("/chunk")
    @PermitAll
    public CommonResponse<String> chunk(@RequestParam("file") MultipartFile file) {
        FileClient fileClient = fileConfigService.getMasterFileClient();
        fileClient.chunk(null,null,null,null,null);
        return success("");
    }

    /**
     * 文件合并
     * @param file
     * @return
     */
    @PostMapping("/merge")
    @PermitAll
    public CommonResponse<String> merge(@RequestParam("file") MultipartFile file) {
        FileClient fileClient = fileConfigService.getMasterFileClient();
        fileClient.merge(null,null,null);
        return success("");
    }

    /**
     * 文件片段清除
     * @param file
     * @return
     */
    @PostMapping("/cleanup")
    @PermitAll
    public CommonResponse<String> cleanup(@RequestParam("file") MultipartFile file) {
        FileClient fileClient = fileConfigService.getMasterFileClient();
        fileClient.cleanup(null);
        return success("");
    }


}
