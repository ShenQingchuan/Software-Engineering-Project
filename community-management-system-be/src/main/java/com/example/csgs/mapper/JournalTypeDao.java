package com.example.csgs.mapper;


import com.example.csgs.entity.JournalTypeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface JournalTypeDao extends PagingAndSortingRepository<JournalTypeEntity, Long> {
    Optional<JournalTypeEntity> findById(Long uid);

    Optional<JournalTypeEntity> findByTypeName(String typeName);
}
