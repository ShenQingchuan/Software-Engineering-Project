package com.example.csgs.service;

import com.example.csgs.bean.Announcement;
import com.example.csgs.bean.AreaList;
import com.example.csgs.bean.Journal;
import com.example.csgs.bean.PageQuery;

public interface GridManageService{

    boolean releaseAnnouncement(Long id, String titleName, String content);

    boolean releaseJournal(String titleName,String content,String type,Long id);

    boolean deleteAnnouncement(Long id);

    boolean deleteJournal(Long id);

    PageQuery<Announcement> getAnnouncementList(Long id, String page);

    PageQuery<Journal> getJournalList(Long id, String page);

    String getJournalContent(Long id);

    boolean addResidentUser(String userID,String district,String community);

    AreaList getManageAreaList(Long id);
}
