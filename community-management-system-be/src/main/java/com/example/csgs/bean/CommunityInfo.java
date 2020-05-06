package com.example.csgs.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityInfo {
    private String community;
    private long numHouses;
    private long numResidents;
    private long numParkingSpaces;
    private PageQuery<Announcement> announcementList;


}
