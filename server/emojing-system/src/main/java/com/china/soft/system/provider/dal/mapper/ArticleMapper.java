package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.dto.a.ArticleDto;
import com.china.soft.system.provider.dal.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 文章dao层
 * @author MingYue
 * @Date 2023/10/10
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    Article selectByPrimaryKey(Integer id);

    /**
     * 查询文章首页列表
     * @param page 分页参数
     * @param entity 查询参数
     */
    IPage<Article> selectPageHome(@Param("page") Page<Article> page, @Param("param") Article entity);

    Long selectPageHome_COUNT(@Param("param") Article entity);

    /**
     * 查询指定年份每天文章数量
     */
    List<HashMap<String,Object>> dailyCounts(ArticleDto dto);

    /**
     * 归档查询列表
     */
    List<Article> fromArchiveList(ArticleDto dto);
}
