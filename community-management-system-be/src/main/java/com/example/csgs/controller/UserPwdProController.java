package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.UserPwdProService;
import com.example.csgs.utils.IsIntegerUtil;
import com.example.csgs.utils.ResultUtil;
import com.example.csgs.utils.SHA256Util;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pwdPro")
@Log4j
public class UserPwdProController {
    @Resource
    UserPwdProService userPwdProService;
    @Resource
    UserMapper userMapper;

    /**
     * 设置密保接口
     */
    @PutMapping("/setUpPwdPro/{id}")
    public Object updatePwdPro(@RequestBody JSONObject jsonObject, @PathVariable String id) {
        if (!IsIntegerUtil.isInteger(id)) {
            return ResultUtil.error("拒绝访问！请修改请求信息......");
        }
        Long uid = Long.parseLong(id);
        String questionOne = jsonObject.getString("questionOne");
        String questionTwo = jsonObject.getString("questionTwo");
        String answerOne = jsonObject.getString("answerOne");
        String answerTwo = jsonObject.getString("answerTwo");

        List<String> list = new ArrayList<>();
        list.add(questionOne);
        list.add(answerOne);
        list.add(questionTwo);
        list.add(answerTwo);

        if (userPwdProService.setPwdPro(uid, list)) {
            log.info("用户<"+ id +">设置密保成功！");
            return ResultUtil.success("密保设置成功");
        }
        log.info("用户<"+ id +">设置密保失败！");
        return ResultUtil.error("密保设置失败！");
    }

    /**
     * 修改密码接口
     * 场景：在回答正确密保问题后，进入修改密码界面，提交修改后的密码
     */
    @PutMapping("/modifyPwd/{id}")
    public Object updatePwd(@RequestBody JSONObject jsonObject, @PathVariable String id) {
        if (!IsIntegerUtil.isInteger(id)) {
            return ResultUtil.error("拒绝访问！请修改请求信息......");
        }

        String sha256String = SHA256Util.getSHA256String(jsonObject.getString("newPassword"));
        UserEntity targetUser = userMapper.findById(Long.parseLong(id));
        if (targetUser != null) {
            if (targetUser.getUserPassword().equals(sha256String)) {
                log.info("用户<"+ id +">密码修改失败,该密码与原始密码相同！");
                return ResultUtil.error("密码修改失败,该密码与原始密码相同！");
            }else if (userMapper.modifyPassword(sha256String, Long.parseLong(id)) > 0) {
                log.info("用户<"+ id +">密码修改成功！");
                return ResultUtil.success("密码修改成功！");
            }
        }
        log.info("用户<"+ id +">密码修改失败！");
        return ResultUtil.error("密码修改失败！");
    }

    /**
     * 返回密保问题接口(两个密保问题)
     */
    @GetMapping("/returnPwdProQue/{id}")
    public Object returnPwdPro(@PathVariable String id) {
        if (!IsIntegerUtil.isInteger(id)) {
            return ResultUtil.error("拒绝访问！请修改请求信息......");
        }

        List<String> list = userPwdProService.returnPwdProQue(Long.parseLong(id));
        if (list != null) {
            log.info("用户<"+ id +">获取密保问题成功！");
            return ResultUtil.success(list, "获取密保问题成功");
        }
        log.info("用户<"+ id +">获取密保问题失败！");
        return ResultUtil.error("获取密保问题失败！");
    }

    /**
     * 比较密保答案接口（验证上传的密保答案）
     */
    @PostMapping("/comparePwdProAns/{id}")
    public Object comparePwdPro(@RequestBody JSONObject jsonObject, @PathVariable String id) {
        if (!IsIntegerUtil.isInteger(id)) {
            return ResultUtil.error("拒绝访问！请修改请求信息......");
        }

        String answerOne = jsonObject.getString("answerOne");
        String answerTwo = jsonObject.getString("answerTwo");

        List<String> list = new ArrayList<>();
        list.add(answerOne);
        list.add(answerTwo);

        if (userPwdProService.comparePwdProAns(Long.parseLong(id), list)) {
            log.info("用户<"+ id +">回答密保问题成功！");
            return ResultUtil.success("回答密保成功");
        }
        log.info("用户<"+ id +">回答密保问题失败！");
        return ResultUtil.error("回答密保失败！");
    }

}
