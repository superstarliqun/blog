package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.SysEmailSendLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysEmailSendLogMapper extends BaseMapper<SysEmailSendLog> {
    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysEmailSendLog record);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysEmailSendLog record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysEmailSendLog record);
}