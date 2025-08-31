package com.overthinker.cloud.auth.entity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/4 9:42
 */
@Data
public class RoleDeleteDTO {
    @NotNull
    private List<Long> Ids;
}
