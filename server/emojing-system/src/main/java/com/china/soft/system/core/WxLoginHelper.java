package com.china.soft.system.core;

import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.enums.IdentityType;
import com.china.soft.commons.utils.AuthenticationUtil;
import com.china.soft.commons.utils.RedisUtil;
import com.china.soft.system.api.service.SysUserService;
import com.china.soft.system.util.CodeGenerateUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 微信登录
 * @author MingYue
 * @Date 2023/10/18
 */
@Slf4j
@Component
public class WxLoginHelper {

    @Autowired
    private RedisUtil redisTemplate;
    /**
     * sse的超时时间，默认15min
     */
    private final static Long SSE_EXPIRE_TIME = 15 * 60 * 1000L;

    private final SysUserService sysUserService;
    /**
     * key = 验证码, value = 长连接
     */
    private LoadingCache<String, SseEmitter> verifyCodeCache;
    /**
     * key = 设备 value = 验证码
     */
    private LoadingCache<String, String> deviceCodeCache;

    public WxLoginHelper(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
        verifyCodeCache = CacheBuilder.newBuilder().maximumSize(300).expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, SseEmitter>() {
            @Override
            public SseEmitter load(String s) throws Exception {
                throw new RuntimeException("no val: " + s);
            }
        });

        deviceCodeCache = CacheBuilder.newBuilder().maximumSize(300).expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
            @Override
            public String load(String s) {
                int cnt = 0;
                while (true) {
                    String code = CodeGenerateUtil.genCode(cnt++);
                    if (!verifyCodeCache.asMap().containsKey(code)) {
                        return code;
                    }
                }
            }
        });
    }

    /**
     * 保持与前端的长连接
     * <p>
     * 直接根据设备拿之前初始化的验证码，不直接使用传过来的code
     *
     * @return
     */
    public SseEmitter subscribe(String deviceId) throws IOException {
        String realCode = deviceCodeCache.getUnchecked(deviceId) ;
        // fixme 设置15min的超时时间, 超时时间一旦设置不能修改；因此导致刷新验证码并不会增加连接的有效期
        SseEmitter sseEmitter = new SseEmitter(SSE_EXPIRE_TIME);
        SseEmitter oldSse = verifyCodeCache.getIfPresent(realCode);
        if (oldSse != null) {
            oldSse.complete();
        }
        verifyCodeCache.put(realCode, sseEmitter);
        sseEmitter.onTimeout(() -> {
            log.info("sse 超时中断 --> {}", realCode);
            verifyCodeCache.invalidate(realCode);
            sseEmitter.complete();
        });
        sseEmitter.onError((e) -> {
            log.warn("sse error! --> {}", realCode, e);
            verifyCodeCache.invalidate(realCode);
            sseEmitter.complete();
        });
        // 若实际的验证码与前端显示的不同，则通知前端更新
        sseEmitter.send("init#" + realCode);
        return sseEmitter;
    }

    public String resend(String deviceId) throws IOException {
        // 获取旧的验证码，注意不使用 getUnchecked, 避免重新生成一个验证码
        String oldCode = deviceCodeCache.getIfPresent(deviceId);
        SseEmitter lastSse = oldCode == null ? null : verifyCodeCache.getIfPresent(oldCode);
        if (lastSse != null) {
            lastSse.send("query#" + oldCode);
            return oldCode;
        }
        return "fail";
    }

    /**
     * 刷新验证码
     *
     * @return
     * @throws IOException
     */
    public String refreshCode(String deviceId) throws IOException {
        // 获取旧的验证码，注意不使用 getUnchecked, 避免重新生成一个验证码
        String oldCode = deviceCodeCache.getIfPresent(deviceId);
        SseEmitter lastSse = oldCode == null ? null : verifyCodeCache.getIfPresent(oldCode);
        if (lastSse == null) {
            log.info("last deviceId:{}, code:{}, sse closed!", deviceId, oldCode);
            deviceCodeCache.invalidate(deviceId);
            return null;
        }

        // 重新生成一个验证码
        deviceCodeCache.invalidate(deviceId);
        String newCode = deviceCodeCache.getUnchecked(deviceId);
        log.info("generate new loginCode! deviceId:{}, oldCode:{}, code:{}", deviceId, oldCode, newCode);

        lastSse.send("updateCode!");
        lastSse.send("refresh#" + newCode);
        verifyCodeCache.invalidate(oldCode);
        verifyCodeCache.put(newCode, lastSse);
        return newCode;
    }

    /**
     * 微信公众号登录
     *
     * @param verifyCode 用户输入的登录验证码
     * @return
     */
    public boolean login(String verifyCode,String userKey) {
        // 通过验证码找到对应的长连接
        SseEmitter sseEmitter = verifyCodeCache.getIfPresent(verifyCode);
        if (sseEmitter == null) {
            return false;
        }
        LoginUser loginUser = sysUserService.queryUserDetails(userKey, IdentityType.WECHAT.getCode());
        String uuid = UUID.randomUUID().toString();
        loginUser.setUuid(uuid);
        String key = AuthenticationUtil.getTokenCode(userKey);
        redisTemplate.set("online_users:"+key, loginUser,5 * 60L);
        String tokenAESen = AuthenticationUtil.getTokenAESen(loginUser.getUsername(), uuid);
        try {
            sseEmitter.send("login#" + tokenAESen);
            return true;
        } catch (Exception e) {
            log.error("登录异常: {}", verifyCode, e);
        } finally {
            sseEmitter.complete();
            verifyCodeCache.invalidate(verifyCode);
        }
        return false;
    }
}
