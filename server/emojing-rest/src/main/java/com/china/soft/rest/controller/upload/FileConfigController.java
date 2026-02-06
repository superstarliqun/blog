package com.china.soft.rest.controller.upload;

import com.china.soft.commons.utils.PageResult;
import com.china.soft.upload.api.service.FileConfigService;
import com.china.soft.upload.provider.dal.entity.FileConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import com.china.soft.commons.utils.response.CommonResponse;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * description 文件上传配置 前端控制器
 * @author  MingYue
 * @version 1.0
 * @since   2025/4/1
 */
@RestController
@RequestMapping("/fileConfig")
public class FileConfigController {

    @Resource
    private FileConfigService FileConfigService;

    @GetMapping("/page")
    @Operation(summary = "文件上传配置分页列表查询")
    public CommonResponse<PageResult<FileConfig>> getPage(@Valid FileConfig reqV0) {
        return success(FileConfigService.getPageList(reqV0));
    }

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @GetMapping("{id}")
    public CommonResponse<FileConfig> queryById(@PathVariable("id") Integer id) {
        return success(FileConfigService.getById(id));
    }

    /**
    * 新增数据
    *
    * @param FileConfig 实体
    * @return 新增结果
    */
    @PostMapping(value = "/add")
    public CommonResponse< Boolean> add(FileConfig FileConfig) {
        FileConfigService.insertSelective(FileConfig);
        return success(true);
    }

    /**
    * 编辑数据
    *
    * @param FileConfig 实体
    * @return 编辑结果
    */
    @PutMapping(value = "/update")
    public CommonResponse< Boolean> edit(FileConfig FileConfig) {
        FileConfigService.updateByPrimaryKeySelective(FileConfig);
        return success(true);
    }

    /**
    * 删除数据
    * @param id 主键
    * @return 删除是否成功
    */
    @PostMapping(value = "/delete/{id}")
    public CommonResponse< Boolean> deleteById(@PathVariable("id") Integer id) {
        FileConfigService.removeById(id);
        return success(true);
    }
}
