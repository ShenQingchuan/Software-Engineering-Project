package com.example.csgs.service;

import com.example.csgs.entity.AreaList;
import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.Journal;

public interface GridManageService {

    /**
     * 发布公告接口
     * 场景：网格员发布管理区域公告
     * @param id user表中网格员id
     * @param content 公告内容
     * @param titleName 公告标题
     */
    boolean releaseAnnouncement(Long id, String titleName, String content);

    /**
     * 网格员发布日志接口
     * 场景：网格员进入日志管理界面，点击新增按钮
     * @param id user表中网格员id
     * @param titleName 日志标题
     * @param content 日志内容
     * @param type 日志类型
     */
    boolean releaseJournal(String titleName, String content, String type, Long id);

    /**
     * 网格员获取所有自己发布的公告列表(分页返回数据)
     * 场景：网格员进入公告管理界面
     * @param id user表中网格员id
     * @param page 当前请求页数
     */
    PageQuery<Announcement> getAnnouncementList(Long id, String page);

    /**
     * 网格员获取所有自己发布的日志列表(分页返回数据)
     * 场景：网格员进入日志管理界面(id为网格员的id)
     * @param id user表中网格员id
     * @param page 当前请求页数
     */
    PageQuery<Journal> getJournalList(Long id, String page);

    /**
     * 网格员增加一个居民用户
     * 密码默认设置为身份证后六位
     * 同时指明该居民位于哪个地区（必须是在该网格员管理的区域之内）
     * @param userID 被添加用户身份证号
     * @param password 登陆密码（已经过前端加密）
     * @param districtName 区名
     * @param communityName 小区名
     */
    boolean addResidentUser(String userID, String districtName, String communityName, String password);

    /**
     * 获取该网格员所管理的区域（id为user表中网格员的id）
     * @param id user表中网格员id
     */
    AreaList getManageAreaList(Long id);
}
