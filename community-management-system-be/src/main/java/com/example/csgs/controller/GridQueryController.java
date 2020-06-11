package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.User;
import com.example.csgs.service.GridQueryService;
import com.example.csgs.utils.IsIntegerUtil;
import com.example.csgs.utils.ResultUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/query")
@Log4j
public class GridQueryController {
    @Resource
    GridQueryService gridQueryService;

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     */
    @GetMapping("/allUserOfGrid/{id}")
    public Object allUserOfGrid(@PathVariable String id, @RequestParam String page) {
        if (IsIntegerUtil.isInteger(id)) {
            PageQuery<User> pageQuery = gridQueryService.allUserOfGrid(Long.parseLong(id), page);
            if (pageQuery != null) {
                log.info("网格员id<" + id + ">获取用户数据列表第<" + page + ">页成功！");
                return ResultUtil.success(pageQuery, "居民用户信息获取成功！");
            }
            if (Integer.parseInt(page) > 1) {
                log.info("已无更多公告！");
                return ResultUtil.error(" 已无更多公告！");
            }
        }
        log.info("网格员id<" + id + ">获取用户数据列表第<" + page + ">页失败！");
        return ResultUtil.error("居民用户信息获取失败！");
    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：<归属小区、姓名>、<归属小区>、<姓名>、<userID>
     */
    @PostMapping("/multipleConditions/{id}")
    public Object multipleConditions(@RequestBody JSONObject jsonObject, @PathVariable String id, @RequestParam String page) throws IOException {
        if (IsIntegerUtil.isInteger(id)) {
            String userID = jsonObject.getString("userID");
            String userName = jsonObject.getString("userName");
            String community = jsonObject.getString("community");

            PageQuery<User> userPageQuery = gridQueryService.multipleConditions(userID, userName, community, Long.parseLong(id), page);
            if (userPageQuery != null) {
                log.info("网格员id<" + id + ">查询居民用户信息成功");
                return ResultUtil.success(userPageQuery, "居民用户信息获取成功");
            }
        }
        log.info("网格员id<" + id + ">查询居民用户信息失败");
        return ResultUtil.error("居民用户信息获取失败！");
    }

    /**
     * 网格员删除自己所管辖区的用户
     * 场景：网格员在查看用户数据列表时，可以删除相应的用户
     * URL中的id是居民用户的id
     */
    @GetMapping("/deleteResident/{id}")
    public Object deleteUser(@PathVariable String id) {
        if (IsIntegerUtil.isInteger(id) && gridQueryService.deleteUser(Long.parseLong(id))) {
            log.info("居民<" + id + ">成功被网格员删除！");
            return ResultUtil.success("用户删除成功！");
        }
        log.info("居民<" + id + ">未能被网格员删除！");
        return ResultUtil.error("用户删除失败！");
    }

}
