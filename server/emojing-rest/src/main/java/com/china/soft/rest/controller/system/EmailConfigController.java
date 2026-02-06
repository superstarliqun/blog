package com.china.soft.rest.controller.system;

import com.china.soft.commons.utils.PageResult;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.system.api.service.SysEmailConfigService;
import com.china.soft.system.provider.dal.entity.SysEmailConfig;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static com.china.soft.commons.utils.response.CommonResponse.success;

@Api(tags = "邮件配置 前端控制器")
@Slf4j
@RestController
@RequestMapping("/email-config")
public class EmailConfigController {

    @Resource
    SysEmailConfigService configService;

    @Operation(summary = "保存")
    @PostMapping(value = "/save")
    public CommonResponse<Boolean> save(@RequestBody SysEmailConfig entity){
        configService.insertSelective(entity);
        return success(true);
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public CommonResponse<Boolean> update(@RequestBody SysEmailConfig entity){
        configService.updateById(entity);
        return success(true);
    }

    @Operation(summary = "查询详情")
    @PostMapping(value = "/detail/{id}")
    public CommonResponse<SysEmailConfig> detail(@PathVariable Integer id){
        return success(configService.selectByPrimaryKey(id));
    }

    @Operation(summary = "删除")
    @PostMapping(value = "/remove/{id}")
    public CommonResponse<Boolean> remove(@PathVariable Integer id){
        return success(configService.removeById(id));
    }

    @PermitAll
    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public CommonResponse<PageResult<SysEmailConfig>> page(SysEmailConfig entity){
        return success(configService.page(entity));
    }

}
