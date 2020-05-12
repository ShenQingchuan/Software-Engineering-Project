package com.example.csgs.mapper;

import com.example.csgs.entity.DistrictEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictMapper {
    DistrictEntity findById(Long id);

    List<DistrictEntity> findAllDistrict();
}
