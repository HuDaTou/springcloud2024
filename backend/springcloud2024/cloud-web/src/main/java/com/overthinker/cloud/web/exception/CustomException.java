package com.overthinker.cloud.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

/**
 * @Title: CustomException
 * @Author overthinker
 * @Package com.overthinker.cloud.web.exception
 * @Date 2024/12/4 01:15
 * @description: 测试异常类
 */
@Getter
public class CustomException extends RuntimeException {

  private final HttpStatusCode statusCode;

  public CustomException(String message, HttpStatusCode statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

}
