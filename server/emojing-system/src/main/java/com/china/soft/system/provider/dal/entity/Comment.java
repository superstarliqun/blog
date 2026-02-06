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
import lombok.NoArgsConstructor;

/**
 * 博客评论表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "online_blog_db.t_comment")

public class Comment implements Serializable {
    /**
     * 评论ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "评论ID（主键）不能为null")
    private Long id;

    /**
     * 关联博客文章ID
     */
    @TableField(value = "post_id")
    @NotNull(message = "关联博客文章ID不能为null")
    private Long postId;

    /**
     * 父评论ID（0表示一级评论，>0表示回复）
     */
    @TableField(value = "parent_id")
    @NotNull(message = "父评论ID（0表示一级评论，>0表示回复）不能为null")
    private Long parentId;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 评论者昵称（匿名用户必填）
     */
    @TableField(value = "nickname")
    @Size(max = 50,message = "评论者昵称（匿名用户必填）最大长度要小于 50")
    @NotBlank(message = "评论者昵称（匿名用户必填）不能为空")
    private String nickname;

    /**
     * 评论者邮箱（可选）
     */
    @TableField(value = "email")
    @Size(max = 100,message = "评论者邮箱（可选）最大长度要小于 100")
    private String email;


    /**
     * 网址
     */
    @TableField(value = "site")
    @Size(max = 100,message = "网址 最大长度小于 100")
    private String site;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 评论IP地址
     */
    @TableField(value = "ip_address")
    @Size(max = 50,message = "评论IP地址最大长度要小于 50")
    private String ipAddress;

    /**
     * 评论状态：1-已审核 2-待审核 3-已删除 4-垃圾评论
     */
    @TableField(value = "`status`")
    @NotNull(message = "评论状态：0显示 1不显示")
    private Long status;

    /**
     * 评论创建时间
     */
    @TableField(value = "create_time")
    @NotNull(message = "评论创建时间不能为null")
    private LocalDateTime createTime;

    @TableField(value = "user_agent")
    @Size(max = 255,message = "最大长度要小于 255")
    private String userAgent;

    private static final long serialVersionUID = 1L;
}