package com.china.soft.rest.controller.demo;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.china.soft.commons.utils.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.china.soft.commons.utils.response.CommonResponse.success;

@Slf4j
@RestController
@RequestMapping("/demo2")
public class Demo2Controller {

    private final static String ak = "NEFRSNIBOZDDESVJ";
    private final static String sk = "SKsbdgwwyprjjgpy";
    private final static String domain = "http://192.168.138.120";

    @GetMapping("/test1")
    @PermitAll
    public CommonResponse<String> test1() throws Exception {
        String url = "/open/api/edit/v1/files/471eba5030/link?type=w";
        String bodyStr = "{\"type\":\"w\"}";
        String sha256body = HMacUtils.getSHA256StrJava(bodyStr.getBytes());

        //日期格式化
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = dateFormat.format(new Date());

        // hmac-sha256(secret_key, Ver + HttpMethod + URI + Content-Type + Wps-Date + sha256(HttpBody))
        // WPS-4POST/callback/path/demoapplication/jsonWed, 20 Apr 2022 01:33:07 GMTfc005f51a6e75586d2d5d078b657dxxxdf9c1dfa6a7c0c0ba38c715daeb6ede9
        String signature = HMacUtils.HMACSHA256("WPS-4GET"+url.replace("/open","") +"application/json"+ date + sha256body, sk);
        // 发送日期
        HttpResponse response = HttpRequest.get(domain+url)
                .header(Header.CONTENT_TYPE, "application/json")
                .header("Wps-Docs-Date", date)
                .header("Wps-Docs-Authorization", "WPS-4 "+ak+":"+signature)
                .body(bodyStr)
                .timeout(20000)
                .execute();
        return success(response.body());
    }


}
