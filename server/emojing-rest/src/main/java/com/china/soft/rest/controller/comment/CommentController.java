package com.china.soft.rest.controller.comment;
import com.china.soft.dto.a.CommentDto;
import com.china.soft.system.provider.dal.entity.Comment;
import com.china.soft.system.api.service.CommentService;
import org.springframework.web.bind.annotation.*;
import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
* 博客评论表前端控制器
* Mon Dec 29 15:17:52 CST 2025
* @author MingYue
*/
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
    * 通过文档的postId查询评论
    */
    @GetMapping("{id}")
    @PermitAll
    public CommonResponse<List<CommentDto>> tree(@PathVariable("id") Integer postId) {
        return success(commentService.tree(postId));
    }

    /**
    * 新增数据
    *
    * @param comment 实体
    * @return 新增结果
    */
    @PostMapping(value = "/add")
    @PermitAll
    public CommonResponse<Comment> add(@RequestBody Comment comment, @RequestHeader("User-Agent") String userAgent) {
        comment.setUserAgent(userAgent);
        commentService.insertSelective(comment);
        return success(comment);
    }

    /**
    * 删除数据
    * @param id 主键
    * @return 删除是否成功
    */
    @PostMapping(value = "/delete/{id}")
    @PermitAll
    public CommonResponse< Boolean> deleteById(@PathVariable("id") Integer id) {
        commentService.removeById(id);
        return success(true);
    }
}
