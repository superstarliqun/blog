package com.china.soft.rest.controller.article;
import com.china.soft.system.api.service.ViewUserService;
import com.china.soft.system.provider.dal.entity.ViewUser;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

/**
* 文章用户关联表(blog_db.t_view_user)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/blog_db.t_view_user")
public class ViewUserController {
/**
* 服务对象
*/
    @Autowired
    private ViewUserService ViewUserService;

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @GetMapping("selectOne")
    public ViewUser selectOne(Integer id) {
    return ViewUserService.selectByPrimaryKey(id);
    }

}
