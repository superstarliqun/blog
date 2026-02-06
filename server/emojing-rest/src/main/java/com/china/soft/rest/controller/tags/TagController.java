package com.china.soft.rest.controller.tags;

import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.system.api.service.ArticlesTagsService;
import com.china.soft.system.provider.dal.entity.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.List;


@RestController
@RequestMapping("/tags")
public class TagController {

    @Resource
    private ArticlesTagsService tagsService;

    @GetMapping("/list")
    @PermitAll
    public CommonResponse<List<Tag>> list() {
        return tagsService.list();
    }

}
