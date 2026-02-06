package com.china.soft.system.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.utils.UserRequestUtil;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.system.api.service.ArticlesCategoryService;
import com.china.soft.system.provider.dal.entity.ArticlesCategory;
import com.china.soft.system.provider.dal.mapper.ArticlesCategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * @Description: 文章分类持久层
 * @author MingYue
 * @Date 2023/10/10
 */
@Service
@Slf4j
public class ArticlesCategoryServiceImpl implements ArticlesCategoryService {

    @Autowired
    private ArticlesCategoryMapper categoryMapper;

    @Override
    public CommonResponse<List<ArticlesCategory>> list() {
        LambdaQueryWrapper<ArticlesCategory> wrapper = new LambdaQueryWrapper<>();
        LoginUser loginUser = UserRequestUtil.getLoginUser();
        wrapper.eq(ArticlesCategory::getUserId,loginUser.getUserId());
        List<ArticlesCategory> list = categoryMapper.selectList(wrapper);
        return success(list);
    }


    @Override
    public CommonResponse<List<ArticlesCategory>> queryDefaultCategory() {
        List<ArticlesCategory> list = categoryMapper.selectListGroup();
        return success(list);
    }

    @Override
    public CommonResponse<Boolean> save(ArticlesCategory dto) {
        if (dto.getId() == null) {
            LoginUser loginUser = UserRequestUtil.getLoginUser();
            dto.setUserId(loginUser.getUserId().toString());
            categoryMapper.insert(dto);
        }else{
            categoryMapper.updateById(dto);
        }
        return success(true);
    }
}
