package com.example.csgs.service.Impl;

import com.example.csgs.bean.*;
import com.example.csgs.dao.*;
import com.example.csgs.entity.*;
import com.example.csgs.service.GridManageService;
import com.example.csgs.utils.CalculatePageUtils;
import com.example.csgs.utils.GetAnnounceListUtils;
import com.example.csgs.utils.SHA256Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GridManageServiceImpl implements GridManageService {
    @Autowired
    UserDao userDAO;
    @Autowired
    ProfileDao profileDao;
    @Autowired
    CommunityInfoDao communityInfoDao;
    @Autowired
    AnnouncementDao announcementDao;
    @Autowired
    JournalTypeDao journalTypeDao;
    @Autowired
    JournalDao journalDao;
    @Autowired
    GridDao gridDao;

    /**
     * 发布公告接口
     * 场景：网格员发布管理区域公告（id为user表中网格员的id）
     */
    @Override
    public boolean releaseAnnouncement(Long id, String titleName, String content) {
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        Optional<UserEntity> gridUser = userDAO.findById(id);
        if (gridUser.isPresent()) {
            announcementEntity.setTitleName(titleName);
            announcementEntity.setContent(content);
            announcementEntity.setCreator(gridUser.get());
            announcementDao.save(announcementEntity);
            return true;
        }
        return false;
    }

    /**
     * 网格员发布日志接口
     * 场景：网格员进入日志管理界面，点击新增按钮
     */
    @Override
    public boolean releaseJournal(String titleName, String content, String type, Long id) {
        JournalEntity journalEntity = new JournalEntity();
        Optional<UserEntity> gridUser = userDAO.findById(id);
        if (gridUser.isPresent()) {
            journalEntity.setTitleName(titleName);
            journalEntity.setContent(content);
            journalEntity.setCreator(gridUser.get());

            Optional<JournalTypeEntity> journalTypeEntity = journalTypeDao.findByTypeName(type);
            journalTypeEntity.ifPresent(journalEntity::setType);
            journalDao.save(journalEntity);
            return true;
        }
        return false;
    }

    /**
     * 删除公告接口
     * 场景：网格员删除某一条公告
     */
    @Override
    public boolean deleteAnnouncement(Long id) {
        Optional<AnnouncementEntity> announcement = announcementDao.findById(id);
        if (announcement.isPresent()) {
            announcement.get().setCreator(null);
            announcementDao.delete(announcement.get());
            return true;
        }
        return false;
    }

    /**
     * 删除日志接口
     * 场景：网格员删除某一条日志
     */
    @Override
    public boolean deleteJournal(Long id) {
        Optional<JournalEntity> journalEntity = journalDao.findById(id);
        if (journalEntity.isPresent()) {
            journalEntity.get().setCreator(null);
            journalEntity.get().setType(null);
            journalDao.delete(journalEntity.get());
            return true;
        }
        return false;
    }

    /**
     * 网格员获取所有自己发布的公告列表(分页返回数据)
     * 场景：网格员进入公告管理界面
     */
    @Override
    public PageQuery<Announcement> getAnnouncementList(Long id, String page) {
        return GetAnnounceListUtils.getAnnouncementList(userDAO,announcementDao,page,id);
    }

    /**
     * 网格员获取所有自己发布的日志列表(分页返回数据)
     * 场景：网格员进入日志管理界面(id为网格员的id)
     */
    @Override
    public PageQuery<Journal> getJournalList(Long id, String page) {
        Optional<UserEntity> gridUser = userDAO.findById(id);
        if (gridUser.isPresent() && gridUser.get().getUserType() == 1) {//判断该用户是否是网格员
            int pageSize = 10;
            int size = 0;
            List<JournalEntity> journalEntityList = journalDao.findByCreator_ToJournal(gridUser.get());
            List<Journal> journalList = new ArrayList<>();
            Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize, Sort.by("id").ascending());

            for (JournalEntity journalEntity : journalEntityList) {
                Long id1 = journalEntity.getId();
                String titleName = journalEntity.getTitleName();
                String typeName = journalEntity.getType().getTypeName();
                Date date = journalEntity.getCreateTime();
                String createTime = DateFormatUtils.format(date, "yyyy年MM月dd日 HH:mm");
                String userName = journalEntity.getCreator().getUserProfile().getUserName();

                Journal journal = new Journal(id1, titleName, typeName, userName, createTime);
                journalList.add(journal);
                size++;
            }
            return CalculatePageUtils.getPageInfo(size, pageSize, pageable, journalList);
        }
        return null;
    }

    /**
     * 网格员获取获取某条日志的详情界面（意思就是要将日志的内容展示出来）
     * 场景：网格员点击日志列表某一条日志，查看日志内容
     */
    @Override
    public String getJournalContent(Long id) {
        Optional<JournalEntity> journalEntity = journalDao.findById(id);
        return journalEntity.map(JournalEntity::getContent).orElse(null);
    }

    /**
     * 网格员增加一个居民用户
     * 密码默认设置为身份证后六位
     * 同时指明该居民位于哪个地区（必须是在该网格员管理的区域之内）
     */
    @Override
    public boolean addResidentUser(String userID,String district,String community) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserType(0);
        userEntity.setUserID(userID);
        String plainPassword = userID.substring(12, 18);
        userEntity.setUserPassword(SHA256Util.getSHA256String(plainPassword));//SHA256加密

        UserProfile userProfile = new UserProfile();
        CommunityInfoEntity communityInfoEntity = communityInfoDao.findByOfGrid(district, community);
        userProfile.setCommunityInfoEntity(communityInfoEntity);
        profileDao.save(userProfile);

        userDAO.save(userEntity);
        return true;
    }

    /**
     * 获取该网格员所管理的区域（id为user表中网格员的id）
     */
    @Override
    public AreaList getManageAreaList(Long id) {
        Optional<UserEntity> gridUser = userDAO.findById(id);
        if (gridUser.isPresent() && gridUser.get().getUserType() == 1) {
            UserEntity userEntity = gridUser.get();
            Optional<GridEntity> gridEntity = gridDao.findByUserEntity(userEntity);
            if (gridEntity.isPresent()) {
                String districtName = gridEntity.get().getDistrictName();
                List<CommunityInfoEntity> infoEntities = communityInfoDao.findByGridEntity(gridEntity.get());
                List<String> communityNameList = new ArrayList<>();

                for (CommunityInfoEntity infoEntity : infoEntities) {
                    communityNameList.add(infoEntity.getCommunityName());
                }
                String[] districtNameArray = communityNameList.toArray(new String[0]);

                return new AreaList(districtName, districtNameArray);
            }
        }
        return null;
    }

}

