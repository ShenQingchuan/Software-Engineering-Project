package com.example.csgs.bean;

import lombok.Data;

@Data
public class GridPersonalInfo {
    private Long id;
    private String userName;
    private String telPhone;
    private AreaList areaList;

    public GridPersonalInfo(Long id, String userName, String telPhone, AreaList areaList) {
        this.id = id;
        this.userName = userName;
        this.telPhone = telPhone;
        this.areaList = areaList;
    }
}
