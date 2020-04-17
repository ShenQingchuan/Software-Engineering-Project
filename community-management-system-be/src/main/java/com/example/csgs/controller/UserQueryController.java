package com.example.csgs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/query")
@Slf4j
public class UserQueryController {
    private final UserDao userDAO;

    public UserQueryController(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * 按 id 信息查询用户
     * 场景：用户进入自己的资料查看界面
     */
    @GetMapping("/{id}")
    public Object singleUserQuery(@PathVariable String id) {
        Long uid = Long.parseLong(id);
        Optional<UserEntity> queryUser = userDAO.findById(uid);
        if (queryUser.isPresent()) {
            UserEntity user = queryUser.get();
            user.setUserPassword(null);
            return ResultUtils.success(JSON.toJSON(user),"用户个人资料获取成功");
        }
        return ResultUtils.error("没有找到 uid: "+ uid + " 的用户");
    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     */
    @GetMapping("/singleUser")
    public Object operatorSingleUserQuery(@RequestBody JSONObject jsonObject) {
        String userAccount = jsonObject.getString("userAccount");
        String userName = jsonObject.getString("userName");
        String userCommunity = jsonObject.getString("userCommunity");
        String userArea = jsonObject.getString("userArea");
        
        Optional<UserEntity> queryUser = userDAO.findOneByUserAccount(userAccount);

        if (queryUser.isPresent()) {
            UserEntity temp = queryUser.get();
            temp.setUserPassword(null);
            return ResultUtils.success(JSON.toJSON(temp),"查询用户资料成功");
        }
        return ResultUtils.error("没有找到 userAccount: "+ userAccount + " 的用户");
    }

    /**
     * （分页查询）获取所有用户
     * 场景：主界面用户信息列表查询
     */
    @GetMapping("/allUser")
    public Object findAll() {
        List<UserEntity> all = (List<UserEntity>) userDAO.findAll();
        for (UserEntity temp : all) {
            temp.setUserPassword(null);//对密码进行过滤
        }
        return ResultUtils.success(
            JSONArray.parseArray(JSON.toJSONString(all)),
            "获取用户列表成功"
        );
    }

}
