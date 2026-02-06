package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: 文章分类实体类
 * @author MingYue
 * @Date 2023/10/10
 */
@Data
@TableName("t_articles_category")
public class ArticlesCategory {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 父级编号 */
    private Integer pid;

    /** 分类名称 */
    private String categoryName;

    /** 排序 */
    private String seq;

    /** 用户编号 */
    private String userId;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 分类数量 */
    @TableField(exist = false)
    private Integer number;

}

