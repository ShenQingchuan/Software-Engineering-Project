package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.JwtUtil;
import com.example.csgs.utils.RedisUtil;
import com.example.csgs.utils.ResultUtil;
import com.example.csgs.utils.SHA256Util;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sign")
@Log4j
public class UserSignController {
    @Resource
    UserSignService userSignService;
    @Resource
    UserMapper userMapper;
    @Resource
    RedisUtil redisUtil;

    @PostMapping("/signIn")
    public Object toLogin(@RequestBody JSONObject jsonObject) {
        Subject subject = SecurityUtils.getSubject();
        String userID = jsonObject.getString("userID");
        String password = SHA256Util.getSHA256String(jsonObject.getString("password"));
        UsernamePasswordToken token = new UsernamePasswordToken(userID, password);

        try {
            subject.login(token);
            UserEntity userEntity = userMapper.findOneByUserID(userID);
            String tokens = JwtUtil.genJsonWebToken(userEntity);

            redisUtil.set(tokens, String.valueOf(userEntity.getId()), JwtUtil.TOKEN_EXPIRE_TIME);
            Map<String, String> map = new HashMap<>();
            map.put("token", tokens);
            map.put("sessionID", String.valueOf(subject.getSession().getId()));
            log.info("用户<" + userID + "> 登录成功!");
            return ResultUtil.success(map, "用户 <" + userID + "> 登录成功!");
        } catch (UnknownAccountException e) {
            log.info("用户<" + userID + "> 不存在!");
            return ResultUtil.error("用户 <" + userID + "> 不存在!");
        } catch (IncorrectCredentialsException e) {
            log.info("用户<" + userID + "> 密码错误!");
            return ResultUtil.error("用户 <" + userID + "> 密码错误!");
        }

    }

    /**
     * 登出接口
     */
    @GetMapping("/signOut")
    public Object logout(HttpServletRequest request) {
        if (userSignService.signOut(request)) {
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.logout();
                return ResultUtil.success("退出登录Success!");
            } catch (UnknownAccountException e) {
                log.info("退出登陆Failure！");
                return ResultUtil.error("退出登录Failure,sessionID不正确！");
            }
        }
        log.info("退出登录Failure，令牌信息不正确！");
        return ResultUtil.success("退出登录Failure，令牌信息不正确！");
    }
}
