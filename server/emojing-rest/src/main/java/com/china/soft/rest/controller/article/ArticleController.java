package com.china.soft.rest.controller.article;

import com.china.soft.commons.utils.PageResult;
import com.china.soft.commons.utils.request.IDRequest;
import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.dto.a.ArticleDto;
import com.china.soft.system.api.service.ArticleService;
import com.china.soft.system.api.service.ArticlesCategoryService;
import com.china.soft.system.api.service.ArticlesTagsService;
import com.china.soft.system.provider.dal.entity.Article;
import com.china.soft.system.provider.dal.entity.ArticlesCategory;
import com.china.soft.system.provider.dal.entity.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.List;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * description 文章管理前端控制器
 * @author  MingYue
 * @version 1.0
 * @since   2023/9/15
 */
@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticlesCategoryService categoryService;

    @Resource
    private ArticlesTagsService tagsService;

    @GetMapping("/list/{num}/{size}")
    @PermitAll
    public CommonResponse<PageResult<Article>> myPage(@PathVariable Integer num, @PathVariable Integer size, ArticleDto dto){
        return articleService.myPage(num,size,dto);
    }

    /**
     * 保存文章
     * @param entity 文件对象保存参数
     */
    @PostMapping(value = "/save")
    private CommonResponse<Boolean> save(@RequestBody Article entity){
        return articleService.save(entity);
    }

    /**
     * 删除文章
     * @param idRequest 删除Id
     */
    @PostMapping(value = "/remove")
    private CommonResponse<Boolean> remove(@RequestBody IDRequest idRequest){
        return articleService.remove(idRequest.getId());
    }

    /**
     * 文章分类列表
     */
    @GetMapping(value = "/categoryList")
    @PermitAll
    private CommonResponse<List<ArticlesCategory>> getCategoryList(){
        return categoryService.list();
    }

    /**
     * 保存文章分类
     */
    @PostMapping(value = "/saveCategory")
    public CommonResponse<Boolean> saveCategory(@RequestBody ArticlesCategory dto){
        return categoryService.save(dto);
    }

    /**
     * 文章标签列表
     */
    @GetMapping(value = "/tagsList")
    @PermitAll
    private CommonResponse<List<Tag>> getTagsList(){
        return tagsService.list();
    }

    /**
     * 保存文章标签
     */
    @PostMapping(value = "/saveTags")
    public CommonResponse<Boolean> saveTags(@RequestBody Tag dto){
        return tagsService.save(dto);
    }

    @GetMapping(value = "/test")
    @PermitAll
    public CommonResponse<Boolean> test(){
        try {
            tagsService.generateImage("中华文明");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return success(true);
    }




}
