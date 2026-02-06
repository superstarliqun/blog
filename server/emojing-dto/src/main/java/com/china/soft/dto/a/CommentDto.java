package com.china.soft.dto.a;

import com.china.soft.commons.utils.tree.TreeEntity;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class CommentDto extends TreeEntity {

    private Long id;

    /** 关联博客文章ID */
    private Long postId;

    /** 父评论ID（0表示一级评论，>0表示回复） */
    private Long parentId;

    /** 评论内容  */
    private String content;

    /** 评论者昵称（匿名用户必填） */
    private String nickname;

    /** 评论者邮箱（可选） */
    private String email;

    /** 网址 */
    private String site;

    /** 头像 */
    private String avatar;

    /** 评论IP地址 */
    private String ipAddress;

    /** 评论状态：1-已审核 2-待审核 3-已删除 4-垃圾评论 */
    private Byte status;

    /** 评论创建时间 */
    private LocalDateTime createTime;

    /** 评论更新时间 */
    private LocalDateTime updateTime;

    private String userAgent;

    /** 总数 */
    private Integer total;

}
