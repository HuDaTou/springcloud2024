package com.overthinker.cloud.api.apis.auth.api;

import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "cloud-auth", contextId = "emailClient")
public interface EmailClient {

    @PostMapping("/email/send-code")
    ResultData<Void> sendEmailCode(@RequestParam("email") String email, @RequestParam("type") String type);

    @PostMapping("/email/send-notification")
    ResultData<Void> sendEmailNotification(@RequestParam("email") String email,
                                            @RequestParam("type") String type,
                                            @RequestBody(required = false) Map<String, Object> content);
}
