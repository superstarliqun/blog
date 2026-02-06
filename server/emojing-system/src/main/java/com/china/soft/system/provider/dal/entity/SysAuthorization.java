package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description: 用户授权信息
 * @author MingYue
 * @Date 2023/10/12
 */
@Data
@TableName(value = "sys_authorization")
public class SysAuthorization implements Serializable {
    /**
     * 主键key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 登录类型(手机号/邮箱) 或第三方应用名称 (微信/微博等)
     */
    @TableField(value = "identity_type")
    private String identityType;

    /**
     * 手机号/邮箱/第三方的唯一标识
     */
    @TableField(value = "identifier")
    private String identifier;

    /**
     * 密码凭证 (自建账号的保存密码, 第三方的保存 token)
     */
    @TableField(value = "credential")
    private String credential;

    /**
     * 是否已验证
     */
    @TableField(value = "verified")
    private String verified;

    /**
     * 绑定时间
     */
    @TableField(value = "bind_time")
    private LocalDateTime bindTime;

    /**
     * 绑定ip地址
     */
    @TableField(value = "bing_addr")
    private String bingAddr;

    /**
     * 上次登录时间
     */
    @TableField(value = "time_last_login")
    private LocalDateTime timeLastLogin;

    /**
     * 上次登录ip地址
     */
    @TableField(value = "time_last_addr")
    private String timeLastAddr;

    private static final long serialVersionUID = 1L;
}