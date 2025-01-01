package com.overthinker.cloud.web.entity.enums.UserEnum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    STATUS_ENABLE(1, "启用"),
    STATUS_DISABLE(0, "禁用"),;



    @EnumValue
    private final Integer code;
    private final String message;

}
