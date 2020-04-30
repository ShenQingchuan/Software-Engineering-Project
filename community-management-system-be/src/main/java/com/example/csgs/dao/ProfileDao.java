package com.example.csgs.dao;

import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.DistrictEntity;
import com.example.csgs.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProfileDao extends PagingAndSortingRepository<UserProfile, Long> {
    Optional<UserProfile> findById(Long uid);

    @Query(value = "select p from UserProfile p join p.communityInfoEntity c where c.districtID.districtName = :district")
    Page<UserProfile> findByDistrict(String  district, Pageable pageable);

    @Query(value = "select p from UserProfile p join p.communityInfoEntity c where p.userName = :userName and c.districtID.districtName = :district")
    Page<UserProfile>  findByUserName(String userName,String district,Pageable pageable);

    @Query(value = "select p from UserProfile p join p.communityInfoEntity c where c.communityName = :community and c.districtID.districtName = :district")
    Page<UserProfile>  findByCommunity(String community,String district,Pageable pageable);

    @Query(value = "select p from UserProfile p join p.communityInfoEntity c where c.communityName = :community and p.userName = :userName and c.districtID.districtName = :district")
    Page<UserProfile>  findByUserNameAndCommunity(String userName,String community,String district,Pageable pageable);

    @Modifying
    @Query(value = "update UserProfile p set p.occupation = ?1 where p.id = ?2")
    void updateByOccupation(String occupation,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.userName = ?1 where p.id = ?2")
    void updateByUserName(String userName,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.avatarUrl = ?1 where p.id = ?2")
    void updateByAvatarUrl(String avatarUrl,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.bloodType = ?1 where p.id = ?2")
    void updateByBloodType(String bloodType,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.degreeOfEducation = ?1 where p.id = ?2")
    void updateByDegreeOfEducation(String degreeOfEducation,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.email = ?1 where p.id = ?2")
    void updateByEmail(String email,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.nation = ?1 where p.id = ?2")
    void updateByNation(String nation,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.politicCountenance = ?1 where p.id = ?2")
    void updateByPoliticCountenance(String politicCountenance,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.sex = ?1 where p.id = ?2")
    void updateBySex(Integer sex,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.stature = ?1 where p.id = ?2")
    void updateByStature(Integer stature,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.telPhone = ?1 where p.id = ?2")
    void updateByTelPhone(String telPhone,Long id);

    @Modifying
    @Query(value = "update UserProfile p set p.communityInfoEntity = ?1 where p.id = ?2")
    void updateByOfGridEntity(CommunityInfoEntity communityInfoEntity, Long id);
}
