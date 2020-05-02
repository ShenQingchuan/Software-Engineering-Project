package com.example.csgs.dao;


import com.example.csgs.entity.AnnouncementEntity;
import com.example.csgs.entity.JournalTypeEntity;
import com.example.csgs.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface JournalTypeDao extends PagingAndSortingRepository<JournalTypeEntity, Long> {
    Optional<JournalTypeEntity> findById(Long uid);

    Optional<JournalTypeEntity> findByTypeName(String typeName);
}
