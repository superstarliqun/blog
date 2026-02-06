package com.china.soft.system.api.service;

import com.china.soft.commons.base.LoginUser;
import com.china.soft.system.provider.dal.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户管理接口类
 * @author MingYue
 * @Date 2023/9/27
 */
public interface SysUserService {

    /**
     * 根据登录账号查询用户详情
     * @param username
     * @return
     */
    LoginUser queryUserDetails(String username,String type);

    /**
     * 修改登录时间以及IP
     * @param username
     * @param request
     */
    void updateAuth(String username, HttpServletRequest request);

    /**
     * 获取用户详情
     * @param userId
     * @return
     */
    SysUser selectById(Integer userId);

    /**
     * 自动注册微信用户
     * @param userkey
     * @return
     */
    Long autoRegisterWxUserInfo(String userkey);

    /**
     * 查询登录用户进行注册登录
     * @param userKey
     * @return
     */
    SysUser queryWxSysUser(String userKey);

}
