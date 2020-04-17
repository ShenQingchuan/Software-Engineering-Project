package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String userID;

    @Column(nullable=false,name="userType",columnDefinition="int default 0")
    Integer userType;

    @Column(nullable = false)
    String userPassword;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userProfile", referencedColumnName = "id")
    public UserProfile userProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pwdProtection", referencedColumnName = "id")
    public PwdProtectionEntity pwdProtectionEntity;


}