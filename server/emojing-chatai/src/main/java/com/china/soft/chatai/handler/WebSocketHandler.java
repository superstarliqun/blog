package com.china.soft.chatai.handler;

import cn.hutool.json.JSONObject;
import com.china.soft.chatai.xunfei.WebSocketMessageCallback;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
@Slf4j
public class WebSocketHandler extends WebSocketListener {

    private WebSocket webSocket;

    private String buffer;

    private String lastReceivedText;



    /**
     * 在WebSocket连接成功时设置WebSocket实例
     * @param webSocket
     */
    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public String getLastReceivedText() {
        return lastReceivedText;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        // 设置WebSocket实例
        setWebSocket(webSocket);
        log.info("打开sockt连接");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        JSONObject jsonObject = new JSONObject(text);
        String status = jsonObject.getJSONObject("payload").getJSONObject("choices").getStr("status");
        String receive = jsonObject.getJSONObject("payload")
                .getJSONObject("choices")
                .getJSONArray("text")
                .getJSONObject(0)
                .getStr("content");
        log.info("sockt连接参数 = {}",receive);
        if("0".equals(status)){
            buffer = receive;
        }else if ("1".equals(status)){
            buffer+=receive;
        }else{
            buffer+=receive;
            lastReceivedText = buffer.toString();
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
    }

}
