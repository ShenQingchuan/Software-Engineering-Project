package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
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
     * 组合：<归属地区、归属地区>、<归属小区>、<归属地区>、<userID>
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
        } else if (result instanceof Integer) {
            return ResultUtils.error("你没有该查询权限！");
        } else {
            return ResultUtils.error("居民用户信息获取失败！");
        }
    }

    /**
     * 网格员删除自己所管辖区的用户
     * 场景：网格员在查看用户数据列表时，可以删除相应的用户
     * URL中的id是居民用户的id
     */
    @GetMapping("/deleteResident/{uid}")
    public Object deleteUser(@PathVariable String uid) {
        if (userQueryService.deleteUser(Long.parseLong(uid))) {
            return ResultUtils.success("用户删除成功！");
        }
        return ResultUtils.error("用户删除失败！");
    }

}
