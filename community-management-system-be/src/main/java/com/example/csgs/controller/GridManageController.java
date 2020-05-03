package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.Announcement;
import com.example.csgs.bean.AreaList;
import com.example.csgs.bean.Journal;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.service.GridManageService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grid")
@Slf4j
public class GridManageController {
    @Autowired
    GridManageService gridManageService;

    /**
     * 发布公告接口
     * 场景：网格员发布管理区域公告（id为user表中网格员的id）
     */
    @PostMapping("/releaseAnnouncement/{id}")
    public Object releaseAnnouncement(@PathVariable String id, @RequestBody JSONObject jsonObject) {
        String titleName = jsonObject.getString("titleName");
        String content = jsonObject.getString("content");
        if (gridManageService.releaseAnnouncement(Long.parseLong(id), titleName, content)) {
            return ResultUtils.success("公告发布成功！");
        }
        return ResultUtils.error("公告发布失败！");
    }

    /**
     * 删除公告接口
     * 场景：网格员删除某一条公告
     */
    @DeleteMapping("/deleteAnnouncement/{id}")
    public Object deleteAnnouncement(@PathVariable String id) {
        if (gridManageService.deleteAnnouncement(Long.parseLong(id))) {
            return ResultUtils.success("公告删除成功！");
        }
        return ResultUtils.error("公告删除失败！");
    }

    /**
     * 删除日志接口
     * 场景：网格员删除某一条日志
     */
    @DeleteMapping("/deleteJournal/{id}")
    public Object deleteJournal(@PathVariable String id) {
        if (gridManageService.deleteJournal(Long.parseLong(id))) {
            return ResultUtils.success("日志删除成功！");
        }
        return ResultUtils.error("日志删除失败！");
    }

    /**
     * 网格员获取所有自己发布的公告列表(分页返回数据)
     * 场景：网格员进入公告管理界面(id为网格员的id)
     */
    @GetMapping("/getAnnouncementList/{id}")
    public Object getAnnouncementList(@PathVariable String id, @RequestParam String page) {
        PageQuery<Announcement> pageQuery = gridManageService.getAnnouncementList(Long.parseLong(id), page);
        if (pageQuery != null) {
            return ResultUtils.success(pageQuery, "返回公告信息列表成功！");
        }
        return ResultUtils.success("返回公告信息列表失败！");
    }

    /**
     * 网格员发布日志接口
     * 场景：网格员进入日志管理界面，点击新增按钮
     */
    @PostMapping("/releaseJournal/{id}")
    public Object releaseJournal(@RequestBody JSONObject jsonObject, @PathVariable String id) {
        String titleName = jsonObject.getString("titleName");
        String content = jsonObject.getString("content");
        String type = jsonObject.getString("type");

        if (gridManageService.releaseJournal(titleName, content, type, Long.parseLong(id))) {
            return ResultUtils.success("日志信息新增成功！");
        }
        return ResultUtils.success("日志信息新增失败！");
    }

    /**
     * 网格员获取所有自己发布的日志列表(分页返回数据)
     * 场景：网格员进入日志管理界面(id为网格员的id)
     */
    @GetMapping("/getJournalList/{id}")
    public Object getJournalList(@PathVariable String id, @RequestParam String page) {
        PageQuery<Journal> pageQuery = gridManageService.getJournalList(Long.parseLong(id), page);
        if (pageQuery != null) {
            return ResultUtils.success(pageQuery, "返回日志信息列表成功！");
        }
        return ResultUtils.success("返回日志信息列表失败！");
    }

    /**
     * 网格员获取获取某条日志的详情界面（意思就是要将日志的内容展示出来）
     * 场景：网格员点击日志列表某一条日志，查看日志内容
     */
    @GetMapping("/getJournalContent/{id}")
    public Object getJournalContent(@PathVariable String id) {
        String journalContent = gridManageService.getJournalContent(Long.parseLong(id));
        if ((!journalContent.equals(""))) {
            return ResultUtils.success(journalContent, "返回日志内容成功！");
        }
        return ResultUtils.success("返回日志内容失败！");
    }

    /**
     * 网格员增加一个居民用户
     * 密码默认设置为身份证后六位
     * 同时指明该居民位于哪个地区（必须是在该网格员管理的区域之内）
     * id为网格员id
     */
    @PostMapping("/addResidentUser")
    public Object addResident(@RequestBody JSONObject jsonObject) {
        String userID = jsonObject.getString("userID");
        String district = jsonObject.getString("district");
        String community = jsonObject.getString("community");
        if (gridManageService.addResidentUser(userID, district,community)) {
            return ResultUtils.success("新增居民用户成功！");
        }
        return ResultUtils.success("新增居民用户失败！");
    }

    /**
     * 获取该网格员所管理的区域（id为user表中网格员的id）
     */
    @GetMapping("/getManageAreaList/{id}")
    public Object getManageAreaList(@PathVariable String id){
        AreaList manageAreaList = gridManageService.getManageAreaList(Long.parseLong(id));
        if (manageAreaList !=null) {
            return ResultUtils.success(manageAreaList,"获取网格员管理区域信息成功！");
        }
        return ResultUtils.error("获取网格员管理区域信息失败！");
    }
}
