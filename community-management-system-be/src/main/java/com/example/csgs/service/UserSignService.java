package com.example.csgs.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserSignService {
    /**
     * 用户登陆
     * @param userID 用户身份证号
     * @param password 登陆密码（已经经过前端加密）
     * @param response 携带返回后端生成的Cookie
     */
    Map<Integer,String > sign(String userID, String password, HttpServletResponse response);

    /**
     * 退出登陆
     * @param request 携带Cookie至后端，清除redis缓存的token
     */
    boolean signOut(HttpServletRequest request);
}
