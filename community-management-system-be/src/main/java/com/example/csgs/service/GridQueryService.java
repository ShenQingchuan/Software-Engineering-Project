package com.example.csgs.service;

import com.example.csgs.entity.PageQuery;
import com.example.csgs.entity.User;

public interface GridQueryService {

    /**
     * 通过网格员id查询居民用户信息列表
     * 场景：网格员登陆进入主界面，数据列表界面
     * @param id user表中网格员id
     * @param page 当前请求页数
     */
    PageQuery<User> allUserOfGrid(Long id, String page);

    /**
     * 按各种用户身份信息进行查询
     * 场景：网格员查询用户信息
     * 组合：<归属地区、归属地区>、<归属小区>、<归属地区>、<userID>
     * @param id user表中网格员id
     * @param page 当前请求页数
     * @param userID 所查询用户的身份证号
     * @param userName 所查询用户的姓名
     * @param community 所查询用户所在的小区名
     */
    PageQuery<User> multipleConditions(String userID, String userName, String community, Long id, String page);

    /**
     * 网格员删除居民用户
     * @param id 所要删除用户在user表中的id
     */
    boolean deleteUser(Long id);

}
