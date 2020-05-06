package com.example.csgs.dao;

import com.example.csgs.entity.PwdProEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PwdProDao extends PagingAndSortingRepository<PwdProEntity, Long> {
    Optional<PwdProEntity> findById(Long uid);
}
