package com.overthinker.cloud.web.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author overH
 * <p>
 * 创建时间：2023/11/28 9:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoleAllVO {
    private Long id;
    private String roleName;
    private String roleKey;
    private Integer isDeleted;
    private Integer status;
    private Long orderNum;
    private String remark;
    private String createTime;
    private String updateTime;
}