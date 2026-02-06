package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.system.provider.dal.entity.SysEmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysEmailConfigMapper extends BaseMapper<SysEmailConfig> {
    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysEmailConfig record);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysEmailConfig record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysEmailConfig record);

    /**
     * 分页集合
     */
    List<SysEmailConfig> getPage(@Param("page") Page<SysEmailConfig> page, @Param("param") SysEmailConfig pageReq);

}