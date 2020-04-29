package com.example.csgs.dao;

import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.DistrictEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DistrictDao extends PagingAndSortingRepository<DistrictEntity, Long> {
    Optional<DistrictEntity> findById(Long uid);
}
