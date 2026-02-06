package com.china.soft.rest.controller.panel;

import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.system.api.service.PanelService;
import com.china.soft.system.core.DynamicScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

import static com.china.soft.commons.utils.response.CommonResponse.success;

@RestController
@RequestMapping("/panel")
public class PanelController {

    @Autowired
    DynamicScheduledTask dynamicScheduledTask;

    @Autowired
    PanelService panelService;


    /**
     * 更新动态任务
     * @return
     */
    @GetMapping("/updateCorn")
    @PermitAll
    public CommonResponse<String> updateCorn(@RequestParam("cron") String newCron) {
        dynamicScheduledTask.updateCron(newCron);
        return success("修改动态任务监听隧道！");
    }

    @GetMapping("/execute")
    public CommonResponse<String> executeTask(){
        return success(panelService.executeTask());
    }

    @GetMapping("/getUrl")
    public CommonResponse<String> getUrl(){
        return success(panelService.getUrl());
    }

}
