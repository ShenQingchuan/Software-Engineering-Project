package com.example.csgs.mapper;


import com.example.csgs.entity.Journal;
import com.example.csgs.entity.JournalEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface JournalMapper {
    JournalEntity findById(Long uid);

    List<Journal> findJournalByCreator(@Param("id") Long id);

    int addOneJournal(HashMap<String, Object> map);

    String findContentById(@Param("id") Long id);

    int deleteJournal(Long id);
}
