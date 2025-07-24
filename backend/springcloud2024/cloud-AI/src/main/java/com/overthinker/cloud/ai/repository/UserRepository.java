package com.overthinker.cloud.ai.repository;


import com.overthinker.cloud.ai.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data R2DBC repository for the User entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    // Spring Data will automatically implement basic CRUD operations like save, findById, etc.
    // All methods will return reactive types (Mono or Flux).
}