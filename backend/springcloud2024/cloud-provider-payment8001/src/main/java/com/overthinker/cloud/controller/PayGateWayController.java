package com.overthinker.cloud.controller;


import cn.hutool.core.util.IdUtil;
import com.overthinker.cloud.entity.TPay;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.service.ITPayService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayGateWayController {
    @Resource
    ITPayService payService;

    @GetMapping(value = "/pay/gateway/{id}")
    public ResultData<TPay> getById(@PathVariable("id") Integer id) {
        TPay tPay = payService.getById(id);
        return ResultData.success(tPay);
    }

    public ResultData<String> getGatewayInfo() {
        return ResultData.success("gateway info" + IdUtil.fastSimpleUUID());
    }
}
