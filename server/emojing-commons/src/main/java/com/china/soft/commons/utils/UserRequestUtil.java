package com.china.soft.commons.utils;

import com.china.soft.commons.base.LoginUser;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
public class UserRequestUtil {

    private static RedisUtil redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        redisTemplate = redisUtil;
    }

    public static LoginUser getLoginUser() {
        LoginUser user = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String tokenHeader = request.getHeader(com.china.soft.commons.utils.AuthenticationUtil.getTokenHeader());
            if (null != tokenHeader && !tokenHeader.equals("")) {
                Claims claims = JwtTokenUtil.validateAndParseToken(tokenHeader);
                String username = claims.get("username").toString();
                String usernameMd5 = JwtTokenUtil.getTokenCode(username);
                Object o = redisTemplate.get("online_users:"+usernameMd5);
                if (null != o){
                    user = (LoginUser) redisTemplate.get("online_users:"+usernameMd5);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return user;
    }

    public static String getTicket() {
        String ticket = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String tokenHeader = request.getHeader(com.china.soft.commons.utils.AuthenticationUtil.getTokenHeader());
            if (null != tokenHeader && !tokenHeader.equals("")) {
                ticket = com.china.soft.commons.utils.AuthenticationUtil.getTokenAESde(tokenHeader)[1];
            }
        } catch (Exception e) {
        }
        return ticket;
    }

    public static void removeToken(List<String> list) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Object[] objects = list.toArray();
                    String[] strings = new String[objects.length];
                    for (int i = 0; i < objects.length; i++) {
                        strings[i] = com.china.soft.commons.utils.AuthenticationUtil.getTokenCode((String) objects[i]);
                    }
                    redisTemplate.del(strings);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void removeToken(String username) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    redisTemplate.del(AuthenticationUtil.getTokenCode(username));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
