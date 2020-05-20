package com.example.csgs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.OfGrid;
import com.example.csgs.entity.UserProfile;
import com.example.csgs.mapper.CommunityInfoMapper;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.service.UserProfileService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
@Transactional
@Log4j
public class UserProfileServiceImpl implements UserProfileService {
    @Resource
    ProfileMapper profileMapper;
    @Resource
    CommunityInfoMapper communityInfoMapper;

    /**
     * 修改资料接口
     * 场景：用户进入资料编辑界面，对可修改的信息进行修改
     *
     * @param id 将要修改资料用户在user表中的id，同样也可以是userProfile表中的id（一对一的关系）
     */
    @Override
    public boolean updateMaterial(JSONObject jsonObject, Long id) {
        UserProfile userProfile = profileMapper.findById(id);
        if (userProfile != null) {
            HashMap<String, Object> map = new HashMap<>();
            addDataToMap(map, jsonObject, id);

            if (jsonObject.containsKey("ofGrid")) {
                JSONObject ofGridObject = jsonObject.getJSONObject("ofGrid");
                OfGrid ofGrid = JSONObject.toJavaObject(ofGridObject, OfGrid.class);
                String districtName = ofGrid.getDistrictName();
                String communityName = ofGrid.getCommunityName();
                System.out.println(districtName);
                CommunityInfoEntity communityInfoEntity = communityInfoMapper.
                        findByOfGrid(districtName, communityName);

                if (communityInfoEntity != null) {
                    map.put("communityId", communityInfoEntity.getId());
                }
            }
            return profileMapper.updateProfile(map) > 0;
        }
        return false;
    }

    /**
     * 从Json串中提取资料信息添加到Map中
     *
     */
    private void addDataToMap(HashMap<String, Object> map, JSONObject jsonObject, Long id) {
        map.put("id", id);
        map.put("avatarUrl",jsonObject.getString("avatarUrl"));
        map.put("occupation", jsonObject.getString("occupation"));
        map.put("bloodType", jsonObject.getString("bloodType"));
        map.put("degreeOfEducation", jsonObject.getString("degreeOfEducation"));
        map.put("email", jsonObject.getString("email"));
        map.put("nation", jsonObject.getString("nation"));
        map.put("politicCountenance", jsonObject.getString("politicCountenance"));
        map.put("sex", jsonObject.getString("sex"));
        map.put("stature", jsonObject.getString("stature"));
        map.put("telPhone", jsonObject.getString("telPhone"));
        map.put("userName", jsonObject.getString("userName"));
    }
}
