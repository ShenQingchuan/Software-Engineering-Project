package com.example.csgs.mapper;


import com.example.csgs.entity.JournalEntity;
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
