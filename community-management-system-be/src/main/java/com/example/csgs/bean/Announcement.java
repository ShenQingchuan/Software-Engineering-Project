package com.example.csgs.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Announcement {
    private long id;
    private String titleName;
    private String content;
    private String creator;
    private String creatTime;

    public Announcement(long id, String titleName, String content, String creator, String creatTime) {
        this.id = id;
        this.titleName = titleName;
        this.content = content;
        this.creator = creator;
        this.creatTime = creatTime;
    }
}
