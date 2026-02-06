package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.ViewUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViewUserMapper extends BaseMapper<ViewUser> {
    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(ViewUser record);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(ViewUser record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(ViewUser record);
}