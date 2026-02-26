package com.china.soft.rest.controller.system;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.china.soft.chatai.handler.WebSocketHandler;
import com.china.soft.chatai.vo.WxRequestResultVo;
import com.china.soft.chatai.vo.WxResponseResultVo;
import com.china.soft.chatai.xunfei.XunFeiIntegration;
import com.china.soft.commons.utils.WeiXinCheckUrl;
import com.china.soft.commons.utils.SHA1;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.system.core.WxAckHelper;
import com.china.soft.system.core.WxLoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.china.soft.commons.utils.response.CommonResponse.error;
import static com.china.soft.commons.utils.response.CommonResponse.success;


/**
 * @author admin
 */
@RestController
@Slf4j
public class WeChatController {

    @Autowired
    private XunFeiIntegration xunFeiIntegration;

    @Autowired
    private WxLoginHelper wxLoginHelper;

    @Autowired
    private WxAckHelper wxAckHelper;

    @Value("${wx.appId}")
    private String wxAppId;

    @Value("${wx.appSecret}")
    private String wxSecret;


    /**
     * 获取二维码地址
     * @return
     */
    @GetMapping("/getQrCode")
    @PermitAll
    private CommonResponse<JSONObject> getQrCode(){
        try {
            // 获取token开发者
            String accessToken = getAccessToken();
            String getQrCodeUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
            // 这里生成一个带参数的二维码，参数是scene_str
            String sceneStr ="wlq12345";
            String json="{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\"" +", \"action_info\": {\"scene\": {\"scene_str\": \""+sceneStr+"\"}}}";
            String result = HttpUtil.post(getQrCodeUrl, json);
            JSONObject jsonObject = JSONObject.parseObject(result);
            jsonObject.put("sceneStr",sceneStr);
            return success(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return error(501,e.getMessage());
        }
        // 生成接口文件，https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=xxx
    }

    /**
     * 验证服务器通讯
     * @param weiXinCheckUrl
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/checkSign")
    @PermitAll
    public String checkSign(WeiXinCheckUrl weiXinCheckUrl,HttpServletRequest request){
        //与申请测试号网站填写的token对应
        String token = "liupanpan624";
        List<String> paramList = new ArrayList<>();
        paramList.add(weiXinCheckUrl.getNonce());
        paramList.add(weiXinCheckUrl.getTimestamp());
        paramList.add(token);
        //按字节排序
        Collections.sort(paramList);
        //按顺序拼接字符串
        StringBuilder stringBuilder = new StringBuilder();
        paramList.forEach(stringBuilder::append);
        //sha1加密
        String encode = SHA1.encode(stringBuilder.toString());
        log.info("这个是联调微信公众号AI回复功能的：{}", weiXinCheckUrl.getEchostr());
        if (encode.equals(weiXinCheckUrl.getSignature())) {
            return weiXinCheckUrl.getEchostr();
        }
        return null;
    }

    /**
     * 微信扫码通讯回调
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/checkSign",  consumes = {"application/xml", "text/xml"}, produces = "application/xml;charset=utf-8")
    @PermitAll
    public WxResponseResultVo checkSign(@RequestBody WxRequestResultVo request) throws IOException {
        log.info("微信返回验签是 {}"+request.toString());
        if ("subscribe".equals(request.getEvent()) || "scan".equalsIgnoreCase(request.getEvent())) {
            String key = request.getEventKey();
            if (StringUtils.isNotBlank(key) || key.startsWith("qrscene_")) {
                // 带参数的二维码，扫描、关注事件拿到之后，直接登录，省却输入验证码这一步
                // fixme 带参数二维码需要 微信认证，个人公众号无权限
                String code = key.substring("qrscene_".length());
                wxLoginHelper.login(code,request.getFromUserName());
                WxResponseResultVo res = new WxResponseResultVo();
                res.setContent("登录成功");
                return res;
            }
        }else{
            // 设置微信公众号AI自动回复
            request.setMsgType("transfer_biz_ai_ivr");
        }
        WxResponseResultVo res = wxAckHelper.buildResponseBody(request,request.getFromUserName());
        return res;
    }

    @GetMapping(path = "/subscribe", produces = {org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE})
    @PermitAll
    public SseEmitter subscribe(String deviceId) throws IOException {
        return wxLoginHelper.subscribe(deviceId);
    }

    @GetMapping(path = "/login/fetch")
    @PermitAll
    public CommonResponse<String> resendCode(String deviceId) throws IOException {
        String resend = wxLoginHelper.resend(deviceId);
        return success(resend);
    }

    @GetMapping(value = "/refresh")
    @PermitAll
    public CommonResponse<String> refresh(String deviceId) throws IOException {
        return success(wxLoginHelper.refreshCode(deviceId));
    }

    /**
     *  获取accessToken
     * @return
     */
    public String getAccessToken(){
        String accessToken = null;
        String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+wxAppId+"&secret="+wxSecret;
        String s = HttpUtil.get(getTokenUrl);
        JSONObject jsonObject = JSONObject.parseObject(s);
        accessToken = jsonObject.getString("access_token");
        return accessToken ;
    }

    private String waitForWebSocketData(WebSocketHandler webSocketHandler) {
        String lastReceivedText = null;
        int maxAttempts = 10;
        int attempts = 0;

        // 循环等待，直到有数据可用或达到最大尝试次数
        while (lastReceivedText == null && attempts < maxAttempts) {
            lastReceivedText = webSocketHandler.getLastReceivedText();
            if (lastReceivedText == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                attempts++;
            }
        }

        return lastReceivedText;
    }

}
