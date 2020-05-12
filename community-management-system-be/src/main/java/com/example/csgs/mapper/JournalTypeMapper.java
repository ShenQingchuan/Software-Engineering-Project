package com.example.csgs.mapper;

import com.example.csgs.entity.JournalTypeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalTypeMapper {
    JournalTypeEntity findById(Long uid);

    Long findByTypeName(String typeName);

    List<String> findAllTypeName();
}
