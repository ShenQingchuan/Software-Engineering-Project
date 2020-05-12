package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PwdProEntity implements Serializable{
    private Long id;
    private String questionOne;
    private String answerOne;
    private String questionTwo;
    private String answerTwo;

    public PwdProEntity(String questionOne, String questionTwo) {
        this.questionOne = questionOne;
        this.questionTwo = questionTwo;
    }
}
