package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.service.UserPwdProService;
import com.example.csgs.utils.ResultUtils;
import com.example.csgs.utils.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pwdPro")
@Slf4j
public class UserPwdProController {

    final UserPwdProService userPwdProService;
    final UserDao userDao;

    public UserPwdProController(UserPwdProService userPwdProService, UserDao userDao) {
        this.userPwdProService = userPwdProService;
        this.userDao = userDao;
    }

    /**
     * 设置密保接口
     */
    @PutMapping("/setUpPwdPro/{id}")
    public Object updatePwdPro(@RequestBody JSONObject jsonObject, @PathVariable String id) {

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
            return ResultUtils.success("密保设置成功");
        }
        return ResultUtils.error("密保设置失败！");
    }

    /**
     * 修改密码接口
     * 场景：在回答正确密保问题后，进入修改密码界面，提交修改后的密码
     */
    @PutMapping("/modifyPwd/{id}")
    public Object updatePwd(@RequestBody String newPassword, @PathVariable String id) {
        String decryptPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        String sha256String = SHA256Util.getSHA256String(decryptPassword);

        Optional<UserEntity> targetUser = userDao.findById(Long.parseLong(id));
        if (targetUser.isPresent()) {
            if (!targetUser.get().getUserPassword().equals(sha256String)) {
                return ResultUtils.error("密码修改失败,该密码与原始密码相同！！");
            }
        }

        userPwdProService.modifyPwd(sha256String, Long.parseLong(id));
        return ResultUtils.success("密码修改成功！");
    }

    /**
     * 返回密保问题接口(两个密保问题)
     */
    @GetMapping("/returnPwdProQue/{id}")
    public Object returnPwdPro(@PathVariable String id) {

        Long uid = Long.parseLong(id);

        List<String> list = userPwdProService.returnPwdProQue(uid);
        if (list != null) {
            return ResultUtils.success(list, "获取密保问题成功");
        }
        return ResultUtils.error("获取密保问题失败！");
    }

    /**
     * 比较密保答案接口（验证上传的密保答案）
     */
    @PostMapping("/comparePwdProAns/{id}")
    public Object comparePwdPro(@RequestBody JSONObject jsonObject, @PathVariable String id) {

        Long uid = Long.parseLong(id);

        String answerOne = jsonObject.getString("answerOne");
        String answerTwo = jsonObject.getString("answerTwo");

        List<String> list = new ArrayList<>();
        list.add(answerOne);
        list.add(answerTwo);

        if (userPwdProService.comparePwdProAns(uid, list)) {
            return ResultUtils.success("回答密保成功");
        }
        return ResultUtils.error("回答密保失败！");
    }

}
