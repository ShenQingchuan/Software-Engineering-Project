package com.example.csgs.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.OfGrid;
import com.example.csgs.dao.GridDao;
import com.example.csgs.dao.ProfileDao;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.OfGridEntity;
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
    GridDao gridDao;

    @Override
    public boolean updateMaterial(JSONObject jsonObject, Long id) {
        /*Set<String> strings = jsonObject.keySet();
        String[] keyName = new String[]{"occupation", "avatarUrl", "bloodType", "degreeOfEducation",
                "email", "nation", "politicCountenance", "sex", "stature", "telPhone", "userName", "ofGrid"};*/
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

                Optional<OfGridEntity> ofGridEntitySrc = gridDao.findByDistrictAndCommunity(ofGrid.getDistrict(), ofGrid.getCommunity());
                if (ofGridEntitySrc.isPresent()) {
                    profileDao.updateByOfGridEntity(ofGridEntitySrc.get(), userProfile.getId());
                }else {
                    OfGridEntity ofGridEntity = new OfGridEntity();
                    ofGridEntity.setCommunity(ofGrid.getCommunity());
                    ofGridEntity.setDistrict(ofGrid.getDistrict());
                    gridDao.save(ofGridEntity);
                    profileDao.updateByOfGridEntity(ofGridEntity,userProfile.getId());
                }
            }

            byIdUser.get().setUserProfile(userProfile);
            userDao.save(byIdUser.get());
            return true;
        }
        return false;
    }

    @Override
    public UserProfile getMaterial(Long id) {
        Optional<UserEntity> byIdUser = userDao.findById(id);
        return (UserProfile) byIdUser.<Object>map(UserEntity::getUserProfile).orElse(null);
    }
}
