package com.example.csgs.service;

import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.User;

public interface GridQueryService {

    PageQuery<User> allUserOfGrid(Long id, String page);

    PageQuery<User> multipleConditions(String userID, String userName, String community, Long id, String page);

    boolean deleteUser(Long uid);

}
