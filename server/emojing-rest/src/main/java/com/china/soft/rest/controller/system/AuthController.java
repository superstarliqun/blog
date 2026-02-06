package com.china.soft.rest.controller.system;

import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.utils.JwtTokenUtil;
import com.china.soft.commons.utils.RedisUtil;
import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.system.api.service.AuthorizationService;
import com.china.soft.system.api.service.SysUserService;
import com.china.soft.system.api.service.UserTokenInfoService;
import com.china.soft.system.provider.dal.entity.SysAuthorization;
import com.china.soft.system.provider.dal.entity.UserTokenInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import static com.china.soft.commons.utils.response.CommonResponse.error;
import static com.china.soft.commons.utils.response.CommonResponse.success;
import static com.china.soft.commons.utils.response.GlobalErrorCodeConstants.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserTokenInfoService tokenInfoService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private RedisUtil redisTemplate;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 接收客户端的Refresh Token
     * @return 刷新短token
     */
    @PostMapping("/refresh")
    @PermitAll
    public CommonResponse<HashMap<String, String>> refresh(@RequestHeader("Authorization") String refreshToken) {
        String accessTokenResult = null;
        String refreshTokenResult = null;
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            String token = refreshToken.substring(7);
            Claims claims = JwtTokenUtil.validateAndParseToken(token);
            // 查询 user_token_info 中是否存在 RefreshToken
            UserTokenInfo userTokenInfo = tokenInfoService.getByJti(claims.getId());
            if(userTokenInfo == null)
                return error(DATA_EXPIRY);
            SysAuthorization item2 = authorizationService.getById(userTokenInfo.getId());
            if( item2 == null)
                return error(DATA_EXPIRY);
             // TODO @MingYue：用户没有添加角色，博客系统不需要添加角色
            accessTokenResult = JwtTokenUtil.generateAccessToken(userTokenInfo.getUserId().toString(), item2.getIdentifier(), null);
            String key = JwtTokenUtil.getTokenCode(item2.getIdentifier());
            LoginUser userDetails = sysUserService.queryUserDetails(item2.getIdentifier(),null);
            redisTemplate.set("online_users:"+key, userDetails,5 * 60L);
            
             // @MingYue：判断如果在过期的前五天内，需要替换长Token,实现无感知更新
            long FIVE_DAYS_IN_MILLIS = 5L * 24 * 60 * 60 * 1000;
            long timeUntilExpiration = claims.getExpiration().getTime() - new Date().getTime();
            if (timeUntilExpiration <= FIVE_DAYS_IN_MILLIS) {
                // 更新数据库user_token_info
                refreshTokenResult = JwtTokenUtil.generateRefreshToken(userTokenInfo.getUserId().toString());
                UserTokenInfo tempUserTokenInfo = tokenInfoService.getByJti(claims.getId());
                tempUserTokenInfo.setExpirationTime(LocalDateTime.now().plusDays(90));
                tokenInfoService.updateByPrimaryKey(tempUserTokenInfo);
            }
            HashMap<String, String> result = new HashMap<>();
            result.put("accessToken",accessTokenResult);
            result.put("refreshToken",refreshTokenResult);
            System.out.println("Token 尚未过期，仍然有效。");
            return success(result);
        }
        return error(BAD_REQUEST);
    }
}
