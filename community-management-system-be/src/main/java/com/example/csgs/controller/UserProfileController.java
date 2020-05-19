package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.ProfileInfo;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.service.UserProfileService;
import com.example.csgs.utils.AuthorityUtils;
import com.example.csgs.utils.IsInteger;
import com.example.csgs.utils.RedisUtils;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/profile")
@Log4j
public class UserProfileController {
    @Resource
    UserProfileService userProfileService;
    @Resource
    ProfileMapper profileMapper;
    @Resource
    RedisUtils redisUtils;
    /**
     * 修改资料接口
     * 场景：用户进入资料编辑界面，对可修改的信息进行修改
     */

    @PostMapping("/modify/{id}")
    public Object updateMaterial(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable String id) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id,redisUtils) &&
                userProfileService.updateMaterial(jsonObject, Long.parseLong(id))) {
            log.info("用户<"+ id +">资料修改成功！");
            return ResultUtils.success("用户资料修改成功！");
        }
        log.info("用户<"+ id +">资料修改失败！");
        return ResultUtils.error("用户资料修改失败！");
    }

    /**
     * 获取用户资料接口
     * 场景：用户进入资料编辑界面，展示资料信息
     */
    @GetMapping("/getProfile/{id}")
    public Object getMaterial(HttpServletRequest request, @PathVariable String id) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id,redisUtils)) {
            ProfileInfo material = profileMapper.getMaterial(Long.parseLong(id));
            if (material != null) {
                log.info("用户<" + id + ">获取资料成功！");
                return ResultUtils.success(material, "用户资料获取成功！");
            }
        }
        log.info("用户<"+ id +">获取资料失败！");
        return ResultUtils.error("用户资料获取失败！");
    }

}
