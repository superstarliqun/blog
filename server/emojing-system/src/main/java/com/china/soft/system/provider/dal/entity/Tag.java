package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: 文章标签实体类
 * @author MingYue
 * @Date 2023/10/10
 */
@Data
@TableName("t_tags")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
    * 标签名称
    */
    private String tagsName;

    /**
    * 排序
    */
    private Integer seq;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;

    /**
     * 标签所属文章数量
     */
    @TableField(exist = false)
    private Integer number;


    /**
     * 标签所属的文章Id
     */
    @TableField(exist = false)
    private Integer postId;

}