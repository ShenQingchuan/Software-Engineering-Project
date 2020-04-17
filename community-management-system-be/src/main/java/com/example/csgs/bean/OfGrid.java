package com.example.csgs.bean;

import lombok.Data;

@Data
public class OfGrid {
    private String community;
    private String district;

    public OfGrid(String community, String district) {
        this.community = community;
        this.district = district;
    }
}
