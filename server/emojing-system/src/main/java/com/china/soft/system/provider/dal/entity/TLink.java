package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 友链表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "online_blog_db.t_link")
public class TLink implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "编号不能为null")
    private Integer id;

    /**
     * 网站名称
     */
    @TableField(value = "`name`")
    @Size(max = 255,message = "网站名称最大长度要小于 255")
    private String name;

    /**
     * 网站备注
     */
    @TableField(value = "description")
    @Size(max = 255,message = "网站备注最大长度要小于 255")
    private String description;

    /**
     * 网站头像
     */
    @TableField(value = "avatar")
    @Size(max = 255,message = "网站头像最大长度要小于 255")
    private String avatar;

    /**
     * 网站地址
     */
    @TableField(value = "url")
    @Size(max = 255,message = "网站地址最大长度要小于 255")
    private String url;

    /**
     * 审核状态 0待审核 1审核通过 2审核驳回 3禁用
     */
    @TableField(value = "`status`")
    @Size(max = 255,message = "审核状态 0待审核 1审核通过 2审核驳回 3禁用最大长度要小于 255")
    private String status;

    /**
     * 类型 0友链  1工具
     */
    @TableField(value = "`type`")
    @Size(max = 255,message = "类型 0友链  1工具最大长度要小于 255")
    private String type;


    @TableField(value = "`email`")
    @Size(max = 255)
    private String email;

    private static final long serialVersionUID = 1L;
}