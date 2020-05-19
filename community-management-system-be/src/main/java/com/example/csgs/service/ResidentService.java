package com.example.csgs.service;

import com.example.csgs.entity.CommunityInfo;
import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.Announcement;

public interface ResidentService {
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
