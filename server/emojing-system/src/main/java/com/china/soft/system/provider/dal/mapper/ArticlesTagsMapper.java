package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.Tag;

import java.util.List;

/**
 * @Description: 文章标签dao层
 * @author MingYue
 * @Date 2023/10/10
 */
public interface ArticlesTagsMapper extends BaseMapper<Tag> {

    List<Tag> list();

    Tag selectByName(String name);

    // 文章与标签关系表 新增
    int fromRelationInsert(Tag entity);

    // 文章与标签关系表 删除
    int fromRelationDelete(Integer postId);

    // 查找没有被使用的标签
    List<Tag> unused();

    // 查询当前文档挂载标签
    List<Tag> fromRelationSelectByPostId(Integer postId);
}
