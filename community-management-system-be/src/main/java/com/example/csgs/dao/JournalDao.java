package com.example.csgs.dao;


import com.example.csgs.entity.AnnouncementEntity;
import com.example.csgs.entity.JournalEntity;
import com.example.csgs.entity.JournalTypeEntity;
import com.example.csgs.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface JournalDao extends PagingAndSortingRepository<JournalEntity, Long> {
    Optional<JournalEntity> findById(Long uid);

    @Query(value = "select j from JournalEntity j where j.creator = ?1")
    List<JournalEntity> findByCreator_ToJournal(UserEntity userEntity);
}
