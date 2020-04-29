package com.example.csgs.dao;

import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.GridEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityInfoDao extends PagingAndSortingRepository<CommunityInfoEntity, Long> {
    Optional<CommunityInfoEntity> findById(Long uid);

    @Query(value = "select c from CommunityInfoEntity c join c.districtID d where d.districtName = ?1 and c.communityName = ?2")
    CommunityInfoEntity findByOfGrid(String district,String community);

    @Query(value = "select c.communityName from CommunityInfoEntity c where c.districtID.districtName = ?1")
    String[] findByDistrictID_DistrictName(String districtName);

    @Query(value = "select c from CommunityInfoEntity c where c.districtID.districtName = ?1")
    List<CommunityInfoEntity> findByDistrictName_toCommunity(String districtName);

    Optional<CommunityInfoEntity> findByCommunityName(String communityName);

    List<CommunityInfoEntity> findByGridEntity(GridEntity gridEntity);
}
