package com.china.soft.rest.controller.system;

import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.es.api.service.GlobalSearchService;
import com.china.soft.es.provider.dal.entity.ArticleDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * 文章全局搜索前端控制器
 * @author  MingYue
 * @version 1.0
 * @since   2025/3/4
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class GlobalSearchController {

    @Autowired
    GlobalSearchService globalSearchService;

    /**
     * 全局搜索文章，附带关键字
     * @param keywords 关键字搜索
     */
    @GetMapping("/findHighlight")
    @PermitAll
    public CommonResponse<List<ArticleDocument>> findHighlight(String keywords){
        List<ArticleDocument> highlight = globalSearchService.queryHighlight(keywords, 0, 100);
        return success(highlight);
    }

    /**
     * 分页查询列表数据
     * @param keywords 查询关键字
     * @return 符合条件结果
     */
    @GetMapping("/list/{num}/{size}")
    public CommonResponse<List<ArticleDocument>> list(String keywords, @PathVariable int num, @PathVariable int size) {
        List<ArticleDocument> list = globalSearchService.findList(keywords, num, size);
        return success(list);
    }

    /**
     * 保存文章
     * @param entity
     */
    @PostMapping("/save")
    public CommonResponse<Boolean> Save(@RequestBody ArticleDocument entity) {
        globalSearchService.save(entity);
        return success(true);
    }

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public CommonResponse<ArticleDocument> getBookById(@PathVariable String id) {
        return success(globalSearchService.findById(id));
    }

    /**
     * 修改文章
     * @param entity
     */
    @PostMapping("/updateById")
    public CommonResponse<Boolean> updateById(@RequestBody ArticleDocument entity) {
        globalSearchService.updateById(entity);
        return success(true);
    }



}
