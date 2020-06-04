package com.example.csgs.mapper;

import com.example.csgs.entity.FeLog;
import org.springframework.stereotype.Repository;

@Repository
public interface FeLogMapper {
    int insertLog(FeLog feLog);
}
