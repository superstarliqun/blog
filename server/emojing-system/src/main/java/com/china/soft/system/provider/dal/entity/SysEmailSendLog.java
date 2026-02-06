package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 邮件发送记录表
 */
@Data
@AllArgsConstructor
@TableName(value = "online_blog_db.sys_email_send_log")
public class SysEmailSendLog implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 收件人邮箱地址
     */
    @TableField(value = "recipient_email")
    private String recipientEmail;

    /**
     * 邮件主题
     */
    @TableField(value = "subject")
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
    private Object status;

    /**
     * 错误信息
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /**
     * 发送时间
     */
    @TableField(value = "sent_time")
    private LocalDateTime sentTime;

    private static final long serialVersionUID = 1L;
}