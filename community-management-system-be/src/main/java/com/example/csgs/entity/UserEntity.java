package com.example.csgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEntity implements Serializable {
    private Long id;
    private String userID;
    private String userPassword;
    private int userType;
    private PwdProEntity pwdProId;
    private UserProfile profileId;
}