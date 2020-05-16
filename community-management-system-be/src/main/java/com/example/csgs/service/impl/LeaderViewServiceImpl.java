package com.example.csgs.service.impl;

import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.DistrictEntity;
import com.example.csgs.entity.DistrictInfo;
import com.example.csgs.mapper.CommunityInfoMapper;
import com.example.csgs.mapper.DistrictMapper;
import com.example.csgs.service.LeaderViewService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class LeaderViewServiceImpl implements LeaderViewService {
    final DistrictMapper districtMapper;
    final CommunityInfoMapper communityInfoMapper;

    public LeaderViewServiceImpl(DistrictMapper districtMapper, CommunityInfoMapper communityInfoMapper) {
        this.districtMapper = districtMapper;
        this.communityInfoMapper = communityInfoMapper;
    }

    /**
     * 场景：此时，领导想要看的的整体各项数据（以区为单位展示数据信息内容）
     * 所以，我们要整理出每个区下，Residents、Houses、ParkingSpaces各自总和
     */
    @Override
    public List<DistrictInfo> getDistrictRSHList() {
        List<DistrictEntity> districtEntities = districtMapper.findAllDistrict();
        List<DistrictInfo> districtInfoList = new ArrayList<>();

        for (DistrictEntity districtEntity : districtEntities) {
            List<CommunityInfoEntity> communityInfoEntities =
                    communityInfoMapper.findByDistrictID(districtEntity.getId());
            if (!communityInfoEntities.isEmpty()) {
                int allNumResidents = 0, allNumParkingSpaces = 0, allNumHouses = 0;

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
     * @param id district表中的区的id
     */
    @Override
    public List<CommunityInfo> getCommunityRPHList(Long id) {
        DistrictEntity districtEntity = districtMapper.findById(id);
        if (districtEntity != null) {
            List<CommunityInfoEntity> communityInfoEntities =
                    communityInfoMapper.findByDistrictID(districtEntity.getId());
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

