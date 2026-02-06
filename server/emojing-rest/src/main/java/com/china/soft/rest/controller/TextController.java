package com.china.soft.rest.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.china.soft.commons.utils.IpUtil;
import com.china.soft.commons.utils.response.CommonResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import static com.china.soft.commons.utils.response.CommonResponse.success;
@RestController
public class TextController {

    @GetMapping("/getIp")
    @PermitAll
    public String abc() {
        return IpUtil.getIpAddr();
    }

    @GetMapping(value = "/weCome")
    @PermitAll
    public CommonResponse<Boolean> wecmoe(HttpServletRequest request)  {
        String uaStr = request.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(uaStr);
        String browser = ua.getBrowser().toString();
        String version = ua.getVersion();
        String os = ua.getOs().toString();
        String platform = ua.getPlatform().toString();
        System.out.println(browser+" "+version+" "+os+" "+platform);
        System.out.println(123);
        return success(true);
    }

    /**
     * 获取鼓励语句，每次都不同
     * @return string
     */
    @GetMapping(value = "/getHelloWorld")
    @PermitAll
    public CommonResponse<String> getHelloWorld(){
        String result = HttpUtil.get("https://img.8845.top/yiyan/index.php");
        JSONObject jsonObject = JSONUtil.parseObj(result);
        return success(jsonObject.getStr("content"));
    }
}
