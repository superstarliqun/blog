package com.china.soft.system.provider.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.china.soft.commons.base.LoginUser;
import com.china.soft.system.provider.dal.entity.SysAuthorization;
import com.china.soft.system.provider.dal.entity.SysUser;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * @Description: 用户dao层
 * @author MingYue
 * @Date 2023/10/12
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据类型查询用户信息
     * @param username
     * @param type
     * @return
     */
    LoginUser selectByUser(String username, String type);

    /**
     * 修改登录认证信息
     * @param auth
     */
    void updateLoginAuthInfo(@Param("auth") SysAuthorization auth);

}