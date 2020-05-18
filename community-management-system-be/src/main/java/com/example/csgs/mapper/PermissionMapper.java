package com.example.csgs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionMapper {
    int findUserTypeByPermissionName(String permissionName);
}
