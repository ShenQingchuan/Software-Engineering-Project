package com.example.csgs.service.Impl;

import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.PwdProEntity;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.service.UserPwdProService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserPwdProServiceImpl implements UserPwdProService {
    @Autowired
    UserDao userDAO;

    private void addPwdPro(PwdProEntity pwdProEntity, List<String> list) {
        pwdProEntity.setQuestionOne(list.get(0));
        pwdProEntity.setAnswerOne(list.get(1));
        pwdProEntity.setQuestionTwo(list.get(2));
        pwdProEntity.setAnswerTwo(list.get(3));
    }

    @Override
    public boolean setPwdPro(Long id, List<String> list) {
        Optional<UserEntity> userEntity = userDAO.findById(id);
        if (userEntity.isPresent()) {
            PwdProEntity pwdProEntity;
            if (userEntity.get().getPwdProEntity() == null) {
                pwdProEntity = new PwdProEntity();
            } else {
                pwdProEntity = userEntity.get().getPwdProEntity();
            }
            addPwdPro(pwdProEntity, list);
            userEntity.get().setPwdProEntity(pwdProEntity);
            userDAO.save(userEntity.get());
            return true;
        }
        return false;
    }

    @Override
    public List<String> returnPwdProQue(Long id) {
        List<String> list = new ArrayList<>();
        Optional<UserEntity> userEntity = userDAO.findById(id);
        if (userEntity.isPresent()) {
            PwdProEntity pwdProEntity = userEntity.get().getPwdProEntity();
            if (pwdProEntity != null) {
                list.add(pwdProEntity.getQuestionOne());
                list.add(pwdProEntity.getQuestionTwo());
            }
            return list;
        }
        return null;
    }

    @Override
    public boolean comparePwdProAns(Long id, List<String> list) {
        Optional<UserEntity> userEntity = userDAO.findById(id);
        if (userEntity.isPresent()) {
            PwdProEntity pwdProEntity = userEntity.get().getPwdProEntity();

            return list.get(0).equals(pwdProEntity.getAnswerOne()) && list.get(1).equals(pwdProEntity.getAnswerTwo());
        }
        return false;
    }



}
