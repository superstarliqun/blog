package com.china.soft.commons.utils;

import com.china.soft.commons.enums.IdentityType;

/**
 * @Description: 登录账号类型判断
 * @author MingYue
 * @Date 2023/10/12
 */
public class LoginTypeChecker {

    public static String checkLoginType(String input) {

        // 邮箱正则表达式
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        // 手机号正则表达式
        String phonePattern = "^1[3456789]\\d{9}$";

        // 判断是否为邮箱
        if (input.matches(emailPattern)) {
            return IdentityType.EMAIL.getCode();
        }

        // 判断是否为手机号
        if (input.matches(phonePattern)) {
            return IdentityType.PHONE.getCode();
        }

        // 默认判断为用户名
        return IdentityType.OWN.getCode();
    }

    public static void main(String[] args) {
        String input = "17767746537";
        String loginType = checkLoginType(input);
        System.out.println("您使用的登录方式是：" + loginType);
    }

}
