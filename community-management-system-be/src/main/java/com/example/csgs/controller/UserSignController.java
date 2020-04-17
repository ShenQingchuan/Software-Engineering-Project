package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.LoginState;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/sign")
@Slf4j
public class UserSignController {
    final UserSignService userSignService;

    public UserSignController(UserSignService userSignService) {
        this.userSignService = userSignService;
    }

    /**
     * 登录接口
     */
    @PostMapping("/signIn")
    public Object login(@RequestBody JSONObject jsonObject, HttpServletResponse response) {

        String userAccount = jsonObject.getString("userAccount");
        String password = jsonObject.getString("password");

        Map<Integer, String> integerStringMap = userSignService.sign(userAccount, password, response);
        Set<Integer> integers = integerStringMap.keySet();

        for (Integer integer : integers) {
            switch (integer) {
                case LoginState.STATE_SUCCESS:
                    log.info("用户 <" + userAccount + "> 登录成功!");
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", integerStringMap.get(integer));
                    return ResultUtils.success(map, "登录成功"); // 登录成功
                case LoginState.STATE_FAIL:
                    return ResultUtils.error("密码错误，请重新输入");
                case LoginState.STATE_UNREGISTER:
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
        if (userSignService.signOut(request)) {
            return ResultUtils.success("退出登录完成！");
        }
        return ResultUtils.success("退出登录失败！");
    }
}
