package com.example.csgs.service.impl;

import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.DistrictEntity;
import com.example.csgs.entity.User;
import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.mapper.PwdProMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.service.GridQueryService;
import com.example.csgs.utils.CalculatePageUtil;
import com.example.csgs.utils.ElasticSearchUtil;
import com.example.csgs.utils.MapToBeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@Service
public class GridQueryServiceImpl implements GridQueryService {
    @Resource
    UserMapper userMapper;
    @Resource
    ProfileMapper profileMapper;
    @Resource
    PwdProMapper pwdProMapper;

    private final int pageSize = 10;
    private Page<User> pageable = new Page<>(1, 1);
    private List<User> userList = new ArrayList<>();

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     *
     * @param id   user表中网格员id
     * @param page 当前请求页数
     */
    @Override
    public PageQuery<User> allUserOfGrid(Long id, String page) {
        UserEntity queryUser = userMapper.findById(id);
        log.info(id);
        if (queryUser != null && queryUser.getUserType() == 1) {
            DistrictEntity districtEntity = profileMapper.findDistrictById(id);
            log.info("片区名称：" + districtEntity.getDistrictName());
            pageable = PageHelper.startPage(Integer.parseInt(page), pageSize);
            PageHelper.orderBy("id ASC");
            userList = profileMapper.findProfileByDistrict(districtEntity.getDistrictName());

            return CalculatePageUtil.getPageInfo(Integer.parseInt(page), pageSize, pageable, userList);
        }
        return null;
    }

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：<归属小区、姓名>、<归属小区>、<姓名>、<userID>
     *
     * @param id        user表中网格员id
     * @param page      当前请求页数
     * @param userID    所查询用户的身份证号
     * @param userName  所查询用户的姓名
     * @param community 所查询用户所在的小区名
     */
    @Override
    public PageQuery<User> multipleConditions(String userID, String userName, String community, Long id, String page) throws IOException {
        if (userMapper.judgeGrid(id) == 1) {
            userList.clear();
            User user = userMapper.findUserByUserID(userID);
            if (!userID.equals("") && user != null) {
                pageable.setTotal(1);
                userList.add(user);
            } else {
                ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil();
                if (userName != null && community == null) {
                    SearchResponse searchResponse = elasticSearchUtil.termQuery(
                            "user", QueryBuilders.termQuery("userName", userName));
                    addDataToUserList(searchResponse);
                } else if (community != null && userName == null) {
                    SearchResponse searchResponse = elasticSearchUtil.termQuery(
                            "user", QueryBuilders.termQuery("community", community));
                    addDataToUserList(searchResponse);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("userName", userName);
                    map.put("community", community);

                    pageable = PageHelper.startPage(Integer.parseInt(page), pageSize);
                    PageHelper.orderBy("id ASC");
                    userList = userMapper.findUserByGridInfo(map);
                }
            }
            return CalculatePageUtil.getPageInfo(Integer.parseInt(page), pageSize, pageable, userList);
        }
        return null;
    }

    /**
     * 从searchResponse中拆解数据（多个User实体类）到List集合中
     * @param searchResponse 使用ElasticSearch所查询到的数据类
     */
    private void addDataToUserList(SearchResponse searchResponse) {
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            log.info(documentFields.getSourceAsMap());
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
            User user1 = MapToBeanUtil.mapToBean(sourceAsMap, new User());
            userList.add(user1);
        }
    }

    /**
     * 网格员删除居民用户
     *
     * @param id 所要删除用户在user表中的id
     */
    @Override
    public boolean deleteUser(Long id) {
        UserEntity userResident = userMapper.findById(id);
        if (userResident != null && userResident.getUserType() == 0) {
            return profileMapper.deleteById(id) > 0 && pwdProMapper.deleteById(id) > 0;
        }
        return false;
    }
}

