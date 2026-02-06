package com.china.soft.upload.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.upload.provider.dal.entity.FileConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileConfigMapper extends BaseMapper<FileConfig> {

    /**
     * 分页查询上传配置列表
     * @param page
     * @param reqV0
     * @return
     */
    List<FileConfig> getPageList(@Param("page") Page<FileConfig> page, @Param("param") FileConfig reqV0);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(FileConfig record);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(FileConfig record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(FileConfig record);

    /**
     * 查询默认配置文件客户端
     * @return
     */
    FileConfig selectByMaster();


}