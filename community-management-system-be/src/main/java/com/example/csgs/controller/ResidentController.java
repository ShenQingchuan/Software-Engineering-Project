package com.example.csgs.controller;

import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.Announcement;
import com.example.csgs.service.ResidentService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resident")
@Log4j
public class ResidentController {
    final ResidentService residentService;

    public ResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    /**
     * 场景：居民用户登陆，居民首页呈现自己所住小区名、房屋数量、停车位数量、居民数量
     * 和网格员向本小区发送的公告信息
     * 注意：当前id是居民用户的id
     */
    @GetMapping("/ResidentRPH/{id}")
    public Object queryCommunityInfo(@PathVariable String id) {
        CommunityInfo communityInfo = residentService.findResidentRPH(Long.parseLong(id));
        if (communityInfo != null) {
            return ResultUtils.success(communityInfo, "用户所在社区RPH信息获取成功！");
        }
        return ResultUtils.error("用户所在社区RPH信息获取失败！");
    }

    /**
     * 居民用户获取自己所处网格中网格员所发布的公告信息
     */
    @GetMapping("/getAnnouncementOfGrid/{id}")
    public Object getAnnouncementOfGrid(@PathVariable String id, @RequestParam String page) {
        PageQuery<Announcement> announcementList = residentService.getAnnouncementOfGrid(Long.parseLong(id), page);
        if (announcementList != null) {
            return ResultUtils.success(announcementList, "获取用户所在网格公告信息成功！");
        }
        if (Integer.parseInt(page) > 1) {
            return ResultUtils.error(" 已无更多公告！");
        }
        return ResultUtils.error("该用户所在小区未发布任何公告！");
    }
}
