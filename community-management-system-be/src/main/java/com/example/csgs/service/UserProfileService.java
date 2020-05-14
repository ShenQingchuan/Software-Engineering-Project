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

    /**
     * 场景：居民用户登陆，居民首页呈现自己所住小区名、房屋数量、停车位数量、居民数量
     * @param id 在user表中某一居民的id
     */
    CommunityInfo findResidentRPH(Long id);

    /**
     * 场景：在居民首页中获取自身所在网格区域的公告信息
     * @param id 在user表中某一居民的id
     * @param page 当前请求页数
     */
    PageQuery<Announcement> getAnnouncementOfGrid(Long id, String page);
}
