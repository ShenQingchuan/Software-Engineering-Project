package com.example.csgs.mapper;

import com.example.csgs.entity.GridEntity;
import com.example.csgs.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GridMapper {
    GridEntity findById(@Param("id") Long uid);

    GridEntity findByDistrictNameId(@Param("district") String district, @Param("id") Long id);

    GridEntity findByUserId(UserEntity userEntity);

    GridEntity findDistrictNameByUId(Long id);

    void addOneGrid(@Param("id") Long id, @Param("districtName") String districtName);

    List<GridEntity> findAll();

    int deleteGrid(Long id);
}
