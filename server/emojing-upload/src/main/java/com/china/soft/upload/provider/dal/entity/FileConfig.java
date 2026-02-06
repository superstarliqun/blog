package com.china.soft.upload.provider.dal.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.china.soft.commons.utils.PageParam;
import com.china.soft.upload.core.FileClientConfig;
import com.china.soft.upload.core.ftp.FtpFileClientConfig;
import com.china.soft.upload.core.local.LocalFileClientConfig;
import com.china.soft.upload.core.s3.S3FileClientConfig;
import com.china.soft.upload.core.sftp.SftpFileClientConfig;
import com.china.soft.upload.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;

/**
 * 文件上传配置表
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sys_file_config", autoResultMap = true)
public class FileConfig extends PageParam {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 存储类型
     */
    @TableField(value = "storage_type")
    private Integer storageType;

    /**
     * 存储服务的访问端点
     */
    @TableField(value = "endpoint")
    private String endpoint;

    /**
     * 访问密匙
     */
    @TableField(value = "access_key")
    private String accessKey;

    /**
     * 密匙
     */
    @TableField(value = "secret_key")
    private String secretKey;

    /**
     * 存储桶
     */
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 区域
     */
    @TableField(value = "region")
    private String region;

    /**
     * 是否默认存储配置
     */
    @TableField(value = "is_default")
    private String isDefault;

    /**
     * 是否有效或启用
     */
    @TableField(value = "`status`")
    private String status;

    /**
     * 记录创建时间
     */
    @TableField(value = "created_at")
    private String createdAt;

    /**
     * 记录更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 删除标识
     */
    @TableField(value = "deleted")
    @JsonIgnore
    private Integer deleted;


    @TableField(value = "config", typeHandler = FileClientConfigTypeHandler.class)
    private FileClientConfig config;

    public static class FileClientConfigTypeHandler extends AbstractJsonTypeHandler<Object> {
        @Override
        public Object parse(String json) {
            FileClientConfig config = JsonUtils.parseObjectQuietly(json, new TypeReference<FileClientConfig>() {});
            if (config != null) {
                return config;
            }
            // 兼容老版本的包路径
            String className = JsonUtils.parseObject(json, "@class", String.class);
            className = StrUtil.subAfter(className, ".", true);
            switch (className) {
                case "FtpFileClientConfig":
                    return JsonUtils.parseObject2(json, FtpFileClientConfig.class);
                case "LocalFileClientConfig":
                    return JsonUtils.parseObject2(json, LocalFileClientConfig.class);
                case "SftpFileClientConfig":
                    return JsonUtils.parseObject2(json, SftpFileClientConfig.class);
                case "S3FileClientConfig":
                    return JsonUtils.parseObject2(json, S3FileClientConfig.class);
                default:
                    throw new IllegalArgumentException("未知的 FileClientConfig 类型：" + json);
            }
        }

        @Override
        public String toJson(Object obj) {
            return JsonUtils.toJsonString(obj);
        }

    }

}