package com.example.csgs.bean;

import lombok.Data;

@Data
public class CommunityInfo {
    private String community;
    private long numHouses;
    private long numResidents;
    private long numParkingSpaces;
    private PageQuery<Announcement> announcementList;

    public CommunityInfo(String community, long numHouses, long numResidents, long numParkingSpaces, PageQuery<Announcement> announcementList) {
        this.community = community;
        this.numHouses = numHouses;
        this.numResidents = numResidents;
        this.numParkingSpaces = numParkingSpaces;
        this.announcementList = announcementList;
    }
}
