package com.example.csgs.service.impl;

import com.example.csgs.entity.PwdProEntity;
import com.example.csgs.mapper.PwdProMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.UserPwdProService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j
@Service
@Transactional
public class UserPwdProServiceImpl implements UserPwdProService {
    final UserMapper userMapper;
    final PwdProMapper pwdProMapper;

    public UserPwdProServiceImpl(UserMapper userMapper, PwdProMapper pwdProMapper) {
        this.userMapper = userMapper;
        this.pwdProMapper = pwdProMapper;
    }

    /**
     * 从List集合中提取密保放到HashMap中
     * @param id 修改密保用户id
     * @param map 装载容器
     * @param list 传递密保数据集合
     */
    private void addPwdPro(Long id,HashMap<String, Object> map, List<String> list) {
        map.put("id",id);
        map.put("questionOne",list.get(0));
        map.put("answerOne",list.get(1));
        map.put("questionTwo",list.get(2));
        map.put("answerTwo",list.get(3));
    }

    /**
     * 此密保设置，同时也是密保修改接口，而且只要这个人存在，那么他所对应的PwdProEntity就存在，
     * 所以不存在插入一个密保的说法
     * @param id user表中用户id
     * @param list 密保信息集合
     */
    @Override
    public boolean setPwdPro(Long id, List<String> list) {
        PwdProEntity pwdProEntity = pwdProMapper.findPwdProById(id);
        HashMap<String, Object> map = new HashMap<>();
        addPwdPro(id,map,list);
        if (pwdProEntity != null) {
            addPwdPro(id,map,list);
            return pwdProMapper.updatePwdPro(map) > 0;
        }
        return false;
    }

    /**
     * 返回用户密保问题
     * @param id user表中用户id
     */
    @Override
    public List<String> returnPwdProQue(Long id) {
        List<String> list = new ArrayList<>();
        PwdProEntity pwdProEntity = pwdProMapper.findPwdProQueById(id);
        if (pwdProEntity != null) {
            list.add(pwdProEntity.getQuestionOne());
            list.add(pwdProEntity.getQuestionTwo());
            return list;
        }
        return null;
    }

    /**
     * 判断用户填写的密保问题答案是否与数据库一致
     * @param id user表中用户id
     * @param list 用户密保问题答案集合
     */
    @Override
    public boolean comparePwdProAns(Long id, List<String> list) {
        PwdProEntity pwdProAnsById = pwdProMapper.findPwdProAnsById(id);
        if (pwdProAnsById != null) {
            return list.get(0).equals(pwdProAnsById.getAnswerOne()) &&
                    list.get(1).equals(pwdProAnsById.getAnswerTwo());
        }
        return false;
    }

}
