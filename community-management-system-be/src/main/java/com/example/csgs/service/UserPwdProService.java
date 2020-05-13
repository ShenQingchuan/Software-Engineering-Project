package com.example.csgs.service;

import java.util.List;

public interface UserPwdProService {

    /**
     * 此密保设置，同时也是密保修改接口，而且只要这个人存在，那么他所对应的PwdProEntity就存在，
     * 所以不存在插入一个密保的说法
     * @param id user表中用户id
     * @param list 密保信息集合
     */
    boolean setPwdPro(Long id, List<String> list);

    /**
     * 返回用户密保问题
     * @param id user表中用户id
     */
    List<String> returnPwdProQue(Long id);

    /**
     * 判断用户填写的密保问题答案是否与数据库一致
     * @param id user表中用户id
     * @param list 用户密保问题答案集合
     */
    boolean comparePwdProAns(Long id, List<String> list);

}
