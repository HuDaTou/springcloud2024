package com.overthinker.cloud.ai.repository;

import com.overthinker.cloud.ai.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User,Long> {

    // 自定义查询方法（可选）
    Flux<User> findByName(String name);
}
