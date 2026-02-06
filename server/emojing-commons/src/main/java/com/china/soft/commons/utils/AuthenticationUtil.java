package com.china.soft.commons.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Description: 认证工具类
 * @author MingYue
 * @Date 2023/9/17
 */
public class AuthenticationUtil {

    private static String tokenSalt = "_~SAASTokenToAES";

    private static String passwordSalt = "_SAASPassword~";

    private static String passwordAESKye = "_SAASAESPassword";

    private static String tokenHeader = "Authorization";

    public static String getTokenCode(String username){
        return DigestUtils.md5Hex(username + tokenSalt);
    }

    public static String getTokenAESen(String username,String uuid){
        String result = null;
        try {
            result = Aes.aesEncrypt(uuid + "_" +getTokenCode(username) , tokenSalt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getPasswordAESde(String password){
        String result = null;
        try {
            result = Aes.aesDecrypt(password, passwordAESKye);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String[] getTokenAESde(String tokenCode){
        String [] result = null;
        try {
            String code = Aes.aesDecrypt(tokenCode, tokenSalt);
            result = code.split("_");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getPassword (String password){
        return DigestUtils.md5Hex(password+passwordSalt);
    }

    public static String getTokenHeader(){
        return tokenHeader;
    }

}
