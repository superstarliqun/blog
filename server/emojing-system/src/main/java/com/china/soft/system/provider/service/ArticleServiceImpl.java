package com.china.soft.system.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.exception.BusinessException;
import com.china.soft.commons.utils.PageResult;
import com.china.soft.commons.utils.UserRequestUtil;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.dto.a.ArticleDto;
import com.china.soft.es.api.service.GlobalSearchService;
import com.china.soft.es.provider.dal.entity.ArticleDocument;
import com.china.soft.system.api.service.ArticleService;
import com.china.soft.system.api.service.ArticlesTagsService;
import com.china.soft.system.provider.converter.ArticleConverter;
import com.china.soft.system.provider.dal.entity.Article;
import com.china.soft.system.provider.dal.entity.Tag;
import com.china.soft.system.provider.dal.mapper.ArticleMapper;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * @Description: 文章管理
 * @author MingYue
 * @Date 2023/9/22
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired(required = false)
    private ArticleConverter articleConverter;

    @Resource
    private GlobalSearchService globalSearchService;

    @Resource
    private ArticlesTagsService tagsService;

    @Override
    public CommonResponse<PageResult<Article>> page(Integer num, Integer size, ArticleDto dto) {
        Article entity = articleConverter.dtoToEntity(dto);
        Page<Article> page = new Page<>(num, size);
        LoginUser loginUser = UserRequestUtil.getLoginUser();
        if(loginUser != null){
            entity.setAuthorId(loginUser.getUserId());
        }
        page.setSearchCount(false);
        IPage<Article> iPage = articleMapper.selectPageHome(page, entity);
        Long l = articleMapper.selectPageHome_COUNT(entity);
        return success(new PageResult<>(iPage.getRecords(), l));
    }

    @Override
    public CommonResponse myPage(Integer num, Integer size, ArticleDto dto) {
        LoginUser loginUser = UserRequestUtil.getLoginUser();
        if (loginUser !=null){
            dto.setAuthorId(loginUser.getUserId());
            return page(num, size, dto);
        }
        return success(new Page<Object>());
    }

    @Override
    public CommonResponse<Article> get(Integer id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        List<Tag> tags = tagsService.fromRelationSelectByPostId(id);
        List<String> collect = tags.stream().map(Tag::getTagsName).collect(Collectors.toList());
        article.setTagsList(collect);
        return success(article);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public CommonResponse<Boolean> save(Article entity) {
        // 替换过敏词汇
        try {
            String replace = SensitiveWordHelper.replace(entity.getContent());
            ArticleDocument document = new ArticleDocument();
            if( entity.getId() == null ){
                LoginUser loginUser = UserRequestUtil.getLoginUser();
                entity.setCreateBy(loginUser.getUsername());
                entity.setAuthorId(loginUser.getUserId());
                articleMapper.insert(entity);
                // 同时保存到es数据库中
                document.setTitle(entity.getTitle());
                document.setTags(entity.getTags());
                document.setSummary(entity.getSummary());
                document.setMysqlId(entity.getId().toString());
                document.setContent(entity.getContent());
                document.setContentText(entity.getContent());
                globalSearchService.save(document);
            }else{
                entity.setUpdateTime(LocalDateTime.now());
                articleMapper.updateById(entity);
                document.setTitle(entity.getTitle());
                document.setTags(entity.getTags());
                document.setSummary(entity.getSummary());
                document.setMysqlId(entity.getId().toString());
                document.setContent(entity.getContent());
                document.setContentText(entity.getContent());
                globalSearchService.updateById(document);
            }
            // @MingYue：处理文章标签
            tagsService.fromRelationDelete(entity.getId());
            for(String item:entity.getTagsList()){
                Tag tag = new Tag();
                tag.setPostId(entity.getId());
                tag.setTagsName(item);
                tag.setCreateTime(LocalDateTime.now());
                tagsService.save(tag);
            }
            // 删除所有没有关系的标签
             // TODO @MingYue：定时还要删除标签关系表
            tagsService.deleteBatchIds(entity.getId());
            return success(true);
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public CommonResponse<Boolean> remove(Integer id) {
        try {
            articleMapper.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return success(true);
    }

    /**
     * 查询指定年份每天文章数量
     */
    @Override
    public List<HashMap<String,Object>> dailyCounts(ArticleDto dto) {
        return articleMapper.dailyCounts(dto);
    }

    /**
     * 归档查询列表
     */
    @Override
    public List<Article> fromArchiveList(ArticleDto dto) {
        return articleMapper.fromArchiveList(dto);
    }
}
