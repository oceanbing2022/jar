package com.ohx.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.ohx.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import static com.ohx.common.Constant.*;

@Slf4j
@Component
public class JWTUtil {

    /**
     * 生成token
     * @param user
     * @return
     */
    public String createJWT(User user){

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        SecretKey secretKey = this.createSecretKey();

        Date nowDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.MILLISECOND, (int)EXPIRE_TIME);
        Date expireDate = calendar.getTime();
        
        JwtBuilder builder = Jwts.builder()
                //header
                .setHeaderParam("typ","JWT")        //token类型
                .setHeaderParam("alg","HS256")      //加密方式
                //payload
                .setId(user.getUserId()+"")
                .setSubject(user.getUsername())
                .signWith(signatureAlgorithm,secretKey)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate);
        log.info("nowDate:{}",nowDate);

        return builder.compact();
    }

    /**
     * 解析toekn
     * @param token
     * @return
     */
//    public Claims parseToken(String token){
//        SecretKey secretKey = this.createSecretKey();
//
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody();
//        return claims;
//    }

    //不管是否过期，都返回claims对象
    public Claims parseToken(String token){
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(signKey) // 设置标识名
                    .parseClaimsJws(token)  //解析token
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }

    /**
     * 创建刷新token
     * @param user
     * @Author: 贾榕福
     * @Date: 2022/10/25
     */
    public String createRefreshToken(User user) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        SecretKey secretKey = this.createSecretKey();

        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.MILLISECOND, (int)REFRESH_TIME);
        Date expireDate = calendar.getTime();

        return Jwts.builder()
                //header
                .setHeaderParam("typ","JWT")        //token类型
                .setHeaderParam("alg","HS256")      //加密方式
                //payload
                .setId(user.getUserId()+"")
                .setSubject(user.getUsername())
                .signWith(signatureAlgorithm,secretKey)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .compact();
    }
    
    
    /**
     * 创建密钥
     * @return
     */
    private SecretKey createSecretKey(){
        //本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(signKey);

        // 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。
        SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        
        return secretKey;
    }
    
    public String getSecretKey(){
        SecretKey secretKey = this.createSecretKey();

        return secretKey.toString();
    }


}
