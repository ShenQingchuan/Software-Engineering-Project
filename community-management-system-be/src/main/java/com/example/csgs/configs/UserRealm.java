package com.example.csgs.configs;

import com.example.csgs.entity.UserEntity;
import com.example.csgs.mapper.UserMapper;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Log4j
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserMapper userMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> authority = new HashSet<>();

        Subject subject = SecurityUtils.getSubject();
        UserEntity currentUser = (UserEntity) subject.getPrincipal();
        switch (currentUser.getUserType()) {
            case 1:
                log.info("[网格员]进行授权");
                authority.add("grid");
                break;
            case 2:
                log.info("[领导]进行授权");
                authority.add("leader");
                break;
            case 3:
                log.info("[系统管理员]进行授权");
                authority.add("leader");
                authority.add("admin");
                break;
        }
        info.addStringPermissions(authority);
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        UserEntity userEntity = userMapper.findOneByUserID(userToken.getUsername());
        if (userEntity != null) {
            log.info("userID<"+ userEntity.getUserID() + ">进行认证");
            return new SimpleAuthenticationInfo(userEntity, userEntity.getUserPassword(), "");
        }
        return null;
    }
}
