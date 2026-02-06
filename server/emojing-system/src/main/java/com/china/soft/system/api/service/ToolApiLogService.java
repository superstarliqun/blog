package com.china.soft.system.api.service;

import com.alibaba.fastjson.JSON;
import com.china.soft.system.provider.dal.entity.ToolApiLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ToolApiLogService extends IService<ToolApiLog>{


    String insert(JsonNode jsonNode);

    int update(ToolApiLog record);

    JsonNode selectByPrimaryKey(String uniqueCode);

    void delete(Integer id);
}
