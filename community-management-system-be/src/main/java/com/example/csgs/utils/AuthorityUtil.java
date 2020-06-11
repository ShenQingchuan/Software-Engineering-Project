package com.example.csgs.utils;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;

@Log4j
public class AuthorityUtil {
    public static boolean authority(HttpServletRequest request, String id, RedisUtil redisUtil) {
        String token = request.getHeader("csgs_token");
        if (!"".equals(token)) {
            Claims claims = JwtUtil.parserToken(token);  //解析token
            if (claims != null) {
                String uid = String.valueOf(claims.get("id"));
                String sessionID = request.getHeader("Authorization");
                Subject subject = SecurityUtils.getSubject();

                String tokenId = (String) redisUtil.get(token);
                log.info("判定用户令牌信息是否跟请求信息一致!");
                return !"".equals(sessionID) && sessionID.equals(subject.getSession().getId())
                        && !"".equals(uid) && id.equals(uid) && !"".equals(tokenId) && tokenId.equals(id);
            }
        }
        return false;
    }
}
