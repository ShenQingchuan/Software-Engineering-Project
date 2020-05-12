package com.example.csgs.service.impl;

import com.example.csgs.bean.AreaList;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.*;
import com.example.csgs.mapper.*;
import com.example.csgs.service.GridManageService;
import com.example.csgs.utils.CalculatePageUtils;
import com.example.csgs.utils.SHA256Util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class GridManageServiceImpl implements GridManageService {
    final UserMapper userMapper;
    final ProfileMapper profileMapper;
    final CommunityInfoMapper communityInfoMapper;
    final AnnouncementMapper announcementMapper;
    final JournalTypeMapper journalTypeMapper;
    final JournalMapper journalMapper;
    final GridMapper gridMapper;
    final PwdProMapper pwdProMapper;

    public GridManageServiceImpl(UserMapper userMapper, ProfileMapper profileMapper,
                                 CommunityInfoMapper communityInfoMapper, AnnouncementMapper announcementMapper,
                                 JournalTypeMapper journalTypeMapper, JournalMapper journalMapper, GridMapper gridMapper,
                                 PwdProMapper pwdProMapper) {
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
        this.communityInfoMapper = communityInfoMapper;
        this.announcementMapper = announcementMapper;
        this.journalTypeMapper = journalTypeMapper;
        this.journalMapper = journalMapper;
        this.gridMapper = gridMapper;
        this.pwdProMapper = pwdProMapper;
    }

    private HashMap<String, Object> map = new HashMap<>();

    private void addInfoToMap(HashMap<String, Object> map, Long id, String titleName, String content) {
        map.put("titleName", titleName);
        map.put("content", content);
        map.put("creator", id);
        map.put("createTime", new Date());
    }

    /**
     * 发布公告接口
     * 场景：网格员发布管理区域公告（id为user表中网格员的id）
     */
    @Override
    public boolean releaseAnnouncement(Long id, String titleName, String content) {
        if (userMapper.judgeGrid(id) == 1) {
            addInfoToMap(map, id, titleName, content);
            return announcementMapper.addOneAnnouncement(map) > 0;
        }
        return false;
    }


    /**
     * 网格员发布日志接口
     * 场景：网格员进入日志管理界面，点击新增按钮
     */
    @Override
    public boolean releaseJournal(String titleName, String content, String type, Long id) {
        if (userMapper.judgeGrid(id) == 1) {
            Long typeId = journalTypeMapper.findByTypeName(type);
            addInfoToMap(map, id, titleName, content);
            map.put("typeId", typeId);
            return journalMapper.addOneJournal(map) > 0;
        }
        return false;
    }

    /**
     * 网格员获取所有自己发布的公告列表(分页返回数据)
     * 场景：网格员进入公告管理界面
     */
    @Override
    public PageQuery<Announcement> getAnnouncementList(Long id, String page) {
        if (userMapper.judgeGrid(id) > 0) {
            int pageSize = 10;
            Page<Announcement> pageAble = PageHelper.startPage(Integer.parseInt(page), pageSize);
            PageHelper.orderBy("id ASC");//按id升序排列
            List<Announcement> announcementList = announcementMapper.findAnnouncementByCreator(id);

            return CalculatePageUtils.getPageInfo(Integer.parseInt(page), pageSize, pageAble, announcementList);
        }
        return null;
    }

    /**
     * 网格员获取所有自己发布的日志列表(分页返回数据)
     * 场景：网格员进入日志管理界面(id为网格员的id)
     */
    @Override
    public PageQuery<Journal> getJournalList(Long id, String page) {
        if (userMapper.judgeGrid(id) > 0) {
            int pageSize = 10;
            Page<Journal> pageAble = PageHelper.startPage(Integer.parseInt(page), pageSize);
            PageHelper.orderBy("id ASC");//按id升序排列
            List<Journal> journalList = journalMapper.findJournalByCreator(id);

            return CalculatePageUtils.getPageInfo(Integer.parseInt(page), pageSize, pageAble, journalList);
        }
        return null;
    }

    /**
     * 网格员增加一个居民用户
     * 密码默认设置为身份证后六位
     * 同时指明该居民位于哪个地区（必须是在该网格员管理的区域之内）
     */
    @Override
    public boolean addResidentUser(String userID, String districtName, String communityName, String password) {
        UserEntity userEntity = userMapper.findOneByUserID(userID);
        if (userEntity == null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userID", userID);
            map.put("password", SHA256Util.getSHA256String(password));
            userMapper.addResidentUser(map);
            CommunityInfoEntity communityInfoEntity = communityInfoMapper.findByOfGrid(districtName, communityName);
            if (communityInfoEntity != null) {
                profileMapper.addUserProfile(communityInfoEntity.getId());
                pwdProMapper.addPwdPro();
                return true;
            }
        }
        return false;
    }

    /**
     * 获取该网格员所管理的区域（id为user表中网格员的id）
     */
    @Override
    public AreaList getManageAreaList(Long id) {
        if (userMapper.judgeGrid(id) > 0) {
            GridEntity gridEntity = gridMapper.findDistrictNameByUId(id);
            List<String> communityNameList = communityInfoMapper.findCommunityNameById(gridEntity.getId());
            String[] districtNameArray = communityNameList.toArray(new String[0]);
            return new AreaList(gridEntity.getDistrictName(), districtNameArray);
        }
        return null;
    }

}

