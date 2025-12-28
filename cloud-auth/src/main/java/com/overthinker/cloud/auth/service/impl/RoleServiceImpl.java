package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.PO.Role;
import com.overthinker.cloud.auth.mapper.RoleMapper;
import com.overthinker.cloud.auth.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public Role getDfalultRole() {
        return null;
    }

    @Override
    public Void setDfalultRole() {
        return null;
    }
}
