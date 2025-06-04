package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (UserRole)表实体类
 *
 * @author overH
 * @since 2023-11-17 16:33:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_user_role")
public class UserRole implements BaseData {
    //主键
    @TableId(value = "id" ,type = IdType.ASSIGN_ID)
    private Long id;
    //用户id
    private Long userId;
    //角色id
    private Long roleId;
}

