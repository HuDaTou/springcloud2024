package com.overthinker.cloud.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.security.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-04
 */
public interface IUserService extends IService<User> {

    boolean saveUserDetails(User user);
}
