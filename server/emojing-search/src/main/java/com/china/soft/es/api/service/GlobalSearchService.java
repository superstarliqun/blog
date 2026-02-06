package com.china.soft.es.api.service;

import com.china.soft.es.provider.dal.entity.ArticleDocument;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GlobalSearchService {

    /**
     * 高亮搜素文章
     * @param keywords 搜索关键字
     * @param pageIndex 当前页
     * @param pageSize 总页数
     * @return 文章对象集合
     */
    List<ArticleDocument> queryHighlight(String keywords , Integer pageIndex, Integer pageSize);

    void save(ArticleDocument articles);

    ArticleDocument findById(String id);

    void deleteById(String id);

    void updateById(ArticleDocument ArticleDocument);

    List<ArticleDocument> findList(String keywords, Integer pageIndex, Integer pageSize);

}
