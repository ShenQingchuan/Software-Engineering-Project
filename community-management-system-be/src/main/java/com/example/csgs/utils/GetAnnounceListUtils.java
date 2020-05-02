package com.example.csgs.utils;

import com.example.csgs.bean.Announcement;
import com.example.csgs.bean.CommunityInfo;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.dao.AnnouncementDao;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.AnnouncementEntity;
import com.example.csgs.entity.UserEntity;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GetAnnounceListUtils {
    public static PageQuery<Announcement> getAnnouncementList(UserDao userDAO, AnnouncementDao announcementDao,String page,Long id){
        Optional<UserEntity> gridUser = userDAO.findById(id);
        if (gridUser.isPresent() && gridUser.get().getUserType() == 1) {//判断该用户是否是网格员
            int pageSize = 10;
            int size = 0;
            List<AnnouncementEntity> announcementEntityList = announcementDao.findByCreator_ToAnnouncement(gridUser.get());
            List<Announcement> announcementList = new ArrayList<>();
            Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize, Sort.by("id").ascending());

            for (AnnouncementEntity announcementEntity : announcementEntityList) {
                Long id1 = announcementEntity.getId();
                String titleName = announcementEntity.getTitleName();
                String content = announcementEntity.getContent();
                Date date = announcementEntity.getCreateTime();
                String createTime = DateFormatUtils.format(date, "yyyy年MM月dd日 HH:mm");
                String userName = announcementEntity.getCreator().getUserProfile().getUserName();

                Announcement announcement = new Announcement(id1, titleName, content, userName, createTime);
                announcementList.add(announcement);
                size++;
            }
            return CalculatePageUtils.getPageInfo(size, pageSize, pageable, announcementList);
        }
        return null;
    }
}
