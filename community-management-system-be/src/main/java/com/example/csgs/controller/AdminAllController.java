package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.AreaList;
import com.example.csgs.entity.CreateGridInfo;
import com.example.csgs.entity.GridPersonalInfo;
import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.ProfileInfo;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.AdminAllService;
import com.example.csgs.utils.IsIntegerUtil;
import com.example.csgs.utils.ResultUtil;
import lombok.extern.log4j.Log4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Log4j
public class AdminAllController {
    @Resource
    AdminAllService adminAllService;
    @Resource
    UserMapper userMapper;
    @Resource
    ProfileMapper profileMapper;
    @Resource
    RestHighLevelClient restHighLevelClient;
    /**
     * 获取用户资料接口
     * 场景：系统管理员添加网格员时，需要查看该居民用户的基本资料信息
     */
    @GetMapping("/getGridProfile")
    public Object getMaterial(@RequestParam String userID) {

        UserEntity userEntity = userMapper.findOneByUserID(userID);
        if (userEntity != null && userEntity.getUserType() == 1) {
            log.info("[系统管理员]用户 <" + userID + "> 不是一名网格员!");
            return ResultUtil.error("该用户已经是一名网格员！！！");
        }else if (userEntity != null){
            ProfileInfo material = profileMapper.getMaterial(userEntity.getId());
            if (material != null) {
                log.info("[系统管理员]用户 <" + userID + "> 资料获取Success!");
                return ResultUtil.success(material, "用户资料获取成功！");
            }
        }
        log.info("[系统管理员]用户 <" + userID + "> 资料获取Failure!");
        return ResultUtil.error("用户资料不存在！");
    }
    /**
     * 场景：系统管理员新增网格员，首先要获取区域数据信息，然后在其中选择区域分配网格员
     * 重点：这里我们返回的区域是还没有被划分的区域，如果某一区域已经被分配，那么不返回该区域信息
     */
    @GetMapping("/getAreaList")
    public Object getAreaList(@RequestParam String userID) {
        AreaList areaList = adminAllService.getAreaList(userID);
        if (areaList != null) {
            log.info("[系统管理员]用户 <" + userID + "> 获取"+ areaList.getDistrictName() +"所在区域数据信息获取Success！");
            return ResultUtil.success(areaList, "获取"+ areaList.getDistrictName() +"区域数据信息成功！");
        }
        log.info("[系统管理员]用户 <" + userID + "> 所在区域数据信息获取Failure(改用户不存在)！");
        return ResultUtil.error("获取区域数据信息失败(该用户不存在)！");
    }

    /**
     * 场景：系统管理员在修改网格员管理区域之前，首先要获取所有未被分配管理的区域数据信息，然后在其中选择区域分配给网格员
     */
    @GetMapping("/getAllAreaList")
    public Object getAllAreaList() {
        List<AreaList> allAreaList = adminAllService.getAllAreaList();
        if (!allAreaList.isEmpty()) {
            log.info("[系统管理员]获取所有区域数据信息Success！");
            return ResultUtil.success(allAreaList, "获取所有区域数据信息成功！");
        }
        log.info("[系统管理员]获取所有区域数据信息Failure！");
        return ResultUtil.error("获取所有区域数据信息失败！");
    }

    /**
     * 新增网格员接口
     * 同时系统管理员为网格员分配管理区域
     */
    @PostMapping("/addGrid")
    public Object addGrid(@RequestBody JSONObject jsonObject) {
        CreateGridInfo createGridInfo = JSONObject.toJavaObject(jsonObject, CreateGridInfo.class);

        if (adminAllService.addGrid(createGridInfo)) {
            log.info("[系统管理员]添加身份证号为<"+ createGridInfo.getUserID() +">网格员身份Success！");
            return ResultUtil.success("添加网格员操作成功！");
        }
        log.info("[系统管理员]添加身份证号为<"+ createGridInfo.getUserID() +">网格员身份Failure！");
        return ResultUtil.error("添加网格员操作失败！");
    }

    /**
     * 返回当前系统管理员添加的网格员数据列表
     */
    @GetMapping("/getGrids")
    public Object getAllGrids(@RequestParam String page) {
        PageQuery<GridPersonalInfo> allGrids = adminAllService.getAllGrids(page);
        if (allGrids != null) {
            log.info("[系统管理员]获取所有网格员信息Success！");
            return ResultUtil.success(allGrids, "获取网格员数据列表成功！");
        }
        log.info("[系统管理员]获取所有区域数据信息Failure！");
        return ResultUtil.error("获取网格员数据列表失败！");
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
        log.info(areaList.toString());
        if (IsIntegerUtil.isInteger(id) && adminAllService.modifyGrid(areaList,Long.parseLong(id))) {
            log.info("[系统管理员]修改网格员id:<"+ Long.parseLong(id) +">Success！");
            return ResultUtil.success("网格员管理区域修改成功！");
        }
        log.info("[系统管理员]修改网格员id:<"+ Long.parseLong(id) +">Failure！");
        return ResultUtil.error("网格员管理区域修改失败！");
    }

    /**
     * 功能：删除某一网格员（user表中任然存在，只是grid表中不存在了）
     */
    @DeleteMapping("/deleteOneGrid/{id}")
    public Object deleteOneGrid(@PathVariable String id) {
        if (IsIntegerUtil.isInteger(id) && adminAllService.deleteOneGrid(Long.parseLong(id))) {
            log.info("[系统管理员]删除网格员id:<"+ Long.parseLong(id) +"Success！");
            return ResultUtil.success("删除id为"+ id +"的网格员操作成功！");
        }
        log.info("[系统管理员]删除网格员id:<"+ Long.parseLong(id) +"Failure！");
        return ResultUtil.error("删除id为"+ id +"的网格员操作失败！");
    }
}
