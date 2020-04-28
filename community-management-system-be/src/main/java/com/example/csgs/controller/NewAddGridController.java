package com.example.csgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.LoginState;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@Slf4j
public class NewAddGridController {

    /**
     * 新增网格员接口
     * 同时系统管理员为网格员分配管理区域
     */
    @PostMapping("/addGrid/{id}")
    public Object addGrid(@RequestBody JSONObject jsonObject, @PathVariable String id) {

        return null;
    }

    /**
     *返回当前系统管理员添加的网格员数据列表
     */
    @GetMapping("/getGrids/{id}")
    public Object returnGrids(@PathVariable String id){
        return null;
    }


}
