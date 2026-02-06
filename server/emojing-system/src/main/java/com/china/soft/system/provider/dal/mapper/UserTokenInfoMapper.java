package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.UserTokenInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserTokenInfoMapper extends BaseMapper<UserTokenInfo> {
    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(UserTokenInfo record);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(UserTokenInfo record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(UserTokenInfo record);


    UserTokenInfo getByJti(String jti);


    void deleteByUserId(Integer userId);
}