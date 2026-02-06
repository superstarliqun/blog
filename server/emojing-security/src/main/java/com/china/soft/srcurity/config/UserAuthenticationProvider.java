package com.china.soft.srcurity.config;

import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.enums.GlobalEnum;
import com.china.soft.commons.utils.AuthenticationUtil;
import com.china.soft.system.api.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @description: 自定义登录验证
 * @author MingYue
 * @Date 2023/9/17
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password =  AuthenticationUtil.getPasswordAESde((String) authentication.getCredentials());
        LoginUser userDetails = sysUserService.queryUserDetails(username,null);
        if ( userDetails == null ) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (!userDetails.getPassword().equals(AuthenticationUtil.getPassword(password))) {
            throw new BadCredentialsException("密码不正确");
        }
        if(userDetails.getState().equals(GlobalEnum.ACCOUNT_LOCKED.getCode())){
            throw new LockedException("账号锁定");
        }
        if(userDetails.getState().equals(GlobalEnum.ACCOUNT_DISABLED.getCode())){
            throw new DisabledException("账号禁用");
        }
        // 角色集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 进行登录
        return new UsernamePasswordAuthenticationToken( userDetails, password, authorities );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        System.out.println(authentication);
        return true;
    }
}
