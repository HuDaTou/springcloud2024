package com.overthinker.cloud.exceptions;

import com.overthinker.cloud.resp.ReturnCodeEnum;
import lombok.Getter;

@Getter

public class FileUploadException extends RuntimeException
{
  private final ReturnCodeEnum returnCodeEnum;

  private final Object data;

  public FileUploadException(){
    this(ReturnCodeEnum.FILE_UPLOAD_ERROR);
  }

  public FileUploadException(Object data) {
    this.data = data;
    this.returnCodeEnum = ReturnCodeEnum.FILE_UPLOAD_ERROR;
  }

  public FileUploadException(Object data, ReturnCodeEnum returnCodeEnum) {
    this.data = data;
    this.returnCodeEnum = returnCodeEnum;
  }

  public FileUploadException(ReturnCodeEnum returnCodeEnum) {
    this.data = null;
    this.returnCodeEnum = returnCodeEnum;
  }
}
