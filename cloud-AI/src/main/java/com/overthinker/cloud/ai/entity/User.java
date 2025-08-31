package com.overthinker.cloud.ai.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users") // 映射到 PostgreSQL 的 "users" 表
public class User {
    @Id // 映射到主键 id
    private Long id;
    private String name;
    private Integer age;
    private String phoneNumber;
    private Character gender;

    // Getters and Setters
}