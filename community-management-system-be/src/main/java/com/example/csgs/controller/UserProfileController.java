package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.ProfileInfo;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.service.UserProfileService;
import com.example.csgs.utils.AuthorityUtil;
import com.example.csgs.utils.IsIntegerUtil;
import com.example.csgs.utils.RedisUtil;
import com.example.csgs.utils.ResultUtil;
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
    RedisUtil redisUtil;
    /**
     * 修改资料接口
     * 场景：用户进入资料编辑界面，对可修改的信息进行修改
     */

    @PostMapping("/modify/{id}")
    public Object updateMaterial(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable String id) {
        if (IsIntegerUtil.isInteger(id) && AuthorityUtil.authority(request, id, redisUtil) &&
                userProfileService.updateMaterial(jsonObject, Long.parseLong(id))) {
            log.info("用户<"+ id +">资料修改成功！");
            return ResultUtil.success("用户资料修改成功！");
        }
        log.info("用户<"+ id +">资料修改失败！");
        return ResultUtil.error("用户资料修改失败！");
    }

    /**
     * 获取用户资料接口
     * 场景：用户进入资料编辑界面，展示资料信息
     */
    @GetMapping("/getProfile/{id}")
    public Object getMaterial(HttpServletRequest request, @PathVariable String id) {
        if (IsIntegerUtil.isInteger(id)) {
            ProfileInfo material = profileMapper.getMaterial(Long.parseLong(id));
            if (material != null) {
                log.info("用户<" + id + ">获取资料成功！");
                return ResultUtil.success(material, "用户资料获取成功！");
            }
        }
        log.info("用户<"+ id +">获取资料失败！");
        return ResultUtil.error("用户资料获取失败！");
    }

}
