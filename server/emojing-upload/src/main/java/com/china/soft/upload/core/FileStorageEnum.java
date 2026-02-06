package com.china.soft.upload.core;

import cn.hutool.core.util.ArrayUtil;
import com.china.soft.upload.core.ftp.FtpFileClient;
import com.china.soft.upload.core.ftp.FtpFileClientConfig;
import com.china.soft.upload.core.local.LocalFileClient;
import com.china.soft.upload.core.local.LocalFileClientConfig;
import com.china.soft.upload.core.s3.S3FileClient;
import com.china.soft.upload.core.s3.S3FileClientConfig;
import com.china.soft.upload.core.sftp.SftpFileClient;
import com.china.soft.upload.core.sftp.SftpFileClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileStorageEnum {

    LOCAL(2,LocalFileClientConfig .class, LocalFileClient .class),
    FTP(3,FtpFileClientConfig .class, FtpFileClient .class),
    SFTP(4,SftpFileClientConfig.class, SftpFileClient .class),
    S3(5,S3FileClientConfig .class, S3FileClient .class),
    ;

    /**
     * 存储器
     */
    private final Integer storage;

    /**
     * 配置类
     */
    private final Class<? extends FileClientConfig> configClass;
    /**
     * 客户端类
     */
    private final Class<? extends FileClient> clientClass;

    public static FileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }

}
