package com.example.csgs.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserPwdProService {

    boolean setPwdPro(Long id,List<String> list);

    List<String> returnPwdProQue(Long id);

    boolean comparePwdProAns(Long id,List<String> list);
}
