package com.overthinker.cloud.auth.entity.ENUMS;

import com.overthinker.cloud.common.core.constants.NumberConst;
import com.overthinker.cloud.common.core.exception.BusinessException;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
import com.overthinker.cloud.system.redis.constants.RedisConstants;

/**
 * 邮箱验证码类型
 */
public enum EmailCodeType {



    REGISTER(NumberConst.ZERO, RedisConstants.REGISTER_CODE_KEY_PREFIX),

    RESET_PASSWORD(NumberConst.ONE, RedisConstants.RESET_PASSWORD_CODE_KEY_PREFIX);

    private final String code;
    private final String codeType;

    EmailCodeType(String code,String codeType) {
        this.code = code;
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public String getCodeType() {
        return codeType;
    }

    public static  EmailCodeType getByCode(String code) {
        return java.util.Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ReturnCodeEnum.TYPE_NOT_RECOGNIZED,"未知的验证码类型: " + code));
    }
    

    /**
     * 根据验证码获取验证码类型
     * @param code
     * @return
     */
    public static  String getTypeByCode(String code) {
        return java.util.Arrays.stream(values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .map(EmailCodeType::getCodeType)
                .orElseThrow(() -> new BusinessException(ReturnCodeEnum.TYPE_NOT_RECOGNIZED,"未知的验证码类型: " + code));
        
    }




}
