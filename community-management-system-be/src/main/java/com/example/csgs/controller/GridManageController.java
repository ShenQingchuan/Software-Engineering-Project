package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.AreaList;
import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.Journal;
import com.example.csgs.mapper.AnnouncementMapper;
import com.example.csgs.mapper.JournalMapper;
import com.example.csgs.mapper.JournalTypeMapper;
import com.example.csgs.service.GridManageService;
import com.example.csgs.utils.AuthorityUtils;
import com.example.csgs.utils.IsInteger;
import com.example.csgs.utils.RedisUtils;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/grid")
@Log4j
public class GridManageController {
    @Resource
    GridManageService gridManageService;
    @Resource
    JournalMapper journalMapper;
    @Resource
    AnnouncementMapper announcementMapper;
    @Resource
    JournalTypeMapper journalTypeMapper;
    @Resource
    RedisUtils redisUtils;

    /**
     * 发布公告接口
     * 场景：网格员发布管理区域公告（id为user表中网格员的id）
     */
    @PostMapping("/releaseAnnouncement/{id}")
    public Object releaseAnnouncement(HttpServletRequest request, @PathVariable String id, @RequestBody JSONObject jsonObject) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id, redisUtils)) {
            String titleName = jsonObject.getString("titleName");
            String content = jsonObject.getString("content");
            if (gridManageService.releaseAnnouncement(Long.parseLong(id), titleName, content)) {
                log.info("id为<" + id + ">网格员发布公告成功！");
                return ResultUtils.success("公告发布成功！");
            }
            log.info("id为<" + id + ">网格员发布公告成功！");
            return ResultUtils.error("公告发布失败！");
        }
        log.info("[Refuse]公告信息发布不匹配！");
        return ResultUtils.success("[Refuse]公告信息发布不匹配！");
    }

    /**
     * 删除公告接口
     * 场景：网格员删除某一条公告
     */
    @DeleteMapping("/deleteAnnouncement/{id}")
    public Object deleteAnnouncement(@PathVariable String id) {
        if (IsInteger.isInteger(id) && announcementMapper.deleteAnnouncement(Long.parseLong(id)) > 0) {
            log.info("Announcement表id为<" + id + ">公告被成功删除！");
            return ResultUtils.success("公告删除成功！");
        }
        log.info("Announcement表id为<" + id + ">公告删除失败！");
        return ResultUtils.error("公告删除失败！");
    }

    /**
     * 删除日志接口
     * 场景：网格员删除某一条日志
     */
    @DeleteMapping("/deleteJournal/{id}")
    public Object deleteJournal(@PathVariable String id) {
        if (IsInteger.isInteger(id) && journalMapper.deleteJournal(Long.parseLong(id)) > 0) {
            log.info("Journal表id为<" + id + ">日志被成功删除！");
            return ResultUtils.success("日志删除成功！");
        }
        log.info("Journal表id为<" + id + ">日志删除失败！");
        return ResultUtils.error("日志删除失败！");
    }

    /**
     * 网格员获取所有自己发布的公告列表(分页返回数据)
     * 场景：网格员进入公告管理界面(id为user表中网格员的id)
     */
    @GetMapping("/getAnnouncementList/{id}")
    public Object getAnnouncementList(HttpServletRequest request, @PathVariable String id, @RequestParam String page) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id, redisUtils)) {
            PageQuery<Announcement> pageQuery = gridManageService.getAnnouncementList(Long.parseLong(id), page);
            if (pageQuery != null && pageQuery.getDataList() != null) {
                log.info("user表中id为<" + id + ">的网格员获取公告列表信息成功!");
                return ResultUtils.success(pageQuery, "返回公告信息列表成功！");
            }
            if (Integer.parseInt(page) > 1) {
                log.info("已无更多公告！");
                return ResultUtils.error(" 已无更多公告！");
            }
            log.info("user表中id为<" + id + ">的网格员获取公告列表信息失败!");
            return ResultUtils.success("返回公告信息列表失败！");
        }
        log.info("[Refuse]公告信息查询不匹配！");
        return ResultUtils.success("[Refuse]公告信息查询不匹配！");
    }

    /**
     * 网格员发布日志接口
     * 场景：网格员进入日志管理界面，点击新增按钮
     */
    @PostMapping("/releaseJournal/{id}")
    public Object releaseJournal(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable String id) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id, redisUtils)) {
            String titleName = jsonObject.getString("titleName");
            String content = jsonObject.getString("content");
            String type = jsonObject.getString("type");

            if (gridManageService.releaseJournal(titleName, content, type, Long.parseLong(id))) {
                log.info("id为<" + id + ">网格员发布日志成功！");
                return ResultUtils.success("日志信息新增成功！");
            }
            log.info("id为<" + id + ">网格员发布日志失败！");
            return ResultUtils.error("日志信息新增失败！");
        }
        log.info("[Refuse]日志信息发布不匹配！");
        return ResultUtils.success("[Refuse]日志信息发布不匹配！");
    }

    /**
     * 网格员获取所有自己发布的日志列表(分页返回数据)
     * 场景：网格员进入日志管理界面(id为user表网格员的id)
     */
    @GetMapping("/getJournalList/{id}")
    public Object getJournalList(HttpServletRequest request, @PathVariable String id, @RequestParam String page) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id, redisUtils)) {
            PageQuery<Journal> pageQuery = gridManageService.getJournalList(Long.parseLong(id), page);
            if (pageQuery != null && pageQuery.getDataList() != null) {
                log.info("user表中id为<" + id + ">的网格员获取日志列表信息成功!");
                return ResultUtils.success(pageQuery, "返回日志信息列表Success！");
            }
            if (Integer.parseInt(page) > 1) {
                log.info("当前已无更多日志");
                return ResultUtils.error(" 已无更多日志！");
            }
            log.info("user表中id为<" + id + ">的网格员获取公告列表信息失败!");
            return ResultUtils.success("返回日志信息列表Failure！");
        }
        log.info("[Refuse]日志信息查询不匹配！");
        return ResultUtils.success("[Refuse]日志信息查询不匹配！");
    }

    /**
     * 网格员获取获取某条日志的详情界面（意思就是要将日志的内容展示出来）
     * 场景：网格员点击日志列表某一条日志，查看日志内容
     */
    @GetMapping("/getJournalContent/{id}")
    public Object getJournalContent(@PathVariable String id) {
        if (IsInteger.isInteger(id)) {
            String journalContent = journalMapper.findContentById(Long.parseLong(id));
            if (journalContent != null) {
                log.info("Journal表中id为<" + id + ">的日志内容获取成功!");
                return ResultUtils.success(journalContent, "返回日志内容成功！");
            }
        }
        log.info("Journal表中id为<" + id + ">的日志内容获取失败!");
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
        String password = jsonObject.getString("password");
        if (gridManageService.addResidentUser(userID, district, community, password)) {
            log.info("新增[" + district + ":" + community + "]用户userID<" + userID + ">成功！");
            return ResultUtils.success("新增居民用户成功！");
        }
        log.info("新增[" + district + ":" + community + "]用户userID<" + userID + ">失败！");
        return ResultUtils.success("新增居民用户失败！");
    }

    /**
     * 获取该网格员所管理的区域（id为user表中网格员的id）
     */
    @GetMapping("/getManageAreaList/{id}")
    public Object getManageAreaList(HttpServletRequest request, @PathVariable String id) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id, redisUtils)) {
            AreaList manageAreaList = gridManageService.getManageAreaList(Long.parseLong(id));
            if (manageAreaList != null) {
                log.info("网格员id<" + id + ">获取网格员管理区域信息成功！");
                return ResultUtils.success(manageAreaList, "获取网格员管理区域信息成功！");
            }
            log.info("网格员id<" + id + ">获取网格员管理区域信息失败！");
            return ResultUtils.error("获取网格员管理区域信息失败！");
        }
        log.info("[Refuse]网格员管理区域信息查询不匹配！");
        return ResultUtils.success("[Refuse]网格员管理区域信息查询不匹配！");
    }

    /**
     * 场景：当网格员在发送日志的时候，先需要得到所有的日志类型，展示在界面上进行选择
     */
    @GetMapping("/getJournalTypeName")
    public Object getJournalType() {
        List<String> allTypeName = journalTypeMapper.findAllTypeName();
        if (!allTypeName.isEmpty()) {
            log.info("网格员获取日志类型名成功！");
            return ResultUtils.success(allTypeName, "获取日志类型名成功！");
        }
        log.info("网格员获取日志类型名失败！");
        return ResultUtils.error("获取日志类型名失败");
    }
}
