package com.example.csgs.dao;

import com.example.csgs.entity.OfGridEntity;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.entity.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface GridDao extends PagingAndSortingRepository<OfGridEntity, Long> {
    Optional<OfGridEntity> findById(Long uid);

    Optional<OfGridEntity> findByDistrictAndCommunity(String district,String community);

}
