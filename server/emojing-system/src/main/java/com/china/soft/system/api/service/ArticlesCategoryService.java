package com.china.soft.system.api.service;

import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.system.provider.dal.entity.ArticlesCategory;

import java.util.List;

/**
 * @Description: 文章分类接口层
 * @author MingYue
 * @Date 2023/10/10
 */
public interface ArticlesCategoryService {

    /**
     * 文章分类列表
     * @return
     */
    CommonResponse<List<ArticlesCategory>> list();

    /**
     * 查询通用分类
     * @return
     */
    CommonResponse<List<ArticlesCategory>> queryDefaultCategory();

    /**
     * 保存文章分类
     * @param dto
     * @return
     */
    CommonResponse<Boolean> save(ArticlesCategory dto);

}
