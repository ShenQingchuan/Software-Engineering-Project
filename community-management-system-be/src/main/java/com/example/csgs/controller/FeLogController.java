package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.FeLog;
import com.example.csgs.mapper.FeLogMapper;
import com.example.csgs.utils.AuthorityUtils;
import com.example.csgs.utils.IsInteger;
import com.example.csgs.utils.RedisUtils;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/log")
@Log4j
public class FeLogController {
    @Resource
    FeLogMapper feLogMapper;
    @Resource
    RedisUtils redisUtils;

    @PostMapping("/storage/{id}")
    public Object logStorage(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable String id) {
        FeLog feLog = JSONObject.toJavaObject(jsonObject, FeLog.class);
        feLog.setUserId(Long.parseLong(id));
        if (IsInteger.isInteger(id) && AuthorityUtils.authority(request, id,redisUtils)){
            if (feLogMapper.insertLog(feLog) > 0) {
                log.info("用户Id<" + feLog.getUserId() + ">前端用户操作日志存储成功！");
                return ResultUtils.success("前端用户操作日志存储成功！");
            }
        }
        log.info("用户Id<"+feLog.getUserId()+">前端用户操作日志存储失败！");
        return ResultUtils.error("前端用户操作日志存储失败！");
    }

}
