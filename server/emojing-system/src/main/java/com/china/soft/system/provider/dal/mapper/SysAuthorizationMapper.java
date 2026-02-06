package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.system.provider.dal.entity.SysAuthorization;

public interface SysAuthorizationMapper  extends BaseMapper<SysAuthorization> {

    SysAuthorization selectByName(String username);

}
