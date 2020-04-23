package com.example.csgs.service.Impl;

import com.example.csgs.bean.PageQuery;
import com.example.csgs.bean.User;
import com.example.csgs.dao.ProfileDao;
import com.example.csgs.dao.UserDao;
import com.example.csgs.entity.OfGridEntity;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.entity.UserProfile;
import com.example.csgs.service.UserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class UserQueryServiceImpl implements UserQueryService {
    @Autowired
    UserDao userDAO;
    @Autowired
    ProfileDao profileDao;

    private List<User> userList = new ArrayList<>();
    private List<UserEntity> userEntityList = new ArrayList<>();
    private int pageSize = 10;
    private Pageable pageable;

    /**
     * 按 id 信息查询用户
     * 场景：用户进入自己的资料查看界面
     */
    @Override
    public UserEntity singleUserQuery(Long uid) {
        Optional<UserEntity> queryUser = userDAO.findById(uid);
        if (queryUser.isPresent()) {
            UserEntity user = queryUser.get();
            user.setUserPassword(null);
            user.setPwdProEntity(null);
            return user;
        }
        return null;
    }

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     */
    @Override
    public PageQuery allUserOfGrid(Long id, String page) {
        Optional<UserEntity> queryUser = userDAO.findById(id);

        if (queryUser.isPresent()) {
            String userDistrict = getUserDistrict(queryUser.get());

            pageable = PageRequest.of(Integer.parseInt(page)-1, pageSize, Sort.by("id").ascending());
            Iterable<UserProfile> byOfGridEntity = profileDao.findByDistrict(userDistrict, pageable);

            return addUserInList(byOfGridEntity, queryUser.get().getUserProfile(), userDistrict);
        }
        return null;
    }

    private String getUserDistrict(UserEntity userEntity) {
        UserProfile gridUserProfile = userEntity.getUserProfile();
        OfGridEntity ofGridEntity = gridUserProfile.getOfGridEntity();
        return ofGridEntity.getDistrict();
    }

    private PageQuery addUserInList(Iterable<UserProfile> byOfGridEntity, UserProfile gridUserProfile, String userDistrict) {
        userEntityList.clear();
        for (UserProfile userProfile : byOfGridEntity) {
            Optional<UserEntity> byUserProfile = userDAO.findByUserProfile(userProfile);
            if (byUserProfile.isPresent()) {
                if (!gridUserProfile.getId().equals(byUserProfile.get().getId())) {
                    byUserProfile.ifPresent(userEntityList::add);
                }
            }
        }
        userList.clear();
        for (UserEntity userEntity : userEntityList) {
            Long id1 = userEntity.getId();
            String userID = userEntity.getUserID();
            String userName = userEntity.getUserProfile().getUserName();
            String telPhone = userEntity.getUserProfile().getTelPhone();
            String district = userEntity.getUserProfile().getOfGridEntity().getDistrict();
            String community = userEntity.getUserProfile().getOfGridEntity().getCommunity();

            User user = new User(id1, userID, userName, telPhone, district, community);
            userList.add(user);
        }

        int size = userEntityList.size();
        int totalPage = 0;
        if (size != 0) {
            if (size % pageSize == 0) {
                totalPage = size / pageSize;
            } else {
                totalPage = size / pageSize + 1;
            }
        } else {
            return null;
        }
        return new PageQuery(pageable.getPageNumber() + 1, totalPage, size, userList);
    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：归属地区、归属地区和归属小区、
     */
    @Override
    public Object multipleConditions(String userID, String userName, String community, Long id, String page) {
        Optional<UserEntity> adminUser = userDAO.findById(id);

        if (adminUser.isPresent()) {
            if (adminUser.get().getUserType() != 1) return 0;
            if (!userID.equals("")) {
                Optional<UserEntity> queryUser = userDAO.findOneByUserID(userID);

                if (queryUser.isPresent()) {
                    UserEntity userEntity = queryUser.get();
                    if (getUserDistrict(userEntity).equals(getUserDistrict(adminUser.get()))) {
                        userEntity.setUserPassword(null);
                        userEntity.setPwdProEntity(null);
                        return userEntity;
                    }
                }
                return 0;
            } else {
                UserProfile userProfile = adminUser.get().getUserProfile();
                String userDistrict = getUserDistrict(adminUser.get());
                Iterable<UserProfile> resultProfile = null;
                pageable = PageRequest.of(Integer.parseInt(page)-1, pageSize, Sort.by("id").ascending());
                if (!userName.equals("") && !community.equals("")) {
                    resultProfile = profileDao.findByUserNameAndCommunity(userName, community, userDistrict, pageable);
                } else if (!userName.equals("")) {
                    resultProfile = profileDao.findByUserName(userName, userDistrict, pageable);
                } else if (!community.equals("")) {
                    resultProfile = profileDao.findByCommunity(community, userDistrict, pageable);
                }
                assert resultProfile != null;
                return addUserInList(resultProfile, userProfile, userDistrict);
            }
        }
        return null;
    }
}

