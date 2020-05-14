package com.example.csgs.mapper;

import com.example.csgs.entity.*;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ProfileMapper{
    UserProfile findById(Long id);

    int updateProfile(HashMap<String, Object> map);

    ProfileInfo getMaterial(Long id);

    int findGridIdIsExist(Long id);

    CommunityInfoEntity findResidentRPH(Long id);

    CommunityInfoEntity findUserIdByProfileId(Long id);

    DistrictEntity findDistrictById(Long id);

    List<User> findProfileByDistrict(@Param("district") String district);

    int deleteById(@Param("id") Long id);

    void addUserProfile(Long communityId);
}
