package com.china.soft.srcurity.filter;


import cn.hutool.core.util.StrUtil;
import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.utils.AuthenticationUtil;
import com.china.soft.commons.utils.RedisUtil;
import com.china.soft.commons.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @Description 权限处理类
 * @author SunJ
 * @CreateTime 2019/10/3 8:39
 */
@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 判断是否是免密登录路径
        request.getRequestURI();

        String tokenHeader = request.getHeader(AuthenticationUtil.getTokenHeader());
        if (StrUtil.isNotEmpty(tokenHeader)) {
            try {
                Claims claims = null;
                try {
                    claims = JwtTokenUtil.validateAndParseToken(tokenHeader);
                }catch (Exception e){
                    log.error(e.getMessage());
                }
                String username = claims.get("username").toString(); // 登录用户名
                Instant iss = claims.getIssuedAt().toInstant();// 签证时间
                Instant exp = claims.getExpiration().toInstant(); // 过期时间
                Duration duration = Duration.between(iss, exp);

                String key = JwtTokenUtil.getTokenCode(username);
                Object o = redisTemplate.get("online_users:"+ key);
                if (null != o){
                    LoginUser user = (LoginUser) o;
                    if( user.getUserId().toString().equals( claims.getSubject() )){
                        // 获取角色
                        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
                        // 设置token过期时间
                        redisTemplate.set(username , user, duration.getSeconds());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, claims.getSubject(), authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                log.info("Token无效");
            }
        }
        filterChain.doFilter(request, response);
    }
}
