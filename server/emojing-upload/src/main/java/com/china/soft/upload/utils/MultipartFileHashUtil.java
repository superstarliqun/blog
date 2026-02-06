package com.china.soft.upload.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MultipartFile 哈希值计算工具类
 */
public class MultipartFileHashUtil {

    // 缓存大小，分块读取文件，避免大文件占用过多内存
    private static final int BUFFER_SIZE = 8192;

    /**
     * 计算 MultipartFile 的 MD5 哈希值
     * @param file 上传的文件
     * @return MD5 哈希值（32位十六进制字符串）
     * @throws NoSuchAlgorithmException 算法不存在异常
     * @throws IOException 文件读取异常
     */
    public static String getFileMd5(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        return calculateFileHash(file, "MD5");
    }

    /**
     * 计算 MultipartFile 的 SHA-256 哈希值
     * @param file 上传的文件
     * @return SHA-256 哈希值（64位十六进制字符串）
     * @throws NoSuchAlgorithmException 算法不存在异常
     * @throws IOException 文件读取异常
     */
    public static String getFileSha256(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        return calculateFileHash(file, "SHA-256");
    }

    /**
     * 核心哈希计算方法
     * @param file 上传的文件
     * @param algorithm 哈希算法（如 MD5、SHA-256）
     * @return 哈希值的十六进制字符串
     * @throws NoSuchAlgorithmException 算法不存在异常
     * @throws IOException 文件读取异常
     */
    private static String calculateFileHash(MultipartFile file, String algorithm)
            throws NoSuchAlgorithmException, IOException {
        // 1. 创建指定算法的 MessageDigest 实例
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        // 2. 获取文件输入流，分块读取计算哈希
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        // 3. 将哈希字节数组转换为十六进制字符串
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}