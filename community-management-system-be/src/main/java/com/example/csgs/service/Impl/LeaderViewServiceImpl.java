package com.example.csgs.service.Impl;

import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.dao.CommunityInfoDao;
import com.example.csgs.dao.DistrictDao;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.DistrictEntity;
import com.example.csgs.service.LeaderViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class LeaderViewServiceImpl implements LeaderViewService {
    @Autowired
    DistrictDao districtDao;
    @Autowired
    CommunityInfoDao communityInfoDao;

    /**
     * 场景：此时，领导查看的数据形式为：选择某一区对车位数、居民数、住宅数可视化数据查看
     * 所以从前端接受到内容主要是“在District表中区所对应的id号”
     */
    @Override
    public List<CommunityInfo> getCommunityRPHList(Long id) {
        Optional<DistrictEntity> districtEntity = districtDao.findById(id);
        if (districtEntity.isPresent()) {
            List<CommunityInfoEntity> communityInfoEntities =
                    communityInfoDao.findByDistrictName_toCommunity(districtEntity.get().getDistrictName());
            List<CommunityInfo> communityInfoList = new ArrayList<>();
            if (!communityInfoEntities.isEmpty()) {
                for (CommunityInfoEntity communityInfoEntity : communityInfoEntities) {
                    String communityName = communityInfoEntity.getCommunityName();
                    Long numResidents = communityInfoEntity.getNumResidents();
                    Long numParkingSpaces = communityInfoEntity.getNumParkingSpaces();
                    Long numHouses = communityInfoEntity.getNumHouses();
                    CommunityInfo communityInfo = new CommunityInfo(communityName, numHouses, numResidents, numParkingSpaces);
                    communityInfoList.add(communityInfo);
                }
            }
            return communityInfoList;
        }
        return null;
    }
}

