package com.example.csgs.service.Impl;

import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.bean.DistrictInfo;
import com.example.csgs.mapper.CommunityInfoDao;
import com.example.csgs.mapper.DistrictDao;
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
     * 场景：此时，领导想要看的的整体各项数据（以区为单位展示数据信息内容）
     * 所以，我们要整理出每个区下，Residents、Houses、ParkingSpaces各自总和
     */
    @Override
    public List<DistrictInfo> getDistrictRPHList() {
        Iterable<DistrictEntity> districtEntities = districtDao.findAll();
        List<DistrictInfo> districtInfoList = new ArrayList<>();

        for (DistrictEntity districtEntity : districtEntities) {
            List<CommunityInfoEntity> communityInfoEntities =
                    communityInfoDao.findByDistrictName_toCommunity(districtEntity.getDistrictName());
            if (!communityInfoEntities.isEmpty()) {
                int allNumResidents = 0,allNumParkingSpaces = 0,allNumHouses = 0;

                for (CommunityInfoEntity communityInfoEntity : communityInfoEntities) {
                    Long numResidents = communityInfoEntity.getNumResidents();
                    Long numParkingSpaces = communityInfoEntity.getNumParkingSpaces();
                    Long numHouses = communityInfoEntity.getNumHouses();

                    allNumResidents += numResidents;
                    allNumParkingSpaces += numParkingSpaces;
                    allNumHouses += numHouses;
                }
                DistrictInfo districtInfo = new DistrictInfo(districtEntity.getDistrictName(),
                        allNumHouses, allNumResidents, allNumParkingSpaces);
                districtInfoList.add(districtInfo);
            }
        }
        return districtInfoList;
    }

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

