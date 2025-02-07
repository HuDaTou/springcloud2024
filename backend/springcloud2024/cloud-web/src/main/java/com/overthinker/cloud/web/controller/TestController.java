package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "测试接口")
@Validated
public class TestController extends BaseController {


    @Operation(summary = "测试接口")
    @RequestMapping("/test")
    public String test() {
        return "web启动成功" ;
    }

    @Operation(summary = "测试securityUtils")
    @RequestMapping("/test2")
    public ResultData<Long> test2() {
        return messageHandler(SecurityUtils::getUserId);
    }

}
