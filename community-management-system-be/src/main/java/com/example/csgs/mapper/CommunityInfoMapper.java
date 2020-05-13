package com.example.csgs.mapper;

import com.example.csgs.entity.CommunityInfoEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityInfoMapper{
    CommunityInfoEntity findById(@Param("id") Long id);

    CommunityInfoEntity findByOfGrid(@Param("district") String district, @Param("community") String community);

    List<CommunityInfoEntity> findByDistrictID(@Param("id") Long id);

    List<String> findCommunityNameById(@Param("id") Long id);

    List<String> findCommunityByDistrictId(@Param("id") Long id);

    List<CommunityInfoEntity> findCommunityByDistrictName(@Param("district") String districtName);

    List<String> findCommunityNameByGridId(@Param("gridId") Long gridId);

    int setGridIdIsNull(@Param("id") Long id);

    void updateGridId(@Param("id") Long id, @Param("gridId") Long gridId);

    List<Long> findCommunityIdByGridId(@Param("id") Long id);
}
