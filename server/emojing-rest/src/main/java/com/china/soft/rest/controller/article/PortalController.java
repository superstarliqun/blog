package com.china.soft.rest.controller.article;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.china.soft.commons.enums.IPEnum;
import com.china.soft.system.kafka.ViewRecordProducer;
import com.china.soft.commons.utils.IpUtil;
import com.china.soft.commons.utils.OpenRestApi;
import com.china.soft.commons.utils.PageResult;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.dto.a.ArticleDto;
import com.china.soft.system.api.service.ArticleService;
import com.china.soft.system.api.service.ArticlesCategoryService;
import com.china.soft.system.provider.dal.entity.Article;
import com.china.soft.system.provider.dal.entity.ArticlesCategory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import java.util.List;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * @Description: 开放式接口-门户前端控制器
 * @author MingYue
 * @Date 2023/9/15
 */
@RestController
@RequestMapping(value = "/portal")
@Slf4j
public class PortalController {

    @Resource
    private ArticleService articleService;
    @Resource
    private ArticlesCategoryService categoryService;
    @Resource
    private ViewRecordProducer viewRecordProducer;

    @Operation(summary = "获取文章通用列表")
    @GetMapping("/article/list/{num}/{size}")
    @PermitAll
    public CommonResponse<PageResult<Article>> page(@PathVariable Integer num,@PathVariable Integer size, ArticleDto dto){
        return articleService.page(num,size,dto);
    }

    @Operation(summary = "获取文章详情")
    @GetMapping("/article/get/{id}")
    @PermitAll
    public CommonResponse<Article> get(@PathVariable Integer id){
        String ipAddr = IpUtil.getIpAddr();
        viewRecordProducer.sendViewRecord(id.toString(),ipAddr);
        return articleService.get(id);
    }

    @Operation(summary = "获取天气信息")
    @GetMapping(value = "/weather/get/{type}")
    @PermitAll
    public CommonResponse<JSONObject> get(@PathVariable String type){
        String ip = IpUtil.getIpAddr();
        String atCode = null;
        if(ip.equals(IPEnum.LOCAL.getCode())|| ip.equals(IPEnum.LOCAL.getCode()) ){
            atCode = "110000";
        }else{
            // 通过ip获取
            String resultJson = HttpUtil.get(OpenRestApi.GaoDeQueryAtCode.replace("{param}", ip));
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            atCode = jsonObject.getString("adcode");
        }
        String res = HttpUtil.get(OpenRestApi.GaoDeQueryWeatherInfo.replace("{param}", atCode)+type);
        JSONObject result = JSONObject.parseObject(res);
        return success(result);
    }

    @Operation(summary = "获取默认分类")
    @GetMapping(value = "/categories")
    @PermitAll
    public CommonResponse<List<ArticlesCategory>> defaultCategory(){
       return categoryService.queryDefaultCategory();
    }





}
