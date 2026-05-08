package com.overthinker.cloud.auth.entity.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/4 10:18
 */
@Data
public class RoleSearchDTO {
    // 角色名称
    private String roleName;
    //角色字符
    private String roleKey;
    // 状态（0：正常，1：停用）
    private Integer status;
    //创建时间，开始时间
    private LocalDateTime createTimeStart;
    //创建时间，结束时间
    private LocalDateTime createTimeEnd;
}
