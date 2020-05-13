package com.example.csgs.service.impl;

import com.example.csgs.entity.PwdProEntity;
import com.example.csgs.mapper.PwdProMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.UserPwdProService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserPwdProServiceImpl implements UserPwdProService {
    final UserMapper userMapper;
    final PwdProMapper pwdProMapper;

    public UserPwdProServiceImpl(UserMapper userMapper, PwdProMapper pwdProMapper) {
        this.userMapper = userMapper;
        this.pwdProMapper = pwdProMapper;
    }

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
     */
    @Override
    public boolean setPwdPro(Long id, List<String> list) {
        PwdProEntity pwdProEntity = pwdProMapper.findPwdProById(id);
        HashMap<String, Object> map = new HashMap<>();
        if (pwdProEntity != null) {
            addPwdPro(id,map,list);
            return pwdProMapper.updatePwdPro(map) > 0;
        }
        return false;
    }

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
