package com.overthinker.cloud.auth.service;


import com.overthinker.cloud.auth.entity.ENUMS.EmailCompolent;

public interface EmailService {

    /**
     * 根据邮箱发送验证码
     * @param email 邮箱
     * @param type 邮件模板
     * @return
     */
    void getEmailCode(String email, String type);
}
