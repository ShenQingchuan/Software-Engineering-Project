package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.entity.ProfileInfo;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.service.UserProfileService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Slf4j
public class UserProfileController {
    final UserProfileService userProfileService;
    final ProfileMapper profileMapper;

    public UserProfileController(UserProfileService userProfileService, ProfileMapper profileMapper) {
        this.userProfileService = userProfileService;
        this.profileMapper = profileMapper;
    }

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
        ProfileInfo material = profileMapper.getMaterial(Long.parseLong(id));
        if (material != null) {
            return ResultUtils.success(material, "用户资料获取成功！");
        }
        return ResultUtils.error("用户资料获取失败！");
    }

    /**
     * 场景：居民用户登陆，居民首页呈现自己所住小区名、房屋数量、停车位数量、居民数量
     * 和网格员向本小区发送的公告信息
     * 注意：当前id是居民用户的id
     */
    @GetMapping("/CommunityInfo/{id}")
    public Object queryCommunityInfo(@PathVariable String id,@RequestParam String page) {
        CommunityInfo communityInfo = userProfileService.findCommunityInfo(Long.parseLong(id),page);
        if (communityInfo != null) {
            return ResultUtils.success(communityInfo, "用户所在社区信息获取成功！");
        }
        return ResultUtils.error("用户所在社区信息获取失败！");
    }

}
