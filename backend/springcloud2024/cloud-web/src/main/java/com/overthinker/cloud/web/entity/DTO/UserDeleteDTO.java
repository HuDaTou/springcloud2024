package com.overthinker.cloud.web.entity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/19 10:07
 */
@Data
public class UserDeleteDTO {

    /**
     * 用户id列表
     */
    @NotNull
    List<Long> Ids;
}
