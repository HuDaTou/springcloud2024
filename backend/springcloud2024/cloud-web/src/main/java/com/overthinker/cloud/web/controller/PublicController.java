package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.annotation.AccessLimit;
import com.overthinker.cloud.web.annotation.LogAnnotation;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.entity.constants.LogConst;
import com.overthinker.cloud.web.service.PublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/16 17:00
 * 公共接口
 */
@RestController
@Tag(name = "公共接口")
@RequestMapping("/public")
@Validated
public class PublicController extends BaseController {

    @Resource
    private PublicService publicService;

    /**
     * 邮件发送
     */
    @Operation(summary = "邮件发送")
    @Parameters({
            @Parameter(name = "email", description = "邮箱", required = true),
            @Parameter(name = "type", description = "邮箱类型", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 1)
    @LogAnnotation(module="邮件发送",operation= LogConst.EMAIL_SEND)
    @GetMapping("/ask-code")
    public ResultData<String> askVerifyCode(
            @RequestParam(name = "email") @Email @NotNull String email,
            @RequestParam(name = "type") @Pattern(regexp = "(register|reset|resetEmail)",message = "邮箱类型错误" ) @NotNull String type
    ) {
        return messageHandler(() -> publicService.registerEmailVerifyCode(type, email));
    }

}
