package com.overthinker.cloud.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.security.config.DBUserDetailsManager;
import com.overthinker.cloud.security.entity.User;
import com.overthinker.cloud.security.mapper.UserMapper;
import com.overthinker.cloud.security.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Resource
    DBUserDetailsManager dbUserDetailsManager;

    @Override
    public boolean saveUserDetails(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder().username(user.getUsername()).password(user.getPassword()).build();
        dbUserDetailsManager.createUser(userDetails);
        return true;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
