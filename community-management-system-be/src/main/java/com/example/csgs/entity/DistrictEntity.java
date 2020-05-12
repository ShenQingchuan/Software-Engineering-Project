package com.example.csgs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictEntity{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;
    String districtName;                //所属区
}