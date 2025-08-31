package com.overthinker.cloud.ai.service;

import com.overthinker.cloud.ai.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// 接口命名规范：以 Service 结尾
public interface UserService {
    Mono<User> getUserById(Long id); // 单值流（Mono）
    Flux<User> getAllUsers(); // 多值流（Flux）
    Mono<User> saveUser(User user); // 插入或更新
    Mono<Void> deleteUserById(Long id); // 删除
}