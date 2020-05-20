package com.example.csgs.mapper;


import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.AnnouncementEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AnnouncementMapper {
    AnnouncementEntity findById(Long uid);

    List<Announcement> findAnnouncementByCreator(@Param("id") Long id);

    int addOneAnnouncement(HashMap<String, Object> map);

    int deleteAnnouncement(Long id);
}
