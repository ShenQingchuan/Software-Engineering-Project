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
@Table(name = "pwdProtection")
public class PwdProtectionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String questionOne;

    @Column(nullable = false)
    String answerOne;

    @Column(nullable = false)
    String questionTwo;

    @Column(nullable = false)
    String answerTwo;

}
