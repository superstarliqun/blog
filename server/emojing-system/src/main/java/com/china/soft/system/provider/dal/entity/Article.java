package com.china.soft.system.provider.dal.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
/**
 * @Description: 文章表
 * @author MingYue
 * @Date 2023/8/24
 */
@Data
@TableName("t_articles")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 编号
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
    * 文章标题
    */
    private String title;

    /**
     * 摘要
     */
    private String summary;


    /**
     * 内容标记
     */
    private String content;

    /**
     * 分类
     */
    private Integer category;

    /**
     * 标签
     */
    private String tags;

    /**
    * 观看数
    */
    private Integer views;

    /**
    * 回复
    */
    private Integer comments;

    /**
    * 点赞
    */
    private Integer likes;

    /**
     * 作者编号
     */
    private Integer authorId;

    /**
    * 创建人
    */
    private String createBy;

    /**
    * 创建标记
    */
    private LocalDateTime createTime;

    /**
     * 创建标记
     */
    private LocalDateTime updateTime;

    /**
    * 删除标志
    */
    @TableLogic
    private Integer isDeleted;

    /** 文章类型  */
    @TableField(exist = false)
    private String categoryName;

    /** 文章标签  */
    @TableField(exist = false)
    private List<String> tagsList;
}