package com.china.soft.system.api.service;

import com.china.soft.dto.a.CommentDto;
import com.china.soft.system.provider.dal.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentService extends IService<Comment>{


    int insertSelective(Comment record);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    /**
     * 通过文档的postId查询评论
     */
    List<CommentDto> tree(Integer postId);

}
