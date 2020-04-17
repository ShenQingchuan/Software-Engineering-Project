package com.example.csgs.bean;

import lombok.Data;

@Data
public class User {
    private long id;
    private String userID;
    private String userName;
    private String telPhone;
    private String Community;
    private String District;

    public User(long uid, String userID, String userName, String telPhone, String community, String district) {
        this.id = uid;
        this.userID = userID;
        this.userName = userName;
        this.telPhone = telPhone;
        Community = community;
        District = district;
    }
}
