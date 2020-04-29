package com.example.csgs.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.bean.OfGrid;
import com.example.csgs.dao.CommunityInfoDao;
import com.example.csgs.dao.ProfileDao;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.entity.UserProfile;
import com.example.csgs.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    UserDao userDao;
    @Autowired
    ProfileDao profileDao;
    @Autowired
    CommunityInfoDao communityInfoDao;

    /**
     * 修改资料接口
     * 场景：用户进入资料编辑界面，对可修改的信息进行修改
     */
    @Override
    public boolean updateMaterial(JSONObject jsonObject, Long id) {

        Optional<UserEntity> byIdUser = userDao.findById(id);
        if (byIdUser.isPresent()) {
            UserProfile userProfile = byIdUser.get().getUserProfile();

            if (jsonObject.containsKey("occupation"))
                profileDao.updateByOccupation(jsonObject.getString("occupation"), userProfile.getId());
            if (jsonObject.containsKey("bloodType"))
                profileDao.updateByBloodType(jsonObject.getString("bloodType"), userProfile.getId());
            if (jsonObject.containsKey("degreeOfEducation"))
                profileDao.updateByDegreeOfEducation(jsonObject.getString("degreeOfEducation"), userProfile.getId());
            if (jsonObject.containsKey("email"))
                profileDao.updateByEmail(jsonObject.getString("email"), userProfile.getId());
            if (jsonObject.containsKey("nation"))
                profileDao.updateByNation(jsonObject.getString("nation"), userProfile.getId());
            if (jsonObject.containsKey("politicCountenance"))
                profileDao.updateByPoliticCountenance(jsonObject.getString("politicCountenance"), userProfile.getId());
            if (jsonObject.containsKey("sex")) profileDao.updateBySex(Integer.parseInt(jsonObject.getString("sex")), userProfile.getId());
            if (jsonObject.containsKey("stature"))
                profileDao.updateByStature(Integer.parseInt(jsonObject.getString("stature")), userProfile.getId());
            if (jsonObject.containsKey("telPhone"))
                profileDao.updateByTelPhone(jsonObject.getString("telPhone"), userProfile.getId());
            if (jsonObject.containsKey("userName"))
                profileDao.updateByUserName(jsonObject.getString("userName"), userProfile.getId());
            if (jsonObject.containsKey("ofGrid")) {
                JSONObject ofGridObject = jsonObject.getJSONObject("ofGrid");
                OfGrid ofGrid = JSONObject.toJavaObject(ofGridObject,OfGrid.class);

                CommunityInfoEntity communityInfoEntity = communityInfoDao.findByOfGrid(ofGrid.getDistrict(),ofGrid.getCommunity());
                if (communityInfoEntity != null) {
                    userProfile.setCommunityInfoEntity(communityInfoEntity);
                }
            }

            byIdUser.get().setUserProfile(userProfile);
            userDao.save(byIdUser.get());
            return true;
        }
        return false;
    }

    /**
     * 获取用户资料接口
     * 场景：用户进入资料编辑界面，展示资料信息
     */
    @Override
    public UserProfile getMaterial(Long id) {
        Optional<UserEntity> byIdUser = userDao.findById(id);
        return (UserProfile) byIdUser.<Object>map(UserEntity::getUserProfile).orElse(null);
    }


    /**
     * 场景：居民用户登陆，居民首页呈现自己所住小区名、房屋数量、停车位数量、居民数量
     * 和网格员向本小区发送的公告信息
     * 注意：当前id是居民用户的id
     */
    @Override
    public CommunityInfo findCommunityInfo(Long id) {
        Optional<UserEntity> targetResidentUser = userDao.findById(id);
        if (targetResidentUser.isPresent()) {
            CommunityInfoEntity communityInfoEntity = targetResidentUser.get().getUserProfile().getCommunityInfoEntity();
            return new CommunityInfo(communityInfoEntity.getCommunityName(),communityInfoEntity.getNumHouses(),
                    communityInfoEntity.getNumResidents(),communityInfoEntity.getNumParkingSpaces());
        }
        return null;
    }

}
