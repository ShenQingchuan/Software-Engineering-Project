package com.example.csgs.mapper;


import com.example.csgs.entity.AdministratorEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AdminDao extends PagingAndSortingRepository<AdministratorEntity, Long> {
    Optional<AdministratorEntity> findById(Long uid);
}
