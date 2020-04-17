package com.example.csgs.service;

import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.UserEntity;

public interface UserQueryService {
    UserEntity singleUserQuery(Long id);

    PageQuery allUserOfGrid(Long id, String page);

    Object multipleConditions(String userID,String userName,String community,Long id,String page);
}
