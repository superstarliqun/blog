package com.china.soft.srcurity.handler;

import cn.hutool.core.date.DateUtil;
import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.utils.RedisUtil;
import com.china.soft.commons.utils.JwtTokenUtil;
import com.china.soft.srcurity.utils.ResultUtil;
import com.china.soft.system.api.service.AuthorizationService;
import com.china.soft.system.api.service.SysUserService;
import com.china.soft.system.api.service.UserTokenInfoService;
import com.china.soft.system.provider.dal.entity.SysAuthorization;
import com.china.soft.system.provider.dal.entity.UserTokenInfo;
import com.china.soft.system.provider.dal.mapper.SysAuthorizationMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

/**
 * @Description 登录成功处理类
 */
@Slf4j
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisUtil redisTemplate;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserTokenInfoService tokenInfoService;

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * 登录成功返回结果
     * @author MingYue
     * @CreateTime 2019/10/3 9:27
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        // 组装JWT
        LoginUser loginUser =  (LoginUser) authentication.getPrincipal();
        String uuid = loginUser.getUsername().equals("admin")? "admin":UUID.randomUUID().toString();
        loginUser.setUuid(uuid);
        String key = JwtTokenUtil.getTokenCode(loginUser.getUsername());
        redisTemplate.set("online_users:"+key, loginUser,5 * 60L);
        // 修改最后登录时间以及IP
        sysUserService.updateAuth(loginUser.getUsername(), request);
        // 返回封装对象
        Map<String,Object> resultData = new HashMap<>(3);
        resultData.put("code",0);
        resultData.put("msg", "登录成功");
        resultData.put("accessToken", JwtTokenUtil.generateAccessToken(uuid, loginUser.getUsername(), null));
        String refreshToken = JwtTokenUtil.generateRefreshToken(uuid);
        resultData.put("refreshToken", refreshToken);
        // 将token存放到数据库中
        Claims claims = JwtTokenUtil.validateAndParseToken(refreshToken);
        java.time.LocalDateTime localDateTime = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        SysAuthorization item = authorizationService.getByName(loginUser.getUsername());
        UserTokenInfo userTokenInfo = new UserTokenInfo(claims.getId(), item.getId(), loginUser.getUserId(),localDateTime,0, java.time.LocalDateTime.now());
        tokenInfoService.insertSelective(userTokenInfo);
        ResultUtil.responseJson(response,resultData);
    }
}
