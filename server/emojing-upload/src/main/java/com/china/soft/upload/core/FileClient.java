package com.china.soft.upload.core;

import com.china.soft.upload.core.s3.FilePresignedUrlRespDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * description 文件客户端，对代码段暴露的使用方法
 * @author  MingYue
 * @version 1.0
 * @since   2025/4/2
 */
public interface FileClient {

    Long getId();

    /** 文件预检 */
    String check(String fileHash, Long fileSize, String fileName);
    /** 文件直传 */
    String direct( MultipartFile file, String fileHash, String fileName);
    /** 文件分片上传 */
    String chunk(String uploadId, Integer chunkIndex, MultipartFile fileChunk, Integer totalChunks, String chunkHash);
    /** 文件合并 */
    String merge(String uploadId, String fileHash,String fileName);
    /** 文件分片清除 */
    String cleanup(String uploadId);

    String upload(byte[] content, String path, String type) throws Exception;

    void delete(String path) throws Exception;

    byte[] getContent(String path) throws Exception;

    FilePresignedUrlRespDTO getPresignedObjectUrl(String path) throws Exception;


}
