package com.china.soft.commons.enums;

/**
 * @Description: 登录类型
 * @author MingYue
 * @Date 2023/10/12
 */
public enum IdentityType {

    // 登录类型枚举
    OWN ("OWN","系统本身登录"),
    EMAIL ("EMAIL","邮箱"),
    PHONE ("PHONE","手机号"),
    WECHAT ("WECHAT","微信"),
    QQ ("QQ","QQ"),
    GITHUB ("GITHUB","github"),
    ALIPAY ("ALIPAY","支付宝");

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    IdentityType(String code,String name){
        this.code = code;
        this.name = name;
    }
}
