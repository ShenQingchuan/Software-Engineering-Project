package com.example.csgs.mapper;

import com.example.csgs.entity.FeLog;
import com.example.csgs.entity.LogTimeResult;
import com.example.csgs.entity.LogTypeResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeLogMapper {
    int insertLog(FeLog feLog);

    List<LogTypeResult> queryTypeLog();

    List<LogTimeResult> queryTimeLog();

}
