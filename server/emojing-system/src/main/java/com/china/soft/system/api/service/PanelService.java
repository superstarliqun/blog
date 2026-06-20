package com.china.soft.system.api.service;

import cn.hutool.json.JSONObject;

public interface PanelService {

    // 定时任务执行
    String executeTask();

    // 获取1panel访问地址
    String getUrl();

    // 获取Token
    String frpToken();

    // 节点监控列表
    JSONObject getNodeStatus();

    // 生成启动配置接口
    JSONObject config(String format,String proxyId);

}
