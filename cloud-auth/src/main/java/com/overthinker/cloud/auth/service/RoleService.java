package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.auth.entity.PO.SysRole;

public interface RoleService extends IService<SysRole> {

    SysRole getDfalultRole();

    Void setDfalultRole();
}
