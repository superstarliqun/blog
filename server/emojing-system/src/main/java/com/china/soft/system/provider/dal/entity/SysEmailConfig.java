package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.china.soft.commons.utils.PageParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 邮箱配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@TableName(value = "online_blog_db.sys_email_config")
public class SysEmailConfig extends PageParam {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * SMTP服务器域名
     */
    @TableField(value = "`host`")
    private String host;

    /**
     * SMTP服务端口
     */
    @TableField(value = "port")
    private Integer port;

    /**
     * 是否需要用户名密码验证
     */
    @TableField(value = "auth")
    private Boolean auth;

    /**
     * 发送方，遵循RFC-822标准
     */
    @TableField(value = "`from`")
    private String from;

    /**
     * 用户名
     */
    @TableField(value = "`user`")
    private String user;

    /**
     * 密码
     */
    @TableField(value = "pass")
    private String pass;

    /**
     * 是否默认 0否 1是
     */
    @TableField(value = "is_default")
    private Integer isDefault;


    @TableLogic
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}