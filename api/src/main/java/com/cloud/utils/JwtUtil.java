package com.cloud.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 *
 * @author AdongYi
 */
public class JwtUtil {

    //有效期为
    /**
     * 7 * 24 * 60 * 60 * 1000L 七天
     */
    public static final Long JWT_TTL = 7 * 24 * 60 * 60 * 1000L;
    /**
     * 设置秘钥明文
     */
    public static final String JWT_KEY = "csuftsap";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     */
    public static String createJWT(String subject) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }


    /**
     * 生成jwt
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     */
    public static String createJWT(String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                //唯一的ID
                .setId(uuid)
                // 主题  可以是JSON数据
                .setSubject(subject)
                // 签发者
                .setIssuer("sap")
                // 签发时间
                .setIssuedAt(now)
                //使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 创建token
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        String jwt = createJWT("fuchuang");
        System.out.println(jwt);
        Claims claims = parseJWT(jwt);
        String subject = claims.getSubject();
        System.out.println(subject);
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析jwt
     *
     * @param jwt jtw字符串
     * @return 负载
     */
    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


    public static String getUsername(HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization");
        return JwtUtil.parseJWT(jwtToken).getSubject();
    }
}