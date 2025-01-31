package com.overthinker.cloud.web.exception;

import com.overthinker.cloud.resp.ReturnCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

/**
 * @Title: BusinessException
 * @Author overthinker
 * @Package com.overthinker.cloud.web.exception
 * @Date 2024/11/25 23:00
 * @description: 业务异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    @Schema(description = "用户提示", example = "操作成功！")
    private final String userMessage;
    /**
     * 错误码<br>
     * 调用成功时，为 null。<br>
     * 示例：10001
     */
    @Schema(description = "错误码")
    private final Integer errorCode;

    /**
     * 错误信息<br>
     * 调用成功时，为 null。<br>
     * 示例："验证码无效"
     */
    @Schema(description = "错误信息")
    private final String errorMessage;

    @Schema(description = "http状态码")
    private final HttpStatusCode httpStatusCode;






    public BusinessException(ReturnCodeEnum ReturnCodeEnum, HttpStatusCode httpStatusCode) {
        super(String.format("错误码：[%s]，错误信息：[%s]，用户提示：[%s]", ReturnCodeEnum.name(), ReturnCodeEnum.getMessage(), ReturnCodeEnum.getMessage()));
        this.userMessage = null;
        this.errorCode = ReturnCodeEnum.getCode();
        this.errorMessage = ReturnCodeEnum.getMessage();

        this.httpStatusCode = httpStatusCode;
    }


    public BusinessException(String userMessage, Integer errorCode, String errorMessage, HttpStatusCode httpStatusCode) {
        super(String.format("错误码：[%s]，错误信息：[%s]，用户提示：[%s]", errorCode, errorMessage, userMessage));
        this.userMessage = userMessage;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;

        this.httpStatusCode = httpStatusCode;
    }


}
