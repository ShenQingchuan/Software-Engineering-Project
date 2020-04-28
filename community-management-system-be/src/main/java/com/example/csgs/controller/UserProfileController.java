package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
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

    /**
     * 场景：居民用户登陆，居民首页呈现自己所住小区的房屋、停车位、居民数量
     * 和网格员向本小区发送的公告信息
     * 注意：当前id是居民用户的id
     */
    @GetMapping("/CommunityInfo/{id}")
    public Object queryCommunityInfo(@PathVariable String id) {
        CommunityInfo communityInfo = userProfileService.findCommunityInfo(Long.parseLong(id));
        if (communityInfo != null) {
            //获取用户所在社区发布的公告（网格员所发布）
            return ResultUtils.success(communityInfo,"用户所在社区信息获取成功！");
        }
        return ResultUtils.error("用户所在社区信息获取失败！");
    }

}
