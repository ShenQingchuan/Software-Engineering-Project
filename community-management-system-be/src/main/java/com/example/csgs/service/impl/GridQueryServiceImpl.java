package com.example.csgs.service.impl;

import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.DistrictEntity;
import com.example.csgs.entity.User;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.mapper.PwdProMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.GridQueryService;
import com.example.csgs.utils.CalculatePageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class GridQueryServiceImpl implements GridQueryService {
    final UserMapper userMapper;
    final ProfileMapper profileMapper;
    final PwdProMapper pwdProMapper;

    private int pageSize = 10;
    private Page<User> pageable = new Page<>(1,1);
    private List<User> userList = new ArrayList<>();

    public GridQueryServiceImpl(UserMapper userMapper, ProfileMapper profileMapper, PwdProMapper pwdProMapper) {
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
        this.pwdProMapper = pwdProMapper;
    }

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     */
    @Override
    public PageQuery<User> allUserOfGrid(Long id, String page) {
        UserEntity queryUser = userMapper.findById(id);

        if (queryUser != null && queryUser.getUserType() == 1) {
            DistrictEntity districtEntity = profileMapper.findDistrictById(id);

            pageable = PageHelper.startPage(Integer.parseInt(page), pageSize);
            PageHelper.orderBy("id ASC");
            userList = profileMapper.findProfileByDistrict(districtEntity.getDistrictName());

            return CalculatePageUtils.getPageInfo(Integer.parseInt(page), pageSize, pageable, userList);
        }
        return null;
    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：<归属地区、归属地区>、<归属小区>、<归属地区>、<userID>
     */
    @Override
    public PageQuery<User> multipleConditions(String userID, String userName, String community, Long id, String page) {
        if (userMapper.judgeGrid(id) == 1) {
            userList.clear();
            User user = userMapper.findUserByUserID(userID);
            if (!userID.equals("") && user !=null) {
                pageable.setTotal(1);
                userList.add(user);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("userName", userName);
                map.put("community", community);

                pageable = PageHelper.startPage(Integer.parseInt(page), pageSize);
                PageHelper.orderBy("id ASC");
                userList = userMapper.findUserByGridInfo(map);
            }
            return CalculatePageUtils.getPageInfo(Integer.parseInt(page), pageSize, pageable, userList);
        }
        return null;
    }

    @Override
    public boolean deleteUser(Long uid) {
        UserEntity userResident = userMapper.findById(uid);
        if (userResident != null && userResident.getUserType() == 0) {
            return profileMapper.deleteById(uid) > 0 && pwdProMapper.deleteById(uid) > 0;
        }
        return false;
    }
}

