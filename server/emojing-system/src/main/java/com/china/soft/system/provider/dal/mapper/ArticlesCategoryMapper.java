package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.ArticlesCategory;

import java.util.List;

/**
 * @Description: 文章分类dao层
 * @author MingYue
 * @Date 2023/10/10
 */
public interface ArticlesCategoryMapper extends BaseMapper<ArticlesCategory> {

    List<ArticlesCategory> selectListGroup();

}
