package com.example.csgs.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGridInfo {
    private AreaList areaList;
    private String userID;

//    //必须要这个无参构造函数
//    public CreateGridInfo() {
//    }
}
