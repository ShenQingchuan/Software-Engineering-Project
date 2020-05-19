package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.LoginState;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.JwtUtils;
import com.example.csgs.utils.RedisUtils;
import com.example.csgs.utils.ResultUtils;
import com.example.csgs.utils.SHA256Util;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/sign")
@Log4j
public class UserSignController {
    final UserSignService userSignService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtils redisUtils;

    public UserSignController(UserSignService userSignService) {
        this.userSignService = userSignService;
    }

    @PostMapping("/login")
    public Object toLogin(@RequestBody JSONObject jsonObject, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        String userID = jsonObject.getString("userID");
        String password = SHA256Util.getSHA256String(jsonObject.getString("password"));

        UsernamePasswordToken token = new UsernamePasswordToken(userID,password);

        try {
            subject.login(token);
            log.info("用户 <" + userID + "> 登录成功!");
            UserEntity userEntity = userMapper.findOneByUserID(userID);
            String tokens = JwtUtils.genJsonWebToken(userEntity); // 得到 Token
            // 登录成功后 把token放到Redis Key 存 token ，value 存用户userType
            redisUtils.set(tokens, userEntity.getUserID(), JwtUtils.TOKEN_EXPIRE_TIME);
            Map<String,String> map = new HashMap<>();
            map.put("token",tokens);
            map.put("sessionID",String.valueOf(subject.getSession().getId()));
            return ResultUtils.success(map,"用户 <" + userID + "> 登录成功!");
        } catch (UnknownAccountException e) {
            return ResultUtils.error("账号 <" + userID + "> 不存在!");
        } catch (IncorrectCredentialsException e) {
            return ResultUtils.error("用户 <" + userID + "> 密码错误!");
        }

    }

    /**
     * 登录接口
     */
    @PostMapping("/signIn")
    public Object login(@RequestBody JSONObject jsonObject, HttpServletResponse response) {
        String userID = jsonObject.getString("userID");
        String password = jsonObject.getString("password");

        Map<Integer, String> integerStringMap = userSignService.sign(userID, password, response);
        Set<Integer> integers = integerStringMap.keySet();

        for (Integer integer : integers) {
            switch (integer) {
                case LoginState.STATE_SUCCESS:
                    log.info("用户 <" + userID + "> 登录成功!");
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", integerStringMap.get(integer));
                    return ResultUtils.success(map, "登录成功"); // 登录成功
                case LoginState.STATE_FAIL:
                    log.info("用户 <" + userID + "> 密码错误!");
                    return ResultUtils.error("密码错误，请重新输入");
                case LoginState.STATE_UNExist:
                    log.info("账号 <" + userID + "> 不存在!");
                    return ResultUtils.error("该账号不存在");
            }
        }
        return null;
    }

    /**
     * 登出接口
     */
    @GetMapping("/signOut")
    public Object logout(HttpServletRequest request) {
//        if (userSignService.signOut(request)) {
//            return ResultUtils.success("退出登录完成！");
//        }
//        return ResultUtils.success("退出登录失败！");
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout();
            return ResultUtils.success("退出成功!");
        } catch (UnknownAccountException e) {
            return ResultUtils.error("退出失败");
        }
    }
}
