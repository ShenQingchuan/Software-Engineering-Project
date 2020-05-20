package com.example.csgs.service.impl;

import com.example.csgs.entity.*;
import com.example.csgs.mapper.*;
import com.example.csgs.service.GridManageService;
import com.example.csgs.utils.CalculatePageUtils;
import com.example.csgs.utils.SHA256Util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Log4j
@Service
public class GridManageServiceImpl implements GridManageService {
    @Resource
    UserMapper userMapper;
    @Resource
    ProfileMapper profileMapper;
    @Resource
    CommunityInfoMapper communityInfoMapper;
    @Resource
    AnnouncementMapper announcementMapper;
    @Resource
    JournalTypeMapper journalTypeMapper;
    @Resource
    JournalMapper journalMapper;
    @Resource
    GridMapper gridMapper;
    @Resource
    PwdProMapper pwdProMapper;

    private final HashMap<String, Object> map = new HashMap<>();

    private void addInfoToMap(HashMap<String, Object> map, Long id, String titleName, String content) {
        map.put("titleName", titleName);
        map.put("content", content);
        map.put("creator", id);
        map.put("createTime", new Date());
    }

    /**
     * 发布公告接口
     * 场景：网格员发布管理区域公告
     *
     * @param id        user表中网格员id
     * @param content   公告内容
     * @param titleName 公告标题
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
     *
     * @param id        user表中网格员id
     * @param titleName 日志标题
     * @param content   日志内容
     * @param type      日志类型
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
     *
     * @param id   user表中网格员id
     * @param page 当前请求页数
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
     *
     * @param id   user表中网格员id
     * @param page 当前请求页数
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
     *
     * @param userID        被添加用户身份证号
     * @param password      登陆密码（已经过前端加密）
     * @param districtName  区名
     * @param communityName 小区名
     */
    @Override
    public boolean addResidentUser(String userID, String districtName, String communityName, String password) {
        UserEntity userEntity = userMapper.findOneByUserID(userID);
        if (userEntity == null) {
            CommunityInfoEntity communityInfoEntity = communityInfoMapper.findByOfGrid(districtName, communityName);
            if (communityInfoEntity != null) {
                /*
                * 目的：在插入user事，同时在pwdPro和profile中对应插入一行数据，并且都与user表关联起来
                * 处理方法：先在profile和pwdPro表中插入一行数据，各自得到自己表中新增一行数据的主键id值
                * 最后在user表中，带上用户身份信息和之前两张表插入时，所得到的id一并插入到表中，实现user表与
                * 另外两张表对应数据行关联起来
                */
                InsertCommunityId insertCommunityId = new InsertCommunityId(communityInfoEntity.getId());
                profileMapper.insertProfile_selectKey(insertCommunityId);
                PwdProEntity pwdProEntity = new PwdProEntity();
                pwdProMapper.insertPwdPro_selectKey(pwdProEntity);

                HashMap<String, Object> map = new HashMap<>();
                map.put("userID", userID);
                map.put("password", SHA256Util.getSHA256String(password));
                map.put("profileId", insertCommunityId.getPid());
                map.put("pwdId", pwdProEntity.getId());
                return userMapper.addResidentUser(map) > 0;
            }
        }
        return false;
    }

    /**
     * 获取该网格员所管理的区域（id为user表中网格员的id）
     * @param id user表中网格员id
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

