package com.overthinker.cloud.auth.entity.ENUMS;

import com.overthinker.cloud.auth.entity.DTO.MailDTO;
import com.overthinker.cloud.common.core.constants.NumberConst;
import com.overthinker.cloud.common.core.exception.BusinessException;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件枚举
 * 融合了 EmailCompolent 和 EmailCodeType 的功能
 */
@Getter
@AllArgsConstructor
public enum EmailEnum {

    /**
     * 注册验证码邮件
     */
    REGISTER(
            NumberConst.ZERO,
            RedisConstants.REGISTER_CODE_KEY_PREFIX,
            "register",
            "register-email-template"
    ) {
        @Override
        public MailDTO send(String userEmail, String code) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("code", code);
            vars.put("expirationTime", NumberConst.FIVE + "分钟");
            vars.put("toUrl", "https://overthinker.top");
            vars.put("openSourceAddress", "https://github.com/your-project");
            return new MailDTO()
                    .setVariables(vars)
                    .setSubject("注册验证码")
                    .setTemplateName(getTemplateName())
                    .setTo(userEmail);
        }
    },

    /**
     * 密码重置邮件
     */
    RESET_PASSWORD(
            NumberConst.ONE,
            RedisConstants.RESET_PASSWORD_CODE_KEY_PREFIX,
            "reset_password",
            "reset-password-template"
    ) {
        @Override
        public MailDTO send(String userEmail, String code) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("code", code);
            vars.put("expirationTime", NumberConst.FIVE + "分钟");
            vars.put("toUrl", "https://overthinker.top/login");
            vars.put("openSourceAddress", "https://github.com/your-repo");
            return new MailDTO()
                    .setTemplateName(getTemplateName())
                    .setTo(userEmail)
                    .setSubject("重置密码")
                    .setVariables(vars);
        }
    };

    private final String code;
    private final String codeType;
    private final String type;
    private final String templateName;

    public abstract MailDTO send(String userEmail, String code);

    public static EmailEnum getByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ReturnCodeEnum.TYPE_NOT_RECOGNIZED, "未知的验证码类型: " + code));
    }

    public static String getTypeByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .map(EmailEnum::getCodeType)
                .orElseThrow(() -> new BusinessException(ReturnCodeEnum.TYPE_NOT_RECOGNIZED, "未知的验证码类型: " + code));
    }

    public static EmailEnum getByType(String type) {
        return Arrays.stream(values())
                .filter(item -> item.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的发送类型: " + type));
    }
}
