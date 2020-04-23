package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userName;            //用户真实姓名

    String telPhone;            //用户电话号码

    @Column(length = 2)
    Integer sex;                //用户性别 0女 1男

    @Column(length = 50)
    Integer stature;            //用户身高

    String avatarUrl;           //用户头像地址

    String nation;              //民族

    String degreeOfEducation;   //文化程度

    String bloodType;           //血型

    String occupation;          //职业

    String email;               //用户邮箱

    String politicCountenance;  //用户政治面貌

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ofGrid")
    OfGridEntity ofGridEntity;  //用户所属网格

}
