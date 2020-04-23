package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.LoginState;
import com.example.csgs.entity.UserProfile;
import com.example.csgs.service.UserProfileService;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/profile")
@Slf4j
public class UserProfileController {
    @Autowired
    UserProfileService userProfileService;

    /**
     * 修改资料接口
     * 场景：用户进入资料编辑界面，对可修改的信息进行修改
     */

    @PostMapping("/modify/{id}")
    public Object updateMaterial(@RequestBody JSONObject jsonObject, @PathVariable String id) {
        if (userProfileService.updateMaterial(jsonObject, Long.parseLong(id))) {
            return ResultUtils.success("用户资料修改成功！");
        }
        return ResultUtils.error("用户资料修改失败！");
    }

    /**
     * 获取用户资料接口
     * 场景：用户进入资料编辑界面，展示资料信息
     */
    @GetMapping("/getProfile/{id}")
    public Object getMaterial(@PathVariable String id) {

        UserProfile material = userProfileService.getMaterial(Long.parseLong(id));
        if (material != null) {
            return ResultUtils.success(material, "用户资料获取成功！");
        }
        return ResultUtils.error("用户资料获取失败！");
    }

}
