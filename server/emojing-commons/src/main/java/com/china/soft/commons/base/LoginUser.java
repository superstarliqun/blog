package com.china.soft.commons.base;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * @Description: security 登录对象实体类
 * @author MingYue
 * @Date 2023/9/17
 */
@Data
public class LoginUser extends BaseEntity implements UserDetails {

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 用户唯一标识
     */
    private String username;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号状态 0正常 1警告  2封存
     */
    private String state;

    /**
     * 是否授权
     */
    private String verified;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户识别序列号
     */
    private String uuid;

    /**
     * 过期日期
     */
    private Date expire;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
