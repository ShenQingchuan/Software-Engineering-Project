package com.example.csgs.service.Impl;

import com.example.csgs.bean.LoginState;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.service.UserQueryService;
import com.example.csgs.service.UserSignService;
import com.example.csgs.utils.JwtUtils;
import com.example.csgs.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserQueryServiceImp implements UserQueryService {

    final UserDao userDAO;

    final RedisUtils redisUtils;

    public UserQueryServiceImp(UserDao userDao, RedisUtils redisUtils) {
        this.userDAO = userDao;
        this.redisUtils = redisUtils;
    }



}

