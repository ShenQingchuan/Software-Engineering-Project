package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeLog {
    private Long id;
    private String event;
    private String type;
    private String page;
    private String createTime;
    private Long userId;

    public FeLog(String event, String type, String page, String createTime) {
        this.event = event;
        this.type = type;
        this.page = page;
        this.createTime = createTime;
    }
}
