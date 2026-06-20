package com.china.soft.system.provider.service;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.china.soft.system.api.service.PanelService;
import com.china.soft.system.shell.ProcessManager;
import com.sun.jna.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.PlatformEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


@Service
@Slf4j
public class PanelServiceImpl implements PanelService {

    private final String authorization = "Bearer 01CIWSHM01VTTNU601FI5TVM01I1C5Z6018N3ADL";
    private static final int HTTP_SUCCESS_CODE = 200;
    // MEFrp的Token
    private String token;
    private final String key = "1Panel服务面板";

    @Override
    public String executeTask() {
        frpToken();
        // 获取部署平台OS
        String platValue = PlatformEnum.getValue(Platform.getOSType()).getName();
        if(platValue != null && platValue.equals("Linux")) {
            // 获取隧道管理列表
            JSONObject data = proxyList();
            // 准备写的东西
            JSONArray nodes = data.getJSONArray("nodes");
            JSONArray proxies = data.getJSONArray("proxies");
            for (int i = 0; i < proxies.size(); i++) {
                JSONObject node = JSONUtil.parseObj(nodes.get(i));
                JSONObject proxy = JSONUtil.parseObj(proxies.get(i));
                String proxyName = proxy.getStr("proxyName");
                // 如果是运维面板才执行的操作
                if (proxyName.equals(key)) {
                    try {
                        String proxyId = proxy.getStr("proxyId");
                        String remotePort = proxy.getStr("remotePort");
                        Object hostname = node.getStr("hostname");
                        // 判断是否存在进程如果存在进程需要kill -9
                        String processPid = ProcessManager.findProcessPid();
                        if (processPid != null)
                            ProcessManager.killProcess(processPid);
                        ProcessManager.startNewProcess(token, proxyId);
                        return hostname + ":" + remotePort;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getUrl() {
        JSONObject data = proxyList();
        // 准备写的东西
        JSONArray nodes = data.getJSONArray("nodes");
        JSONArray proxies = data.getJSONArray("proxies");
        for (int i = 0; i < proxies.size(); i++) {
            JSONObject node = JSONUtil.parseObj(nodes.get(i));
            JSONObject proxy = JSONUtil.parseObj(proxies.get(i));
            String proxyName = proxy.getStr("proxyName");
            if (proxyName.equals(key)) {
                String remotePort = proxy.getStr("remotePort");
                Object hostname = node.getStr("hostname");
                return hostname + ":" + remotePort;
            }
        }
        return null;
    }

    /**
     * 获取MEFrp获取隧道管理列表
     *
     */
    public JSONObject proxyList(){
        try (HttpResponse response = HttpRequest.get("https://api.mefrp.com/api/auth/proxy/list").header(Header.AUTHORIZATION, authorization).execute()){
            JSONObject dataJson = JSONUtil.parseObj(response.body());
            return JSONUtil.parseObj(dataJson.get("data").toString());
        } catch (Exception e) {
            log.error("ME Frp获取生成启动配置接口方法报错，请联系管理员:{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }



    /**
     * 生成启动配置接口
     * @param format 格式类型 toml
     * @param proxyId 代理编号
     * @return {"code":200,"data":{"config":"serverAddr = '47.108.228.69'\nserverPort = 2333\nuser = 'b275f77317aa8a3d88c327a5e53d657f'\n\n[auth]\nmethod = 'token'\ntoken = 'MEFrpServerToken'\n\n[[proxies]]\nname = '禅道'\ntype = 'tcp'\n\nlocalIP = '127.0.0.1'\nlocalPort = 8118\nremotePort = 14149\n\n[proxies.transport]\nuseEncryption = false\nuseCompression = false","type":"TOML"},"message":"生成配置成功"}
     */
    @Override
    public JSONObject config(String format,String proxyId){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("format", format);
        paramMap.put("proxyId", proxyId);
        try (HttpResponse response = HttpRequest.post("https://api.mefrp.com/api/auth/proxy/config")
                     .header(Header.AUTHORIZATION, authorization)
                     .form(paramMap)//表单内容
                     .execute()){
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            log.error("ME Frp获取生成启动配置接口方法报错，请联系管理员",e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取节点状态
     * @return [{"nodeId":118,"name":"广东/广州 ①","totalTrafficIn":370616245,"totalTrafficOut":2059874269,"onlineClient":37,"onlineProxy":37,"isOnline":true,"version":"MEFrp_0.67.0_20260302_f1907e56","uptime":1012770,"curConns":2,"loadPercent":50}]
     */
    @Override
    public JSONObject getNodeStatus(){
        try(HttpResponse response = HttpRequest.get("https://api.mefrp.com/api/auth/node/status")
                    .header(Header.AUTHORIZATION, authorization)
                    .execute()){
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            log.error("ME Frp获取节点状态方法报错，请联系管理员:{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * MEFrp的Token
     * @return {"code":200,"data":{"token":"b275f77317aa8a3d88c327a5e53d657f"},"message":"获取启动令牌成功"}
     */
    @Override
    public String frpToken(){
        String error = null;
        try(HttpResponse response = HttpRequest.get("https://api.mefrp.com/api/auth/user/frpToken")
                    .header(Header.AUTHORIZATION, authorization)
                    .execute()){
            if (response.isOk()) {
                JSONObject jsonObject = JSONUtil.parseObj(response.body());
                if(Objects.equals(jsonObject.getInt("code"), HTTP_SUCCESS_CODE)){
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {
                        String token = data.getStr("token");
                        this.token = token;
                        return token;
                    }
                }
                error = jsonObject.getStr("message");
            }
        } catch (Exception e) {
            log.error("ME Frp获取Token方法报错，请联系管理员", e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("获取FRP Token失败: " + (error != null ? error : "未知错误"));
    }

}
