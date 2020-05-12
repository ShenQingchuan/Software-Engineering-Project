package com.example.csgs.service;

import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.entity.DistrictInfo;

import java.util.List;

public interface LeaderViewService {
    List<DistrictInfo> getDistrictRSHList();

    List<CommunityInfo> getCommunityRPHList(Long id);
}
