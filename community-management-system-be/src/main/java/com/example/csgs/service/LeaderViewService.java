package com.example.csgs.service;

import com.example.csgs.bean.CommunityInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface LeaderViewService {
    List<CommunityInfo> getCommunityRPHList(Long id);
}
