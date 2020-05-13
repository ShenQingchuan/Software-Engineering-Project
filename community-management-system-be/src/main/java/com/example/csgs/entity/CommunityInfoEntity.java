package com.example.csgs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class CommunityInfoEntity {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;
    String communityName;               //所属小区
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long numHouses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long numResidents;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long numParkingSpaces;
    DistrictEntity districtId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    GridEntity gridId;
}