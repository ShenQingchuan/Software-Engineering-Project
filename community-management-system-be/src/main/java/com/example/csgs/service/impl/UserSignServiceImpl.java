package com.example.csgs.service.impl;

import com.example.csgs.bean.LoginState;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.JwtUtils;
import com.example.csgs.utils.RedisUtils;
import com.example.csgs.utils.SHA256Util;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Log4j
@Service
public class UserSignServiceImpl implements UserSignService {
    final UserMapper userMapper;
    final RedisUtils redisUtils;

    public UserSignServiceImpl(UserMapper userMapper, RedisUtils redisUtils) {
        this.userMapper = userMapper;
        this.redisUtils = redisUtils;
    }

    /**
     * 用户登陆
     *
     * @param userID   用户身份证号
     * @param password 登陆密码（已经经过前端加密）
     * @param response 携带返回后端生成的Cookie
     */
    @Override
    public Map<Integer, String> sign(String userID, String password, HttpServletResponse response) {
        UserEntity userEntity = userMapper.findOneByUserID(userID);
        Map<Integer, String> list = new HashMap<>();
        if (userEntity != null) { // 判断用户是否存在x
            String sha256String = SHA256Util.getSHA256String(password);
            if (sha256String.equals(userEntity.getUserPassword())) { // 校验密码是否一致
                String token = JwtUtils.genJsonWebToken(userEntity); // 得到 Token
                // 登录成功后 把token放到Redis Key 存 token ，value 存用户userType
                redisUtils.set(token, userEntity.getUserID(), JwtUtils.TOKEN_EXPIRE_TIME);
                //登陆成功后 把token和真实姓名返回
                Cookie tokenCookie = new Cookie("csgs_token", token);
                response.addCookie(tokenCookie);
                list.put(LoginState.STATE_SUCCESS, token);
                return list;
            }
            list.put(LoginState.STATE_FAIL, "0");
        } else {
            list.put(LoginState.STATE_UNExist, "1");
        }
        return list;
    }

    /**
     * 退出登陆
     * @param request 携带Cookie至后端，清除redis缓存的token
     */
    @Override
    public boolean signOut(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (request.getCookies() != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("csgs_token")) { //找到 Token Cookie
                    if (redisUtils.hasKey(c.getValue())) {
                        log.info("用户 uid: " + redisUtils.get(c.getValue()) + " 退出登录...");
                        redisUtils.remove(c.getValue());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

