package com.china.soft.rest.controller.article;

import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.dto.a.ArticleDto;
import com.china.soft.system.api.service.ArticleService;
import com.china.soft.system.api.service.ArticlesTagsService;
import com.china.soft.system.provider.dal.entity.Article;
import com.china.soft.system.provider.dal.entity.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.china.soft.commons.utils.response.CommonResponse.success;

@RestController
@RequestMapping("/archive")
public class ArchiveController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticlesTagsService tagsService;

    /**
     * 查询指定年份每天文章数量
     */
    @GetMapping("/posts/calendar/daily-counts")
    @PermitAll
    public CommonResponse<List<HashMap<String,Object>>> dailyCounts(ArticleDto dto){
        return success(articleService.dailyCounts(dto));
    }

    /**
     * 查询归档列表
     */
    @GetMapping("/fromArchiveList")
    @PermitAll
    public CommonResponse<List<Article>> list(ArticleDto dto){
        List<Article> articles = articleService.fromArchiveList(dto);
        articles.forEach(item -> {
            List<Tag> tags = tagsService.fromRelationSelectByPostId(item.getId());
            List<String> collect = tags.stream().map(Tag::getTagsName).collect(Collectors.toList());
            item.setTagsList(collect);
        });
        return success(articles);
    }

}
