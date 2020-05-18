package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.ProfileInfo;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.service.UserProfileService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@Log4j
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

}
