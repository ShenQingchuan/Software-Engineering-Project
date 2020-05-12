package com.example.csgs.service;

import com.example.csgs.bean.AreaList;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.Journal;

public interface GridManageService {

    boolean releaseAnnouncement(Long id, String titleName, String content);

    boolean releaseJournal(String titleName, String content, String type, Long id);

    PageQuery<Announcement> getAnnouncementList(Long id, String page);

    PageQuery<Journal> getJournalList(Long id, String page);

    boolean addResidentUser(String userID, String districtName, String communityName, String password);

    AreaList getManageAreaList(Long id);
}
