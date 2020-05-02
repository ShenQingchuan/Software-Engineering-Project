package com.example.csgs.dao;

import com.example.csgs.entity.DistrictEntity;
import com.example.csgs.entity.GridEntity;
import com.example.csgs.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface GridDao extends PagingAndSortingRepository<GridEntity, Long> {
    Optional<GridEntity> findById(Long uid);

    Optional<GridEntity> findByDistrictName(String districtName);

    Optional<GridEntity> findByUserEntity(UserEntity userEntity);
}
