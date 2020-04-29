package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.*;
import com.example.csgs.service.AdminAllService;
import com.example.csgs.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminAllController {
    @Autowired
    AdminAllService adminAllService;

    /**
     * 场景：系统管理员新增网格员，首先要获取区域数据信息，然后在其中选择区域分配网格员
     * 重点：这里我们返回的区域是，还没有被划分的区域，如果某一区域已经被分配，那么不返回该区域信息
     */
    @GetMapping("/getAreaList")
    public Object getAreaList() {
        List<AreaList> areaList = adminAllService.getAreaList();
        if (!areaList.isEmpty()) {
            return ResultUtils.success(areaList, "获取区域数据信息成功！");
        }
        return ResultUtils.error("获取区域数据信息失败！");
    }

    /**
     * 新增网格员接口
     * 同时系统管理员为网格员分配管理区域
     */
    @PostMapping("/addGrid")
    public Object addGrid(@RequestBody JSONObject jsonObject) {
        CreateGridInfo createGridInfo = JSONObject.toJavaObject(jsonObject, CreateGridInfo.class);

        if (adminAllService.addGrid(createGridInfo)) {
            return ResultUtils.success("添加网格员操作成功！");
        }
        return ResultUtils.error("添加网格员操作失败！");
    }

    /**
     * 返回当前系统管理员添加的网格员数据列表
     */
    @GetMapping("/getGrids")
    public Object getAllGrids(@RequestParam String page) {
        PageQuery<GridPersonalInfo> allGrids = adminAllService.getAllGrids(page);
        if (allGrids != null) {
            return ResultUtils.success(allGrids, "获取网格员数据列表成功！");
        }
        return ResultUtils.error("获取网格员数据列表失败！");
    }

    /**
     * 场景：在系统管理员获取到网格员数据信息列表之后，可以进行两项操作：
     * 选择修改任意网格员管理区域、是否删除该网格员（user表中任然存在，只是grid表中不存在了）
     * 功能：该接口进行管理区域修改
     * id是网网格员的id
     */
    @PutMapping("/modifyAreaOfGrid/{id}")
    public Object modifyGrid(@RequestBody JSONObject jsonObject, @PathVariable String id) {
        AreaList areaList = JSONObject.toJavaObject(jsonObject, AreaList.class);

        if (adminAllService.modifyGrid(areaList,Long.parseLong(id))) {
            return ResultUtils.success("网格员管理区域修改成功！");
        }
        return ResultUtils.error("网格员管理区域修改失败！");
    }

    /**
     * 功能：删除某一网格员（user表中任然存在，只是grid表中不存在了）
     */
    @PutMapping("/deleteOneGrid/{id}")
    public Object deleteOneGrid(@PathVariable String id) {
        if (adminAllService.deleteOneGrid(Long.parseLong(id))) {
            return ResultUtils.success("删除id为"+ id +"的网格员操作成功！");
        }
        return ResultUtils.error("删除id为"+ id +"的网格员操作失败！");
    }
}
