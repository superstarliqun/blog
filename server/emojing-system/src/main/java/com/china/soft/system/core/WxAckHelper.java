package com.china.soft.system.core;

import cn.hutool.core.util.RandomUtil;
import com.china.soft.chatai.vo.WxRequestResultVo;
import com.china.soft.chatai.vo.WxResponseResultVo;
import com.china.soft.system.api.service.SysUserService;
import com.china.soft.system.util.CodeGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author YiHui
 * @date 2022/9/5
 */
@Slf4j
@Component
public class WxAckHelper {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WxLoginHelper qrLoginHelper;

    /**
     * 返回自动响应的文本
     *
     * @return
     */
    public WxResponseResultVo buildResponseBody(WxRequestResultVo result,String userKey) {
        // 返回的文本消息
        String textRes = null;
        // 返回的是图文消息
        if ("subscribe".equalsIgnoreCase(result.getMsgType())) {
            // 订阅
            textRes = "哎妈呀，打小就看这孩子有见识，感谢你的关注哈\n" +
                    "\n" +
                    "后续还有什么文章以及新的技术会推送给你哦" +
                    "\n";
        }
        // 下面是回复图文消息
        else if ("商务合作".equalsIgnoreCase(result.getContent())) {
            textRes = "商务合作（非诚勿扰）：请添加小编微信 SUPERSTARLIQUN 备注\"商务合作\"'";
        }else if ("验证码".equalsIgnoreCase(result.getContent())) {
            String randomCode = RandomUtil.randomNumbers(6);
            String msg = MessageFormat.format("【SUPERSTAR干货铺】您的本次验证码为：{0}。验证码有效期为30分钟，请及时使用。", randomCode);
            textRes = msg;
        }
        // 微信公众号登录
        else if (CodeGenerateUtil.isVerifyCode(result.getContent())) {
            sysUserService.autoRegisterWxUserInfo(result.getFromUserName());
            if (qrLoginHelper.login(result.getContent(),userKey)) {
                textRes = "登录成功！！";
            } else {
                textRes = "验证码过期了，刷新登录页面重试一下吧";
            }
        } else {
            textRes = "其他的我识别不了，我只知道你说了："+result.getContent();
        }
        WxResponseResultVo vo = new WxResponseResultVo();
        vo.setToUserName(result.getFromUserName());
        vo.setCreateTime(result.getCreateTime());
        vo.setFromUserName(result.getToUserName());
        vo.setMsgType(result.getMsgType());
        vo.setContent(textRes);
        return vo;
    }
}
