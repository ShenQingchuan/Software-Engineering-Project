package com.example.csgs.bean;

import com.example.csgs.entity.Announcement;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityInfo {
    private String community;
    private Long numHouses;
    private Long numResidents;
    private Long numParkingSpaces;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private PageQuery<Announcement> announcementList;

}
