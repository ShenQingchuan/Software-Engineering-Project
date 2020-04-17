package com.example.csgs.dao;


import com.example.csgs.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserDao extends PagingAndSortingRepository<UserEntity, Long> {
    Optional<UserEntity> findById(Long uid);

    Optional<UserEntity> findOneByUserAccount(String userAccount);

}
