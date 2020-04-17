package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.service.UserPwdProService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/pwdPro")
@Slf4j
public class PwdProController {

    final UserPwdProService userPwdProService;

    public PwdProController(UserPwdProService userPwdProService) {
        this.userPwdProService = userPwdProService;
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

        if (userPwdProService.setPwdPro(uid,list)) {
            return ResultUtils.success("密保修改成功");
        }
        return ResultUtils.error("密保修改失败！");
    }

    /**
     * 返回密保问题接口
     */
    @GetMapping("/returnPwdProQue/{id}")
    public Object returnPwdPro(@PathVariable String id) {

        Long uid = Long.parseLong(id);

        List<String> list = userPwdProService.returnPwdProQue(uid);
        if (list != null) {
            return ResultUtils.success(list,"获取密保问题成功");
        }
        return ResultUtils.error("获取密保问题失败！");
    }

    /**
     * 比较密保答案接口
     */
    @PostMapping("/comparePwdProAns/{id}")
    public Object comparePwdPro(@RequestBody JSONObject jsonObject, @PathVariable String id) {

        Long uid = Long.parseLong(id);

        String answerOne = jsonObject.getString("answerOne");
        String answerTwo = jsonObject.getString("answerTwo");

        List<String> list = new ArrayList<>();
        list.add(answerOne);
        list.add(answerTwo);

        if (userPwdProService.comparePwdProAns(uid,list)) {
            return ResultUtils.success("回答密保成功");
        }
        return ResultUtils.error("回答密保失败！");
    }

}
