package com.china.soft.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class CryptoJSCodec {

    public static String decrypt(String cipherText, String password) throws Exception {
        byte[] ctBytes = Base64.getDecoder().decode(cipherText);

        // 1. 提取 Salt (CryptoJS 密文前 8-16 字节通常是 "Salted__" + 8位随机码)
        byte[] salt = Arrays.copyOfRange(ctBytes, 8, 16);
        byte[] encryptedData = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);

        // 2. 派生 Key 和 IV (模拟 OpenSSL 的 EVP_BytesToKey)
        byte[][] keyAndIv = deriveKeyAndIv(password.getBytes(StandardCharsets.UTF_8), salt);
        SecretKeySpec keySpec = new SecretKeySpec(keyAndIv[0], "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyAndIv[1]);

        // 3. 解密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(encryptedData);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static byte[][] deriveKeyAndIv(byte[] password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] keyIv = new byte[48]; // 32 字节 Key + 16 字节 IV
        byte[] lastHash = new byte[0];

        int offset = 0;
        while (offset < keyIv.length) {
            md.update(lastHash);
            md.update(password);
            md.update(salt);
            lastHash = md.digest();
            int copyLen = Math.min(lastHash.length, keyIv.length - offset);
            System.arraycopy(lastHash, 0, keyIv, offset, copyLen);
            offset += copyLen;
        }

        return new byte[][]{
                Arrays.copyOfRange(keyIv, 0, 32),
                Arrays.copyOfRange(keyIv, 32, 48)
        };
    }
}