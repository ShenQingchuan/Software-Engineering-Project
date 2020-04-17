package com.example.csgs.dao;

import com.example.csgs.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProfileDao extends PagingAndSortingRepository<UserProfile, Long> {
    Optional<UserProfile> findById(Long uid);

    @Query(value = "select count(p.id) from UserProfile p join p.ofGridEntity g where g.district = :district")
    int queryByOfGridEntity(String district);

    @Query(value = "select p from UserProfile p join p.ofGridEntity g where g.district = :district")
    Page<UserProfile> findByDistrict(String district,Pageable pageable);

    @Query(value = "select p from UserProfile p join p.ofGridEntity g where p.userName = :userName and g.district = :district")
    Page<UserProfile>  findByUserName(String userName,String district,Pageable pageable);

    @Query(value = "select p from UserProfile p join p.ofGridEntity g where g.community = :community and g.district = :district")
    Page<UserProfile>  findByCommunity(String community,String district,Pageable pageable);

    @Query(value = "select p from UserProfile p join p.ofGridEntity g where g.community = :community and p.userName = :userName and g.district = :district")
    Page<UserProfile>  findByUserNameAndCommunity(String userName,String community,String district,Pageable pageable);
}
