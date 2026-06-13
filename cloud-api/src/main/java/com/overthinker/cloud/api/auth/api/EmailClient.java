package com.overthinker.cloud.api.auth.api;

import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "cloud-auth")
public interface EmailClient {

    @PostMapping("/internal/api/email/send-code")
    ResultData<Void> sendEmailCode(@RequestParam String email, @RequestParam String type);

    @PostMapping("/internal/api/email/send-notification")
    ResultData<Void> sendEmailNotification(@RequestParam String email,
                                            @RequestParam String type,
                                            @RequestBody(required = false) Map<String, Object> content);
}
