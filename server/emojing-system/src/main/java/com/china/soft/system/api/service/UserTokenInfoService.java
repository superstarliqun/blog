package com.china.soft.system.api.service;

import com.china.soft.system.provider.dal.entity.UserTokenInfo;
import com.baomidou.mybatisplus.extension.service.IService;
public interface UserTokenInfoService extends IService<UserTokenInfo>{

    UserTokenInfo getByJti(String jti);

    int insertSelective(UserTokenInfo record);

    int updateByPrimaryKeySelective(UserTokenInfo record);

    int updateByPrimaryKey(UserTokenInfo record);

}
