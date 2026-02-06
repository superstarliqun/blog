package com.china.soft.rest.controller;
import com.china.soft.system.provider.dal.entity.UserTokenInfo;
import com.china.soft.system.api.service.UserTokenInfoService;
import org.springframework.web.bind.annotation.*;
import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
* 用户长token管理前端控制器
* Mon Dec 15 16:26:13 CST 2025
* @author MingYue
*/
@RestController
@RequestMapping("/UserTokenInfo")
public class UserTokenInfoController {

    /**  服务对象 */
    @Autowired
    private UserTokenInfoService userTokenInfoService;

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @GetMapping("{id}")
    public CommonResponse<UserTokenInfo> queryById(@PathVariable("id") Integer id) {
        return success(userTokenInfoService.getById(id));
    }

    /**
    * 新增数据
    *
    * @param userTokenInfo 实体
    * @return 新增结果
    */
    @PostMapping(value = "/add")
    public CommonResponse< Boolean> add(UserTokenInfo userTokenInfo) {
        userTokenInfoService.insertSelective(userTokenInfo);
        return success(true);
    }

    /**
    * 编辑数据
    *
    * @param userTokenInfo 实体
    * @return 编辑结果
    */
    @PutMapping(value = "/update")
    public CommonResponse< Boolean> edit(UserTokenInfo userTokenInfo) {
        userTokenInfoService.updateByPrimaryKeySelective(userTokenInfo);
        return success(true);
    }

    /**
    * 删除数据
    * @param id 主键
    * @return 删除是否成功
    */
    @PostMapping(value = "/delete/{id}")
    public CommonResponse< Boolean> deleteById(@PathVariable("id") Integer id) {
        userTokenInfoService.removeById(id);
        return success(true);
    }
}
