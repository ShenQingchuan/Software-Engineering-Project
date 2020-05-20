package com.example.csgs.mapper;

import com.example.csgs.entity.PwdProEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface PwdProMapper {
    PwdProEntity findById(@Param("id") Long id);

    PwdProEntity findPwdProQueById(@Param("id") Long id);

    PwdProEntity findPwdProAnsById(@Param("id") Long id);

    PwdProEntity findPwdProById(@Param("id") Long id);

    int updatePwdPro(HashMap<String, Object> map);

    int deleteById(Long id);

    void insertPwdPro_selectKey(PwdProEntity pwdProEntity);
}
