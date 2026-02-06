package com.china.soft.rest.controller.upload;
import com.china.soft.commons.utils.PageResult;
import com.china.soft.upload.provider.dal.entity.FileConfig;
import com.china.soft.upload.provider.dal.entity.SysFileMd5;
import com.china.soft.upload.api.service.FileMd5Service;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * description 文件上传唯一标识表前端控制器
 * @author  MingYue
 * @version 1.0
 * @since   2025/4/1
 */
@RestController
@RequestMapping("/SysFileMd5")
public class FileMd5Controller {

    /**  服务对象 */
    @Autowired
    private FileMd5Service fileMd5Service;

    @GetMapping("/page")
    @Operation(summary = "文件上传配置分页列表查询")
    public CommonResponse<PageResult<SysFileMd5>> getPage(@Valid SysFileMd5 reqV0) {
        return success(fileMd5Service.getPageList(reqV0));
    }

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @GetMapping("{id}")
    public CommonResponse<SysFileMd5> queryById(@PathVariable("id") Integer id) {
        return success(fileMd5Service.getById(id));
    }

    /**
    * 新增数据
    *
    * @param sysFileMd5 实体
    * @return 新增结果
    */
    @PostMapping(value = "/add")
    public CommonResponse< Boolean> add(SysFileMd5 sysFileMd5) {
        fileMd5Service.insertSelective(sysFileMd5);
        return success(true);
    }

    /**
    * 编辑数据
    *
    * @param sysFileMd5 实体
    * @return 编辑结果
    */
    @PutMapping(value = "/update")
    public CommonResponse< Boolean> edit(SysFileMd5 sysFileMd5) {
        fileMd5Service.updateByPrimaryKeySelective(sysFileMd5);
        return success(true);
    }

    /**
    * 删除数据
    * @param id 主键
    * @return 删除是否成功
    */
    @PostMapping(value = "/delete/{id}")
    public CommonResponse< Boolean> deleteById(@PathVariable("id") Integer id) {
        fileMd5Service.removeById(id);
        return success(true);
    }
}
