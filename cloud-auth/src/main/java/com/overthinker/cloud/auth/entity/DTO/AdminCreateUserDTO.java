package com.overthinker.cloud.auth.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Schema(description = "管理员创建用户DTO")
public class AdminCreateUserDTO {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Length(min = 6, max = 20)
    private String password;

    @Schema(description = "用户昵称")
    @Length(max = 20)
    private String nickname;

    @Schema(description = "邮箱")
    @Email
    private String email;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "是否禁用", defaultValue = "false")
    private Boolean  isDisable = false;

}
