package com.china.soft.system.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.enums.IdentityType;
import com.china.soft.commons.utils.IpUtil;
import com.china.soft.commons.utils.LoginTypeChecker;
import com.china.soft.commons.utils.NicknameGenerator;
import com.china.soft.system.api.service.SysUserService;
import com.china.soft.system.provider.dal.entity.SysAuthorization;
import com.china.soft.system.provider.dal.entity.SysUser;
import com.china.soft.system.provider.dal.mapper.SysAuthorizationMapper;
import com.china.soft.system.provider.dal.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 用户管理持久类
 * @author MingYue
 * @Date 2023/9/27
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysAuthorizationMapper authorizationMapper;

    /**
     *
     * @param username 用户唯一表示
     * @param type 登录类型(手机号/邮箱) 或第三方应用名称 (微信/微博等)
     * @return
     */
    @Override
    public LoginUser queryUserDetails(String username,String type) {
        if(type == null){
            type = LoginTypeChecker.checkLoginType(username);
        }
        LoginUser auth = sysUserMapper.selectByUser(username, type);
        if(auth == null){
            return null;
        }
        return auth;
    }

    @Override
    public void updateAuth(String username, HttpServletRequest request) {
        String type = LoginTypeChecker.checkLoginType(username);
        SysAuthorization auth = new SysAuthorization();
        auth.setIdentityType(type);
        auth.setIdentifier(username);
        auth.setTimeLastAddr(IpUtil.getIpAddr());
        sysUserMapper.updateLoginAuthInfo(auth);
    }

    @Override
    public SysUser selectById(Integer userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        return sysUser;
    }

    @Override
    public Long autoRegisterWxUserInfo(String userkey) {
        SysUser user = queryWxSysUser(userkey);
        if(user == null){
            SysUser sysUser = new SysUser();
            sysUser.setNickname(NicknameGenerator.generateRandomNickname());
            sysUser.setState(0);
            sysUserMapper.insert(sysUser);

            SysAuthorization sysAuthorization = new SysAuthorization();
            sysAuthorization.setUserId(sysUser.getId());
            sysAuthorization.setIdentityType(IdentityType.WECHAT.getCode());
            sysAuthorization.setIdentifier(userkey);
            sysAuthorization.setVerified("1");
            authorizationMapper.insert(sysAuthorization);
            return sysUser.getId().longValue();
        }else{
            return user.getId().longValue();
        }
    }

    @Override
    public SysUser queryWxSysUser(String userKey) {
        LambdaQueryWrapper<SysAuthorization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAuthorization::getIdentifier,userKey);
        wrapper.eq(SysAuthorization::getIdentityType,IdentityType.WECHAT.getCode());
        wrapper.last("LIMIT 1");
        SysAuthorization sysAuthorization = authorizationMapper.selectOne(wrapper);
        if(sysAuthorization != null){
            return selectById(sysAuthorization.getUserId());
        }
        return null;
    }
}
