package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.auth.entity.PO.Role;

public interface RoleService extends IService<Role> {

    Role getDfalultRole();

    Void setDfalultRole();
}
