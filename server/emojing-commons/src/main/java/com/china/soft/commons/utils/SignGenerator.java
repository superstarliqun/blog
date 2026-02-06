package com.china.soft.commons.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
// 如果您使用的是 Spring Boot 或其他现代框架，可以考虑使用 org.apache.commons.codec.digest.DigestUtils 或 Guava 的 Hashing 类，代码会更简洁。
// 但这里我们使用标准 JDK 库实现。

public class SignGenerator {

    // !!! 替换为您真实的共享密钥 !!!
    private static final String SHARED_SECRET = "YourCustomSecretKey2025"; // 确保与前端保持一致

    /**
     * 根据前端的签名逻辑，在后端生成签名。
     * @param categoryId 前端传来的 categoryId
     * @param timestamp 前端传来的时间戳（毫秒字符串）
     * @return 计算出的 SHA-256 签名（Hex 字符串）
     */
    public static String generateSign(String categoryId, String timestamp) {
        // 1. 构造原始字符串：categoryId + timestamp + SHARED_SECRET
        String rawString = categoryId + timestamp + SHARED_SECRET;

        try {
            // 2. 初始化 SHA-256 算法
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 3. 计算哈希值
            // 将原始字符串编码为字节数组
            byte[] hashBytes = digest.digest(rawString.getBytes(StandardCharsets.UTF_8));

            // 4. 将字节数组转换为十六进制字符串 (与 CryptoJS.enc.Hex 保持一致)
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // 将每个字节转换为两个十六进制字符
                // 使用 & 0xff 确保高位被清除，只保留后 8 位
                // 使用 0x100 | (...) 保证结果总是两位十六进制数
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // 补零
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // SHA-256 是标准算法，通常不会抛出此异常
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证前端传来的签名是否有效。
     * @param categoryId 前端传来的 categoryId
     * @param timestamp 前端传来的时间戳
     * @param receivedSign 前端传来的签名
     * @return 签名是否有效
     */
    public static boolean verifySign(String categoryId, String timestamp, String receivedSign) {
        String generatedSign = generateSign(categoryId, timestamp);
        // 使用 equals 方法进行比较
        return receivedSign != null && receivedSign.equals(generatedSign);
    }
}