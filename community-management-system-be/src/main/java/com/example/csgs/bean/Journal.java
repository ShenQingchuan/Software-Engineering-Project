package com.example.csgs.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Journal {
    private long id;
    private String titleName;
    private String type;
    private String creator;
    private String creatTime;

    public Journal(long id, String titleName, String type, String creator, String creatTime) {
        this.id = id;
        this.titleName = titleName;
        this.type = type;
        this.creator = creator;
        this.creatTime = creatTime;
    }
}
