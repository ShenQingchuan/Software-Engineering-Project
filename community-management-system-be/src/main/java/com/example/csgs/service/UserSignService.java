package com.example.csgs.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserSignService {
    Map<Integer,String > sign(String userAccount, String password, HttpServletResponse response);

    boolean signOut(HttpServletRequest request);
}
