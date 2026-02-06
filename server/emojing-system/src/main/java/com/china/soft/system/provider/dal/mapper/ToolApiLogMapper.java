package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.ToolApiLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ToolApiLogMapper extends BaseMapper<ToolApiLog> {
    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(ToolApiLog record);
}