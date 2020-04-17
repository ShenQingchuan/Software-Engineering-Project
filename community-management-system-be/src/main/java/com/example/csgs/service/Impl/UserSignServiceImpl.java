package com.example.csgs.service.Impl;

import com.example.csgs.bean.LoginState;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.JwtUtils;
import com.example.csgs.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserSignServiceImpl implements UserSignService {
    @Autowired
    UserDao userDAO;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public Map<Integer, String> sign(String userID, String password, HttpServletResponse response) {
        Optional<UserEntity> userEntitySrc = userDAO.findOneByUserID(userID);
        Map<Integer, String> list = new HashMap<>();
        if (userEntitySrc.isPresent()) { // 判断用户是否存在
//            if (DigestUtils.md5DigestAsHex(password.getBytes()).equals(userEntitySrc.get().getUserPassword())) { // 校验密码是否一致
            if (password.equals(userEntitySrc.get().getUserPassword())) { // 校验密码是否一致
                String token = JwtUtils.genJsonWebToken(userEntitySrc.get()); // 得到 Token
                // 登录成功后 把token放到Redis Key 存 token ，value 存用户userType
                redisUtils.set(token, userEntitySrc.get().getUserType().toString(), JwtUtils.TOKEN_EXPIRE_TIME);
                //登陆成功后 把token和真实姓名返回
                Cookie tokenCookie = new Cookie("csgs_token", token);
                response.addCookie(tokenCookie);
                list.put(LoginState.STATE_SUCCESS, token);
                return list;
            }
            list.put(LoginState.STATE_FAIL, "0");
        } else {
            list.put(LoginState.STATE_UNREGISTER, "1");
        }
        return list;
    }

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

