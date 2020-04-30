package com.example.csgs.bean;

import lombok.Data;

@Data
public class AreaList {
    private String districtName;
    private String[] communityArray;

    public AreaList(String districtName, String[] communityArray) {
        this.districtName = districtName;
        this.communityArray = communityArray;
    }

    //必须要这个无参构造函数
    public AreaList() {
    }
}
