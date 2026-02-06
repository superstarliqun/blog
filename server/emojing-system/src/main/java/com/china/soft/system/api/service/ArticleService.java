package com.china.soft.system.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.commons.utils.PageResult;
import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.dto.a.ArticleDto;
import com.china.soft.system.provider.dal.entity.Article;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 文章接口层
 * @author MingYue
 * @Date 2023/10/10
 */
public interface ArticleService {

    CommonResponse<PageResult<Article>> page(Integer num, Integer size, ArticleDto dto);

    CommonResponse<PageResult<Article>> myPage(Integer num,Integer size, ArticleDto dto);

    CommonResponse<Article> get(Integer id);

    CommonResponse<Boolean> save(Article entity);

    CommonResponse<Boolean> remove(Integer id);

    /**
     * 查询指定年份每天文章数量
     */
    List<HashMap<String,Object>> dailyCounts(ArticleDto dto);

    /**
     * 归档查询列表
     */
    List<Article> fromArchiveList(ArticleDto dto);
}
