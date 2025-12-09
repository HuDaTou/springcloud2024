package com.overthinker.cloud.auth.entity.DTO;

import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserUpdateDTO implements BasecopyProperties {
    //用户昵称
    @NotNull
    private String nickname;
    //用户性别
    @NotNull
    private Integer gender;
    //用户头像
    @NotNull
    private String avatar;
    //个人简介
    @NotNull
    private String intro;
}
