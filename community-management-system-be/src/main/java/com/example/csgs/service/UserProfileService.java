package com.example.csgs.service;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.Announcement;

import java.util.List;

public interface UserProfileService {
    /**
     * 修改资料接口
     * 场景：用户进入资料编辑界面，对可修改的信息进行修改
     * @param id 将要修改资料用户在user表中的id，同样也可以是userProfile表中的id（一对一的关系）
     */
    boolean updateMaterial(JSONObject jsonObject, Long id);

}
