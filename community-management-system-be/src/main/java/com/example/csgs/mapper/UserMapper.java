package com.example.csgs.mapper;


import com.example.csgs.entity.User;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.entity.UserProfile;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserMapper {
    UserEntity findById(@Param("id") Long id);

    UserEntity findOneByUserID(@Param("userID") String userID);

    UserEntity findByUserProfile(UserProfile userProfile);

    int modifyPassword(@Param("newPassword") String newPassword, @Param("id") Long id);

    int judgeGrid(@Param("id") Long id);

    User findUserByUserID(@Param("UserID") String UserID);

    List<User> findUserByGridInfo(HashMap<String, String> map);

    void addResidentUser(HashMap<String, Object> map);

    void setGrid(@Param("UserID") String UserID);

    void cancelGrid(@Param("UserID") String UserID);
}
