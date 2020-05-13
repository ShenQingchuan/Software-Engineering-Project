package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileInfo implements Serializable{
    private Long id;
    private String userName;            //用户真实姓名
    private String telPhone;            //用户电话号码
    private Integer sex;                //用户性别 0女 1男
    private Integer stature;            //用户身高
    private String avatarUrl;           //用户头像地址
    private String nation;              //民族
    private String degreeOfEducation;   //文化程度
    private String bloodType;           //血型
    private String occupation;          //职业
    private String email;               //用户邮箱
    private String politicCountenance;  //用户政治面貌
    private OfGrid ofGrid;

}
