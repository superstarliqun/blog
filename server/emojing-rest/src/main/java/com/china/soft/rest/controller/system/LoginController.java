package com.china.soft.rest.controller.system;

import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.utils.RedisUtil;
import com.china.soft.commons.utils.UserRequestUtil;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.srcurity.utils.VerifyCodeUtils;
import com.china.soft.system.api.service.SysUserService;
import com.china.soft.system.provider.dal.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.china.soft.commons.utils.response.CommonResponse.error;
import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * @Description: 登录前端控制器
 * @author MingYue
 * @Date 2023/9/17
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private RedisUtil redisTemplate;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/verifyCode.jpg")
    @PermitAll
    public void verifyCode(HttpServletResponse response) throws IOException {
        /*禁止缓存*/
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        /*获取验证码*/
        String code = VerifyCodeUtils.generateVerifyCode(4);
        /*验证码已key，value的形式缓存到redis 存放时间一分钟*/
        String uuid = UUID.randomUUID().toString();
        response.setHeader("captcha", uuid);
        redisTemplate.set(uuid, code, 1800);
        log.info("登录获取验证码："+ code);
        Cookie cookie = new Cookie("captcha", uuid);
        /*key写入cookie，验证时获取*/
        response.addCookie(cookie);
        ServletOutputStream outputStream = response.getOutputStream();
        VerifyCodeUtils.outputImage(110, 40, outputStream, code);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 获取用户基本信息
     * @return
     */
    @GetMapping("/getUserInfo")
    public CommonResponse<SysUser> getUserInfo(){
        LoginUser loginUser = UserRequestUtil.getLoginUser();
        SysUser entity = sysUserService.selectById(loginUser.getUserId());
        if(entity != null){
            return success(entity);
        }
        return error(501,"失败");
    }

}
