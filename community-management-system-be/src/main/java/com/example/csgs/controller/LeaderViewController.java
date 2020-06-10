package com.example.csgs.controller;

import com.example.csgs.entity.CommunityInfo;
import com.example.csgs.entity.DistrictEntity;
import com.example.csgs.entity.DistrictInfo;
import com.example.csgs.mapper.DistrictMapper;
import com.example.csgs.service.LeaderViewService;
import com.example.csgs.utils.IsIntegerUtil;
import com.example.csgs.utils.ResultUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/leader")
@Log4j
public class LeaderViewController {
    @Resource
    DistrictMapper districtMapper;
    @Resource
    LeaderViewService leaderViewService;

    /**
     * 场景：此时，领导想要看的的整体各项数据（以区为单位展示数据信息内容）
     * 所以，我们要整理出每个区下，Residents、Houses、ParkingSpaces各自总和
     */
    @GetMapping("/getDistrictRPHList")
    public Object getDistrictRSHList() {
        List<DistrictInfo> districtRSHList = leaderViewService.getDistrictRSHList();
        if (!districtRSHList.isEmpty()) {
            log.info("领导正在查看总体数据.....");
            return ResultUtil.success(districtRSHList, "获取所有区RPH信息成功！ ");
        }
        log.info("领导获取总体数据信息失败！");
        return ResultUtil.error("获取所有区RPH信息失败！ ");
    }

    /**
     * 场景：领导在选择查看某一片区数据情况时，前端得先从服务器获取所有片区名信息
     * 没有构建相应的service实现方法，主要是太简单。
     * 如果你使用mybatis查询过程比较复杂，可以新建接口方法，并在service中实现
     */
    @GetMapping("/getAllDistrictName")
    public Object getAllDistrictName() {
        List<DistrictEntity> districtEntities = districtMapper.findAllDistrict();
        log.info("领导正在<片区界面>选择查看哪一个区的RPH信息......");
        return ResultUtil.success(districtEntities, "获取所有区名信息成功！");
    }

    /**
     * 场景：此时，领导查看的数据形式为：选择某一区对车位数、居民数、住宅数可视化数据查看
     * 所以从前端接受到内容主要是“在District表中区所对应的id号”
     */
    @GetMapping("/getCommunityRPHList/{id}")
    public Object getCommunityRSHList(@PathVariable String id) {
        if (IsIntegerUtil.isInteger(id)) {
            List<CommunityInfo> communityRPHList = leaderViewService.getCommunityRPHList(Long.parseLong(id));
            if (!communityRPHList.isEmpty()) {
                log.info("获取id<" + id + ">的区所有小区RPH信息成功！ ");
                return ResultUtil.success(communityRPHList, "获取id<" + id + ">的区所有小区RPH信息成功！ ");
            }
        }
        log.info("获取id<" + id + ">的区所有小区RPH信息失败！ ");
        return ResultUtil.error("获取id为" + id + "的区所有小区RPH信息失败！ ");
    }
}
