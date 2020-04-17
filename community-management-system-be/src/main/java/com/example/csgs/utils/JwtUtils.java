package com.example.csgs.utils;

import com.example.csgs.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    private static final String SUBJECT = "csgs_space";//签名发行者

    private static final String APPSECRET = "csgsSpace2020";//签名

    public static final Integer TOKEN_EXPIRE_TIME = 3600; //过期时间

    /**
     * 生成token
     */
    public static String genJsonWebToken(UserEntity userEntity) {
        String token;
        if (userEntity != null) {
            token = Jwts.builder()
                    .setSubject(SUBJECT)//发行者
                    .claim("userType", userEntity.getUserType())
                    .claim("userAccount", userEntity.getUserAccount())
                    .setIssuedAt(new Date())//发行日期
                    .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();//签名
        } else {
            token = "";
        }
        return token;
    }

}