package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridPersonalInfo {
    private Long id;
    private String userName;
    private String userID;
    private String telPhone;
    private AreaList areaList;
}
