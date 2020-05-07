package com.example.csgs.mapper;

import com.example.csgs.entity.DistrictEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DistrictDao extends PagingAndSortingRepository<DistrictEntity, Long> {
    Optional<DistrictEntity> findById(Long uid);
}
