package com.example.csgs.entity;

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

}
