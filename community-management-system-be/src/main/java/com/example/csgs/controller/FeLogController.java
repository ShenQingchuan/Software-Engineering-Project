package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.FeLog;
import com.example.csgs.entity.LogTimeResult;
import com.example.csgs.entity.LogTypeResult;
import com.example.csgs.mapper.FeLogMapper;
import com.example.csgs.utils.AuthorityUtil;
import com.example.csgs.utils.IsIntegerUtil;
import com.example.csgs.utils.RedisUtil;
import com.example.csgs.utils.ResultUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/log")
@Log4j
public class FeLogController {
    @Resource
    FeLogMapper feLogMapper;
    @Resource
    RedisUtil redisUtil;

    @PostMapping("/storage/{id}")
    public Object logStorage(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable String id) {
        FeLog feLog = JSONObject.toJavaObject(jsonObject, FeLog.class);
        feLog.setUserId(Long.parseLong(id));
        if (IsIntegerUtil.isInteger(id) && AuthorityUtil.authority(request, id, redisUtil)) {
            if (feLogMapper.insertLog(feLog) > 0) {
                log.info("用户Id<" + feLog.getUserId() + ">前端用户操作日志存储成功！");
                return ResultUtil.success("前端用户操作日志存储成功！");
            }
        }
        log.info("用户Id<" + feLog.getUserId() + ">前端用户操作日志存储失败！");
        return ResultUtil.error("前端用户操作日志存储失败！");
    }

    @GetMapping("/AggregateQuery/time")
    public Object queryTime() {
        List<LogTimeResult> logTimeResults = feLogMapper.queryTimeLog();
        if (logTimeResults != null) {
            return ResultUtil.success(logTimeResults,"日期聚合查询成功！");
        }
        return ResultUtil.error("日期聚合查询失败");
    }

    @GetMapping("/AggregateQuery/type")
    public Object queryType() {
        List<LogTypeResult> logTypeResults = feLogMapper.queryTypeLog();
        if (logTypeResults != null) {
            return ResultUtil.success(logTypeResults,"类型聚合查询成功！");
        }
        return ResultUtil.error("类型聚合查询失败");
    }

}
