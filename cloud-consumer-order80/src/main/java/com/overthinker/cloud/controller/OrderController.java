package com.overthinker.cloud.controller;

import com.overthinker.cloud.entity.PayDTO;
import com.overthinker.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    public static final String PaymentSrv_URL = "http://localhost:8001/t-pay/";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO) {
        return restTemplate.postForObject(PaymentSrv_URL + "/t-pay/add", payDTO, ResultData.class);
    }

    @GetMapping("/consumer/pay/{id}")
    public ResultData getOrderById(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(PaymentSrv_URL + id, ResultData.class,id);
    }


}
