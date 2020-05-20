package com.example.csgs.service;

import javax.servlet.http.HttpServletRequest;

public interface UserSignService {

    /**
     * 退出登陆
     * @param request 携带Cookie至后端，清除redis缓存的token
     */
    boolean signOut(HttpServletRequest request);
}
