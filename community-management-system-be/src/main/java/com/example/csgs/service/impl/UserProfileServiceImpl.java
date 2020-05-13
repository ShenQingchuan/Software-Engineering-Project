package com.example.csgs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.OfGrid;
import com.example.csgs.entity.UserProfile;
import com.example.csgs.mapper.AnnouncementMapper;
import com.example.csgs.mapper.CommunityInfoMapper;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.UserProfileService;
import com.example.csgs.utils.CalculatePageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    final UserMapper userMapper;
    final ProfileMapper profileMapper;
    final CommunityInfoMapper communityInfoMapper;
    final AnnouncementMapper announcementMapper;

    public UserProfileServiceImpl(UserMapper userMapper, ProfileMapper profileMapper,
                                  CommunityInfoMapper communityInfoMapper, AnnouncementMapper announcementMapper) {
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
        this.communityInfoMapper = communityInfoMapper;
        this.announcementMapper = announcementMapper;
    }

    /**
     * 修改资料接口
     * 场景：用户进入资料编辑界面，对可修改的信息进行修改
     */
    @Override
    public boolean updateMaterial(JSONObject jsonObject, Long id) {
        UserProfile userProfile = profileMapper.findById(id);
        if (userProfile != null) {
            HashMap<String, Object> map = new HashMap<>();
            addDataToMap(map,jsonObject,id);

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

    private void addDataToMap(HashMap<String, Object> map, JSONObject jsonObject,Long id) {
        map.put("id", id);
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

    /**
     * 场景：居民用户登陆，居民首页呈现自己所住小区名、房屋数量、停车位数量、居民数量
     * 和网格员向本小区发送的公告信息
     * 注意：当前id是居民用户的id
     */
    @Override
    public CommunityInfo findCommunityInfo(Long id, String page) {
        int pageSize = 10;
        if (profileMapper.findGridIdIsExist(id) > 0) {
            CommunityInfoEntity communityInfoEntity = profileMapper.findCommunityInfoProfileId(id);
            String communityName = communityInfoEntity.getCommunityName();
            Long numHouses = communityInfoEntity.getNumHouses();
            Long numResidents = communityInfoEntity.getNumResidents();
            Long numParkingSpaces = communityInfoEntity.getNumParkingSpaces();
            Long userId = communityInfoEntity.getGridId().getUserId().getId();

            Page<Announcement> pageAble = PageHelper.startPage(Integer.parseInt(page), pageSize);
            PageHelper.orderBy("id ASC");//按id升序排列
            List<Announcement> announcementList = announcementMapper.findAnnouncementByCreator(userId);
            PageQuery<Announcement> pageInfo = CalculatePageUtils.getPageInfo(Integer.parseInt(page),pageSize, pageAble, announcementList);

            return new CommunityInfo(communityName,numHouses,numResidents,numParkingSpaces,pageInfo);
        }
        return null;
    }
}
