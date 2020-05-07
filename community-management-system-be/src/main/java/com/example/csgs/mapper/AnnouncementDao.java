package com.example.csgs.mapper;


import com.example.csgs.entity.AnnouncementEntity;
import com.example.csgs.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementDao extends PagingAndSortingRepository<AnnouncementEntity, Long> {
    Optional<AnnouncementEntity> findById(Long uid);

    @Query(value = "select a from AnnouncementEntity a where a.creator = ?1")
    List<AnnouncementEntity> findByCreator_ToAnnouncement(UserEntity userEntity);

}
