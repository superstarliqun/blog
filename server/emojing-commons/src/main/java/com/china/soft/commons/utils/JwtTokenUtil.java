package com.china.soft.commons.utils;

import com.china.soft.commons.exception.InvalidJwtException;
import io.jsonwebtoken.*;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtTokenUtil {

    // 注意：HS256的密钥建议至少32位（你的密钥长度足够，但建议用更随机的字符串，不要硬编码）
    private static final String SECRET_KEY = "myu8gKz2nFq9cR4jA+BwTx0LpD6VsYhXeEoC/3tZmGw=";

    private static final Key SIGNING_KEY = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    // Access Token过期时间：5分钟
    private static final long ACCESS_TOKEN_EXPIRE = 5 * 60 * 1000L;

    // Refresh Token过期时间：90天（你代码里写的90天，注释写的7天，统一修正）
    private static final long REFRESH_TOKEN_EXPIRE = 90L * 24 * 60 * 60 * 1000L;

    /**
     * 生成Access Token
     */
    public static String generateAccessToken(String userId, String username, List<String> roles) {
        return generateToken(userId, username, roles, ACCESS_TOKEN_EXPIRE, null);
    }

    /**
     * 生成Refresh Token
     */
    public static String generateRefreshToken(String userId) {
        String jti = UUID.randomUUID().toString();
        return generateToken(userId, null, null, REFRESH_TOKEN_EXPIRE, jti);
    }

    /**
     * 通用生成Token方法
     */
    private static String generateToken(String userId, String username, List<String> roles, long expireMillis, String jti) {
        long nowMillis = System.currentTimeMillis();
        Date issuedAt = new Date(nowMillis);
        Date expiration = new Date(nowMillis + expireMillis);

        JwtBuilder builder = Jwts.builder()
                .setSubject(userId)          // sub: 用户ID
                .setIssuedAt(issuedAt)       // iat: 签发时间
                .setExpiration(expiration)   // exp: 过期时间
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256);

        // 自定义声明：Access Token需要用户名和角色，Refresh Token不需要
        if (username != null) {
            builder.claim("username", username);
        }
        if (roles != null && !roles.isEmpty()) {
            builder.claim("roles", roles);
        }
        // Refresh Token需要jti（用于吊销）
        if (jti != null) {
            builder.setId(jti);
        }

        return builder.compact();
    }

    /**
     * 完整的Token验证 + 解析（核心修正：加校验+异常处理）
     * @param token {@link #generateToken}
     */
    public static Claims validateAndParseToken(String token) throws InvalidJwtException {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();
            // TODO @MingYue：这里是否需要验证token是否过期,只有长token判断是否过期
            // if (claims.getExpiration().before(new Date())) {
            //     throw new ExpiredJwtException(jws.getHeader(), claims, "Token已过期");
            // }
            return claims;
        }catch (Exception e) {
            throw new InvalidJwtException(e.getMessage());
        }
    }

    /**
     * 从Claims中获取用户信息（便捷方法）
     */
    public static String getUserId(Claims claims) {
        return claims.getSubject();
    }

    public static String getUsername(Claims claims) {
        return claims.get("username", String.class);
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRoles(Claims claims) {
        return claims.get("roles", List.class);
    }

    // 加盐生成TokenCode
    private static String tokenSalt = "_~SAASTokenToAES";
    public static String getTokenCode(String username) {
        return DigestUtils.md5Hex(username + tokenSalt);
    }
}