package com.example.csgs.service.Impl;

import com.example.csgs.bean.PageQuery;
import com.example.csgs.bean.User;
import com.example.csgs.mapper.ProfileDao;
import com.example.csgs.mapper.PwdProDao;
import com.example.csgs.mapper.UserDao;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.entity.UserProfile;
import com.example.csgs.utils.CalculatePageUtils;
import com.example.csgs.service.GridQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GridQueryServiceImpl implements GridQueryService {
    @Autowired
    UserDao userDAO;
    @Autowired
    ProfileDao profileDao;
    @Autowired
    PwdProDao pwdProDao;

    private List<User> userList = new ArrayList<>();
    private List<UserEntity> userEntityList = new ArrayList<>();
    private int pageSize = 10;
    private Pageable pageable;

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     */
    @Override
    public PageQuery<User> allUserOfGrid(Long id, String page) {
        Optional<UserEntity> queryUser = userDAO.findById(id);

        if (queryUser.isPresent() && queryUser.get().getUserType() == 1) {
            String userDistrict = getUserDistrict(queryUser.get());

            pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize, Sort.by("id").ascending());
            Iterable<UserProfile> userProfileList = profileDao.findByDistrict(userDistrict, pageable);

            return addUserInList(userProfileList, queryUser.get().getUserProfile());
        }
        return null;
    }

    private String getUserDistrict(UserEntity userEntity) {
        UserProfile gridUserProfile = userEntity.getUserProfile();
        CommunityInfoEntity communityInfoEntity = gridUserProfile.getCommunityInfoEntity();
        return communityInfoEntity.getDistrictID().getDistrictName();
    }

    private PageQuery<User> addUserInList(Iterable<UserProfile> userProfileList, UserProfile gridUserProfile) {
        userEntityList.clear();
        for (UserProfile userProfile : userProfileList) {
            Optional<UserEntity> userEntity = userDAO.findByUserProfile(userProfile);
            if (userEntity.isPresent()) {
                if (!gridUserProfile.getId().equals(userEntity.get().getId())) {
                    userEntity.ifPresent(userEntityList::add);
                }
            }
        }
        userList.clear();
        for (UserEntity userEntity : userEntityList) {
            userList.add(getUserBean(userEntity));
        }
        return  CalculatePageUtils.getPageInfo(userEntityList.size(),pageSize,pageable,userList);

    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：<归属地区、归属地区>、<归属小区>、<归属地区>、<userID>
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
                        return getUserBean(userEntity);
                    }
                }
                return 0;
            } else {
                UserProfile userProfile = adminUser.get().getUserProfile();
                String userDistrict = getUserDistrict(adminUser.get());
                Iterable<UserProfile> resultProfile = null;
                pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize, Sort.by("id").ascending());
                if (!userName.equals("") && !community.equals("")) {
                    resultProfile = profileDao.findByUserNameAndCommunity(userName, community, userDistrict, pageable);
                } else if (!userName.equals("")) {
                    resultProfile = profileDao.findByUserName(userName, userDistrict, pageable);
                } else if (!community.equals("")) {
                    resultProfile = profileDao.findByCommunity(community, userDistrict, pageable);
                }
                assert resultProfile != null;
                return addUserInList(resultProfile, userProfile);
            }
        }
        return null;
    }

    @Override
    public boolean deleteUser(Long uid) {
        Optional<UserEntity> targetUser = userDAO.findById(uid);
        if (targetUser.isPresent() && targetUser.get().getUserType() == 0) {
            targetUser.get().getUserProfile().setCommunityInfoEntity(null);
            profileDao.save(targetUser.get().getUserProfile());
            userDAO.deleteById(uid);
            return true;
        }
        return false;
    }

    public User getUserBean(UserEntity userEntity){
        Long id1 = userEntity.getId();
        String userID = userEntity.getUserID();
        String userName = userEntity.getUserProfile().getUserName();
        String telPhone = userEntity.getUserProfile().getTelPhone();
        String district = userEntity.getUserProfile().getCommunityInfoEntity().getDistrictID().getDistrictName();
        String community = userEntity.getUserProfile().getCommunityInfoEntity().getCommunityName();

        return new User(id1, userID, userName, telPhone, district, community);
    }
}

