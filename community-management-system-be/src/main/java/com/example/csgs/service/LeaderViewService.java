package com.example.csgs.service;

import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.entity.DistrictInfo;

import java.util.List;

public interface LeaderViewService {
    /**
     * 场景：此时，领导想要看的的整体各项数据（以区为单位展示数据信息内容）
     * 所以，我们要整理出每个区下，Residents、Houses、ParkingSpaces各自总和
     */
    List<DistrictInfo> getDistrictRSHList();

    /**
     * 场景：此时，领导查看的数据形式为：选择某一区对车位数、居民数、住宅数可视化数据查看
     * 所以从前端接受到内容主要是“在District表中区所对应的id号”
     * @param id district表中的区的id
     */
    List<CommunityInfo> getCommunityRPHList(Long id);
}
