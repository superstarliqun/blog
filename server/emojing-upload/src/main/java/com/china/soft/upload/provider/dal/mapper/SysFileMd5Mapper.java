package com.china.soft.upload.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.upload.provider.dal.entity.SysFileMd5;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysFileMd5Mapper extends BaseMapper<SysFileMd5> {


    List<SysFileMd5> getPageList(@Param("page") Page<SysFileMd5> page,@Param("param") SysFileMd5 reqV0);
    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysFileMd5 record);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysFileMd5 record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysFileMd5 record);

}