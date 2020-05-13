package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.AreaList;
import com.example.csgs.bean.CreateGridInfo;
import com.example.csgs.bean.GridPersonalInfo;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.ProfileInfo;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.AdminAllService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminAllController {
    final AdminAllService adminAllService;
    final UserMapper userMapper;
    final ProfileMapper profileMapper;

    public AdminAllController(AdminAllService adminAllService, UserMapper userMapper, ProfileMapper profileMapper) {
        this.adminAllService = adminAllService;
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
    }

    /**
     * 获取用户资料接口
     * 场景：系统管理员添加网格员时，需要查看该居民用户的基本资料信息
     */
    @GetMapping("/getGridProfile")
    public Object getMaterial(@RequestBody JSONObject jsonObject) {
        String userID = jsonObject.getString("userID");
        UserEntity userEntity = userMapper.findOneByUserID(userID);
        if (userEntity != null && userEntity.getUserType() == 1) {
            return ResultUtils.error("该用户已经是一名网格员！！！","404");
        }else if (userEntity != null){
            ProfileInfo material = profileMapper.getMaterial(userEntity.getId());
            if (material != null) {
                return ResultUtils.success(material, "用户资料获取成功！");
            }
        }
        return ResultUtils.error("用户资料不存在！");
    }
    /**
     * 场景：系统管理员新增网格员，首先要获取区域数据信息，然后在其中选择区域分配网格员
     * 重点：这里我们返回的区域是还没有被划分的区域，如果某一区域已经被分配，那么不返回该区域信息
     */
    @GetMapping("/getAreaList")
    public Object getAreaList(@RequestBody JSONObject jsonObject) {
        String userID = jsonObject.getString("userID");
        AreaList areaList = adminAllService.getAreaList(userID);
        if (areaList != null) {
            return ResultUtils.success(areaList, "获取"+ areaList.getDistrictName() +"区域数据信息成功！");
        }
        return ResultUtils.error("获取区域数据信息失败(该用户已经是一名网格员)！");
    }

    /**
     * 场景：系统管理员在修改网格员管理区域之前，首先要获取所有未被分配管理的区域数据信息，然后在其中选择区域分配给网格员
     */
    @GetMapping("/getAllAreaList")
    public Object getAllAreaList() {
        List<AreaList> allAreaList = adminAllService.getAllAreaList();
        if (!allAreaList.isEmpty()) {
            return ResultUtils.success(allAreaList, "获取所有区域数据信息成功！");
        }
        return ResultUtils.error("获取所有区域数据信息失败！");
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
     * id是grid表中的id
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
    @DeleteMapping("/deleteOneGrid/{id}")
    public Object deleteOneGrid(@PathVariable String id) {
        if (adminAllService.deleteOneGrid(Long.parseLong(id))) {
            return ResultUtils.success("删除id为"+ id +"的网格员操作成功！");
        }
        return ResultUtils.error("删除id为"+ id +"的网格员操作失败！");
    }
}
