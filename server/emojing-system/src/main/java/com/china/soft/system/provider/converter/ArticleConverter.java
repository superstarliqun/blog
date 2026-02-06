package com.china.soft.system.provider.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.dto.a.ArticleDto;
import com.china.soft.system.provider.dal.entity.Article;
import org.mapstruct.Mapper;

import java.util.List;
/**
 * @Description: 文章转化工具类
 * @author MingYue
 * @Date 2023/9/22
 */
@Mapper(componentModel = "spring")
public interface ArticleConverter {

    public Page<ArticleDto> entityToDto(IPage<Article> parkIPage);

    public ArticleDto entityToDto(Article dto);

    public List<ArticleDto> entityToDto(List<Article> list);

    public Article dtoToEntity(ArticleDto dto);

}
