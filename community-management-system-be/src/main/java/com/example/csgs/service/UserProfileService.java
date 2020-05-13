package com.example.csgs.service;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;

public interface UserProfileService {
    boolean updateMaterial(JSONObject jsonObject, Long id);

    CommunityInfo findCommunityInfo(Long id, String page);
}
