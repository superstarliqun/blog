package com.china.soft.chatai.xunfei;

/**
 * @author admin
 */
public interface WebSocketMessageCallback {

    /**
     * 触发回调参数
     * @param message
     */
    void onMessageReceived(String message);
}
