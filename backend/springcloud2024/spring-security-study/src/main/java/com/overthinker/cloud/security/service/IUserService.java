package com.overthinker.cloud.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.security.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-04
 */
public interface IUserService extends IService<User> , UserDetailsService {

    boolean saveUserDetails(User user);
}
