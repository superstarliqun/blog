package com.china.soft.commons.enums;

/**
 * @Description: 全局定义枚举
 * @author MingYue
 * @Date 2023/10/7
 */
public enum GlobalEnum {

    // 测评类型
    EXAM_LOCAL("local","本地测评"),
    EXAM_YPS("yps","伊普斯测评"),
    // 用户账号状态
    ACCOUNT_NORMAL("0", "正常"),
    ACCOUNT_LOCKED("1", "锁定"),
    ACCOUNT_DISABLED("2", "禁用");


    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    GlobalEnum(String code,String name){
        this.code = code;
        this.name = name;
    }
}
