package com.china.soft.rest.controller.demo;

import com.china.soft.commons.utils.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/demo4")
@Slf4j
public class Demo4Controller {

    @PermitAll
    @GetMapping("/test")
    public CommonResponse<Boolean> test1(HttpServletRequest request){
        String path = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            path += "?" + queryString;
        }
        log.info("path:{}", path);
        return null;
    }

}
