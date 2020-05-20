package com.example.csgs.controller;

import com.example.csgs.entity.CommunityInfo;
import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.Announcement;
import com.example.csgs.service.ResidentService;
import com.example.csgs.utils.AuthorityUtils;
import com.example.csgs.utils.IsInteger;
import com.example.csgs.utils.RedisUtils;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/resident")
@Log4j
public class ResidentController {
    @Resource
    ResidentService residentService;
    @Resource
    RedisUtils redisUtils;

    /**
     * 场景：居民用户登陆，居民首页呈现自己所住小区名、房屋数量、停车位数量、居民数量
     * 和网格员向本小区发送的公告信息
     * 注意：当前id是居民用户的id
     */
    @GetMapping("/ResidentRPH/{id}")
    public Object queryCommunityInfo(HttpServletRequest request, @PathVariable String id) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id,redisUtils)) {
            CommunityInfo communityInfo = residentService.findResidentRPH(Long.parseLong(id));
            if (communityInfo != null) {
                log.info("用户<" + id + ">获取所在小区RPH信息成功！");
                return ResultUtils.success(communityInfo, "用户所在社区RPH信息获取成功！");
            }
            log.info("用户<" + id + ">获取所在小区RPH信息失败！");
            return ResultUtils.error("用户所在社区RPH信息获取失败！");
        }
        log.info("[Refuse]用户所在社区RPH信息查询不匹配！");
        return ResultUtils.success("[Refuse]用户所在社区RPH信息查询不匹配！");
    }

    /**
     * 居民用户获取自己所处网格中网格员所发布的公告信息
     */
    @GetMapping("/getAnnouncementOfGrid/{id}")
    public Object getAnnouncementOfGrid(HttpServletRequest request, @PathVariable String id, @RequestParam String page) {
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id,redisUtils)) {
            PageQuery<Announcement> announcementList = residentService.getAnnouncementOfGrid(Long.parseLong(id), page);
            if (announcementList != null) {
                log.info("用户<" + id + ">获取所在网格公告信息成功！");
                return ResultUtils.success(announcementList, "获取用户所在网格公告信息成功！");
            }
            if (Integer.parseInt(page) > 1) {
                log.info("居民用户<"+ id +">所在小区已无更多公告！");
                return ResultUtils.error(" 已无更多公告！");
            }
            log.info("用户<" + id + ">该用户所在小区未发布任何公告！");
            return ResultUtils.error("该用户所在小区未发布任何公告！");
        }
        log.info("[Refuse]用户所在社区公告信息查询不匹配！");
        return ResultUtils.success("[Refuse]用户所在社区公告信息查询不匹配！");
    }
}
