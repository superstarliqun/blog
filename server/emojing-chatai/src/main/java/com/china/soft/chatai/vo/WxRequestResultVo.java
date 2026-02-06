package com.china.soft.chatai.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
/**
 * @Description: 微信公众号请求结果集
 * @author MingYue
 * @Date 2023/10/19
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxRequestResultVo {

    /**
     * 开发者微信号
     */
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;

    /**
     * 发送者帐号
     */
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;

    /**
     * 消息类型
     */
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;

    /**
     * 消息事件
     */
    @JacksonXmlProperty(localName = "Event")
    private String event;

    /**
     * 生成二维码时穿过的特殊参数
     */
    @JacksonXmlProperty(localName = "EventKey")
    private String eventKey;

    @JacksonXmlProperty(localName = "Ticket")
    private String ticket;

    /**
     * 文本消息
     */
    @JacksonXmlProperty(localName = "Content")
    private String content;

    @JacksonXmlProperty(localName = "MsgId")
    private String msgId;

    @JacksonXmlProperty(localName = "MsgDataId")
    private String msgDataId;

    @JacksonXmlProperty(localName = "Idx")
    private String idx;

}
