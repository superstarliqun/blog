package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.TLink;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TLinkMapper extends BaseMapper<TLink> {

    /**
     * 查询友链列表
     */
    List<TLink> list(TLink record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TLink record);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TLink record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TLink record);

}