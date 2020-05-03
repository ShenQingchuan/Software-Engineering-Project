package com.example.csgs.service;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserProfileService{
    boolean updateMaterial(JSONObject jsonObject,Long id);

    UserProfile getMaterial(Long id);

    CommunityInfo findCommunityInfo(Long id, String page);
}
