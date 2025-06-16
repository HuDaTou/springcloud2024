package com.overthinker.cloud.ai.service.impl;

import com.overthinker.cloud.ai.entity.User;
import com.overthinker.cloud.ai.repository.UserRepository;
import com.overthinker.cloud.ai.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// 实现类命名规范：以 ServiceImpl 结尾
@Service("userService") // 显式指定 Bean 名称（可选）
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository; // 使用 ReactiveCrudRepository

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<Void> deleteUserById(Long id) {
        return userRepository.deleteById(id);
    }
}