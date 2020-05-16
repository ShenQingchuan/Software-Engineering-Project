package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.User;
import com.example.csgs.service.GridQueryService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@Log4j
public class GridQueryController {
    final GridQueryService gridQueryService;

    public GridQueryController(GridQueryService gridQueryService) {
        this.gridQueryService = gridQueryService;
    }

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     */
    @GetMapping("/allUserOfGrid/{id}")
    public Object allUserOfGrid(@PathVariable String id, @RequestParam String page) {
        Long uid = Long.parseLong(id);
        PageQuery<User> pageQuery = gridQueryService.allUserOfGrid(uid, page);
        if (pageQuery != null) {
            return ResultUtils.success(pageQuery, "居民用户信息获取成功！");
        }
        return ResultUtils.error("居民用户信息获取失败！");
    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：<归属地区、姓名>、<归属小区>、<姓名>、<userID>
     */
    @GetMapping("/multipleConditions/{id}")
    public Object multipleConditions(@RequestBody JSONObject jsonObject, @PathVariable String id, @RequestParam String page) {
        String userID = jsonObject.getString("userID");
        String userName = jsonObject.getString("userName");
        String community = jsonObject.getString("community");

        PageQuery<User> userPageQuery = gridQueryService.multipleConditions(userID, userName, community, Long.parseLong(id), page);
        if (userPageQuery != null) {
            return ResultUtils.success(userPageQuery, "居民用户信息获取成功");
        }
        return ResultUtils.error("居民用户信息获取失败！");
    }

    /**
     * 网格员删除自己所管辖区的用户
     * 场景：网格员在查看用户数据列表时，可以删除相应的用户
     * URL中的id是居民用户的id
     */
    @GetMapping("/deleteResident/{uid}")
    public Object deleteUser(@PathVariable String uid) {
        if (gridQueryService.deleteUser(Long.parseLong(uid))) {
            return ResultUtils.success("用户删除成功！");
        }
        return ResultUtils.error("用户删除失败！");
    }

}
