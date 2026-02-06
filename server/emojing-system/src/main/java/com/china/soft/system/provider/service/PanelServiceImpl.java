package com.china.soft.system.provider.service;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.china.soft.system.api.service.PanelService;
import com.china.soft.system.shell.ProcessManager;
import com.sun.jna.Platform;
import org.springframework.stereotype.Service;
import oshi.PlatformEnum;

import java.io.IOException;
import java.util.HashMap;


@Service
public class PanelServiceImpl implements PanelService {

    private final String authorization = "Bearer 01CIWSHM01VTTNU601FI5TVM01I1C5Z6018N3ADL";

    @Override
    public String executeTask() {
        String result = null;
        JSONObject frpToken = frpToken();
        JSONArray list = list();
        JSONArray nameList = nameList();
        if(frpToken.get("code").toString().equals("200")){
            Object JsonData = frpToken.get("data");
            JSONObject data = JSONUtil.parseObj(JsonData);
            String token = data.get("token").toString();
            System.out.println(token);
            // 获取启动配置之后，在本地执行代码
            PlatformEnum value = PlatformEnum.getValue(Platform.getOSType());
            String platValue = value.getName();
            if(platValue != null && platValue.equals("Linux")){
                try {
                    // 判断是否存在进程如果存在进程需要kill -9
                    String processPid = ProcessManager.findProcessPid();
                    if(processPid != null)
                        ProcessManager.killProcess(processPid);
                    // 开始循环
                    for (Object item : list) {
                        JSONObject listItem = JSONUtil.parseObj(item);
                        String proxyId = listItem.get("proxyId").toString();
                        String proxyName = listItem.get("proxyName").toString();
                        String nodeId = listItem.get("nodeId").toString();
                        String remotePort = listItem.get("remotePort").toString();
                        if(proxyName.equals("1Panel服务面板")){
                            for (Object item2:nameList){
                                JSONObject listItem2 = JSONUtil.parseObj(item2);
                                String nodeId2 = listItem2.get("nodeId").toString();
                                String hostname = listItem2.get("hostname").toString();
                                if(nodeId2 != null && nodeId2.equals(nodeId)){
                                    result = hostname + ":" + remotePort;
                                }
                            }
                            ProcessManager.startNewProcess(token,proxyId);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    @Override
    public String getUrl() {
        String result = null;
        JSONArray list = list();
        JSONArray nameList = nameList();
        for (Object item:list){
            JSONObject listItem = JSONUtil.parseObj(item);
            String proxyName = listItem.get("proxyName").toString();
            String nodeId = listItem.get("nodeId").toString();
            String remotePort = listItem.get("remotePort").toString();
            if(proxyName.equals("1Panel服务面板")){
                for (Object item2:nameList){
                    JSONObject listItem2 = JSONUtil.parseObj(item2);
                    String nodeId2 = listItem2.get("nodeId").toString();
                    String hostname = listItem2.get("hostname").toString();
                    if(nodeId2 != null && nodeId2.equals(nodeId)){
                        result = hostname + ":" + remotePort;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取mefrp隧道列表
     */
    public JSONArray list(){
        String result = HttpRequest.get("https://api.mefrp.com/api/auth/proxy/list").header(Header.AUTHORIZATION, authorization).execute().body();
        JSONObject dataJson = JSONUtil.parseObj(result);
        return JSONUtil.parseArray(dataJson.get("data"));
    }

    /**
     * 获取mefrp隧道列表
     */
    public JSONArray nameList(){
        String result = HttpRequest.get("https://api.mefrp.com/api/auth/node/nameList").header(Header.AUTHORIZATION, authorization).execute().body();
        JSONObject dataJson = JSONUtil.parseObj(result);
        return JSONUtil.parseArray(dataJson.get("data"));
    }

    public JSONObject frpToken(){
        String resultItem = HttpRequest.get("https://api.mefrp.com/api/auth/user/frpToken")
                .header(Header.AUTHORIZATION, authorization)
                .execute().body();
        return JSONUtil.parseObj(resultItem);
    }

    /**
     * 获取
     * @param format
     * @param proxyId
     * @return
     */
    public JSONObject config(String format,String proxyId){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("format", format);
        paramMap.put("proxyId", proxyId);
        String result2 = HttpRequest.post("https://api.mefrp.com/api/auth/proxy/config")
                .header(Header.AUTHORIZATION, authorization)
                .form(paramMap)//表单内容
                .execute().body();
        return JSONUtil.parseObj(result2);
    }




}
