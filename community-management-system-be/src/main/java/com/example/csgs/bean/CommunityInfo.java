package com.example.csgs.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageQuery<Announcement> announcementList;

    public CommunityInfo(String community, long numHouses, long numResidents, long numParkingSpaces) {
        this.community = community;
        this.numHouses = numHouses;
        this.numResidents = numResidents;
        this.numParkingSpaces = numParkingSpaces;
    }
}
