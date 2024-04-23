package com.overthinker.cloud.controller;


import cn.hutool.core.bean.BeanUtil;
import com.overthinker.cloud.entity.PayDTO;
import com.overthinker.cloud.entity.TPay;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import com.overthinker.cloud.service.impl.TPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 支付交易表 前端控制器
 *
 *
 * @author overthinker
 * @since 2024-04-21
 */
@RestController
@RequestMapping("/t-pay")
@Log4j2
@RequiredArgsConstructor
public class TPayController {
    private final TPayServiceImpl tPayService;




    @PostMapping(value = "/add")
    public ResultData<String> addPay(@RequestBody PayDTO payDTO){
        TPay tPay = BeanUtil.copyProperties(payDTO, TPay.class);
        tPayService.save(tPay);
        log.info("插入成功"+ tPay.getId());
        return ResultData.success("成功插入记录，返回值："+tPay.getId());

    }

    @DeleteMapping("{id}")
    public ResultData<Integer> deletePay(@PathVariable("id") Long id){
        tPayService.removeById(id);

        return ResultData.success(1);
    }

    @PutMapping(value = "/update")
    public ResultData<Integer> updatePay(@RequestBody PayDTO payDTO){
        TPay tPay = BeanUtil.copyProperties(payDTO, TPay.class);
        tPayService.updateById(tPay);
        return ResultData.success(1);
    }

    @GetMapping("{id}")
    public ResultData<TPay> getPayBid(@PathVariable("id") Long id){
//        如果id为负数抛出异常
        if (id < 0) throw new RuntimeException("id不能为负数");
        TPay tPay = tPayService.getById(id);
        return ResultData.success(tPay);
    }

    @GetMapping("/list")
    public ResultData<List> listPay(){
        return ResultData.success(tPayService.list());
    }




}
