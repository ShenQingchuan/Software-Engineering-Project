package com.example.csgs.bean;

import lombok.Data;

@Data
public class CommunityInfo {
    private String community;
    private long numHouses;
    private long numResidents;
    private long numSparkingSpaces;

    public CommunityInfo(String community,long numHouses, long numResidents, long numSparkingSpaces) {
        this.numHouses = numHouses;
        this.numResidents = numResidents;
        this.numSparkingSpaces = numSparkingSpaces;
        this.community = community;
    }
}
