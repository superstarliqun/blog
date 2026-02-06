package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 文章用户关联表
 */
@Data
@AllArgsConstructor
@TableName(value = "blog_db.t_view_user")
public class ViewUser implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 文章ID
     */
    @TableField(value = "articles_id")
    private Integer articlesId;

    /**
     * 网络IP
     */
    @TableField(value = "ip_address")
    private String ipAddress;

    private static final long serialVersionUID = 1L;
}