package com.china.soft.rest.controller.tools;
import com.china.soft.system.provider.dal.entity.ToolApiLog;
import com.china.soft.system.api.service.ToolApiLogService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;
import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
* JSON工具 前端控制器
* Wed Mar 19 10:10:09 CST 2025
* @author MingYue
*/
@RestController
@RequestMapping("/api")
public class ToolApiLogController {

    /**  服务对象 */
    @Autowired
    private ToolApiLogService toolApiLogService;

    /**
    * 通过主键查询单条数据
    *
    * @param uniqueCode 主键
    * @return 单条数据
    */
    @GetMapping("/{id}")
    @PermitAll
    public JsonNode queryById(@PathVariable("id") String uniqueCode) {
        return toolApiLogService.selectByPrimaryKey(uniqueCode);
    }

    /**
    * 新增数据
    *
    * @param jsonNode 实体
    * @return 新增结果
    */
    @PostMapping(value = "/save")
    public CommonResponse<String> add(@RequestParam JsonNode jsonNode) {
        return success(toolApiLogService.insert(jsonNode));
    }

    /**
    * 编辑数据
    *
    * @param toolApiLog 实体
    * @return 编辑结果
    */
    @PutMapping(value = "/update")
    public CommonResponse<Boolean> edit(ToolApiLog toolApiLog) {
        toolApiLogService.update(toolApiLog);
        return success(true);
    }

    /**
    * 删除数据
    * @param id 主键
    * @return 删除是否成功
    */
    @PostMapping(value = "/delete/{id}")
    public CommonResponse<Boolean> deleteById(@PathVariable("id") Integer id) {
        toolApiLogService.delete(id);
        return success(true);
    }
}
