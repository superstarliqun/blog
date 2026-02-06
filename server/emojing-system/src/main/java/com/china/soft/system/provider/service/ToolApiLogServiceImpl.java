package com.china.soft.system.provider.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.system.provider.dal.entity.ToolApiLog;
import com.china.soft.system.provider.dal.mapper.ToolApiLogMapper;
import com.china.soft.system.api.service.ToolApiLogService;

import javax.annotation.Resource;

@Service
public class ToolApiLogServiceImpl extends ServiceImpl<ToolApiLogMapper, ToolApiLog> implements ToolApiLogService{

    @Override
    public String insert(JsonNode jsonNode){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ToolApiLog record =  new ToolApiLog();
            record.setUniqueCode(IdUtil.objectId());
            record.setCreatedAt(LocalDateTime.now());
            record.setJson(objectMapper.writeValueAsString(jsonNode));
            baseMapper.insertSelective(record);
            return record.getUniqueCode();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(ToolApiLog record) {
        record.setUpdatedAt(LocalDateTime.now());
        return baseMapper.updateById(record);
    }

    @Override
    public JsonNode selectByPrimaryKey(String uniqueCode) {
        try {
            LambdaUpdateWrapper<ToolApiLog> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ToolApiLog::getUniqueCode, uniqueCode);
            ToolApiLog toolApiLog = baseMapper.selectOne(wrapper);
            if(toolApiLog== null){
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(toolApiLog.getJson());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        baseMapper.update(null,Wrappers.<ToolApiLog>lambdaUpdate()
                .set(ToolApiLog::getDeleted,1)
                .eq(ToolApiLog::getUniqueCode,id.toString())
        );
    }
}
