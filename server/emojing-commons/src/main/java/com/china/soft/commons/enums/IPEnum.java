package com.china.soft.commons.enums;

/**
 * @Description: IP枚举
 * @author MingYue
 * @Date 2023/10/7
 */
public enum IPEnum {

    // 测评类型
    LOCAL("127.0.0.1","本地"),
    ONESELF("localhost","本地");

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    IPEnum(String code,String name){
        this.code = code;
        this.name = name;
    }
}
