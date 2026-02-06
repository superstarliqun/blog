package com.china.soft.dto.a;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
/**
 * @Description: 文章DTO试题列
 * @author MingYue
 * @Date 2023/9/22
 */
@Data
@ToString
public class ArticleDto {

    /**
     * 编号
     */
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
     * 查询年份
     */
    private String searchYear;

    /**
     * 查询天
     */
    private String searchDate;
}
