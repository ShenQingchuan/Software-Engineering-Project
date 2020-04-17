package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.PageQuery;

import com.example.csgs.entity.UserEntity;
import com.example.csgs.service.UserQueryService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@Slf4j
public class UserQueryController {
    @Autowired
    UserQueryService userQueryService;

    /**
     * 按 id 信息查询用户
     * 场景：用户进入自己的资料查看界面
     */
    @GetMapping("/{id}")
    public Object singleUserQuery(@PathVariable String id) {
        Long uid = Long.parseLong(id);
        UserEntity userEntity = userQueryService.singleUserQuery(uid);
        if (userEntity != null) {
            return ResultUtils.success(userEntity, "用户个人资料获取成功");
        }
        return ResultUtils.error("没有找到 uid: " + uid + " 的用户");
    }

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     */
    @GetMapping("/allUserOfGrid/{id}")
    public Object allUserOfGrid(@PathVariable String id, @RequestParam String page) {
        Long uid = Long.parseLong(id);
        PageQuery pageQuery = userQueryService.allUserOfGrid(uid, page);
        if (pageQuery != null) {
            return ResultUtils.success(pageQuery, "居民用户信息获取成功！");
        }
        return ResultUtils.error("居民用户信息获取失败！");
    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：归属地区、归属地区和归属小区、
     */
    @GetMapping("/multipleConditions/{id}")
    public Object multipleConditions(@RequestBody JSONObject jsonObject, @PathVariable String id, @RequestParam String page) {
        String userID = jsonObject.getString("userID");
        String userName = jsonObject.getString("userName");
        String community = jsonObject.getString("community");

        Object result = userQueryService.multipleConditions(userID, userName, community, Long.parseLong(id), page);
        if (result instanceof UserEntity) {
            UserEntity userEntity = (UserEntity) result;
            return ResultUtils.success(userEntity, "居民用户信息获取成功");
        } else if (result instanceof PageQuery) {
            PageQuery pageQuery = (PageQuery) result;
            return ResultUtils.success(pageQuery, "居民用户信息获取成功！");
        } else if (result instanceof Integer){
            return ResultUtils.error("你没有该查询权限！");
        } else {
            return ResultUtils.error("居民用户信息获取失败！");
        }
    }
}
