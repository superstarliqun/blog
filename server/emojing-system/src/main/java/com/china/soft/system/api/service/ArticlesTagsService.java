package com.china.soft.system.api.service;

import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.system.provider.dal.entity.Tag;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * @Description: 文章标签接口层
 * @author MingYue
 * @Date 2023/10/10
 */
public interface ArticlesTagsService {

    CommonResponse<List<Tag>> list();

    CommonResponse<Boolean> save(Tag dto);

    int deleteBatchIds(Integer id);

    void generateImage(String name) throws IOException, FontFormatException;

    // 删除文章关系标签
    int fromRelationDelete(Integer postId);

    // 根据文章ID查询文章关系
    List<Tag> fromRelationSelectByPostId(Integer postId);

}
