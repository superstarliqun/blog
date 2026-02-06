package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户长token管理
 */
@Data
@AllArgsConstructor
@TableName(value = "online_blog_db.user_token_info")
public class UserTokenInfo implements Serializable {

    /**
     * JWT-ID唯一标识
     */
    @TableField(value = "jti")
    @Size(max = 255,message = "JWT-ID唯一标识最大长度要小于 255")
    private String jti;

    /**
     * 编号
     */
    @TableId(value = "id")
    @NotNull(message = "sys_authorization_id 表示")
    private Integer id;

    /**
     * 关联的用户 ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * Token 的过期时间
     */
    @TableField(value = "expiration_time")
    private LocalDateTime expirationTime;

    /**
     * 是否有效
     */
    @TableField(value = "is_valid")
    private Integer isValid;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}