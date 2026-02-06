package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 邮件发送记录表
 */
@Data
@AllArgsConstructor
@TableName(value = "online_blog_db.sys_email_send_log")
public class EmailSendLog implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为null")
    private Integer id;

    /**
     * 收件人邮箱地址
     */
    @TableField(value = "recipient_email")
    @Size(max = 255, message = "收件人邮箱地址最大长度要小于 255")
    @NotBlank(message = "收件人邮箱地址不能为空")
    private String recipientEmail;

    /**
     * 邮件主题
     */
    @TableField(value = "subject")
    @Size(max = 255, message = "邮件主题最大长度要小于 255")
    @NotBlank(message = "邮件主题不能为空")
    private String subject;

    /**
     * 邮件内容
     */
    @TableField(value = "body")
    private String body;

    /**
     * 发送状态
     */
    @TableField(value = "`status`")
    @NotNull(message = "发送状态不能为null")
    private Object status;

    /**
     * 错误信息
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /** 发送时间 */
    @TableField(value = "sent_time")
    private LocalDateTime sentTime;

    /** 邮件默认邮箱发送源 */
    @TableField(exist = false)
    private Integer configId;

    private static final long serialVersionUID = 1L;
}