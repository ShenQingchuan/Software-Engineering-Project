package com.example.csgs.dao;


import com.example.csgs.entity.AdministratorEntity;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.entity.UserProfile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AdminDao extends PagingAndSortingRepository<AdministratorEntity, Long> {
    Optional<AdministratorEntity> findById(Long uid);
}
