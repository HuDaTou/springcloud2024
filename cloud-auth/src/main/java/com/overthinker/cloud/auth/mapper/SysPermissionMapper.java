package com.overthinker.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.overthinker.cloud.auth.entity.PO.SysPermission;
import com.overthinker.cloud.auth.entity.PO.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
}