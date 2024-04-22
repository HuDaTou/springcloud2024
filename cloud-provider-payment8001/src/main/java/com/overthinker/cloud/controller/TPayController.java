package com.overthinker.cloud.controller;


import cn.hutool.core.bean.BeanUtil;
import com.overthinker.cloud.entity.PayDTO;
import com.overthinker.cloud.entity.TPay;
import com.overthinker.cloud.service.impl.TPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 支付交易表 前端控制器
 * </p>
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
    public String addPay(@RequestBody PayDTO payDTO){
        TPay tPay = BeanUtil.copyProperties(payDTO, TPay.class);
        tPayService.save(tPay);
        log.info("插入成功"+ tPay.getId());
        return "success 插入成功"+ tPay.getId();

    }

    @DeleteMapping("{id}")
    public String deletePay(@PathVariable("id") Long id){
        tPayService.removeById(id);
        return "success";
    }

    @PutMapping(value = "/update")
    public String updatePay(@RequestBody PayDTO payDTO){
        TPay tPay = BeanUtil.copyProperties(payDTO, TPay.class);
        tPayService.updateById(tPay);
        return "success";
    }

    @GetMapping("{id}")
    public TPay getPayBid(@PathVariable("id") Long id){
        return tPayService.getById(id);
    }

    @GetMapping("/list")
    public String listPay(){
        return tPayService.list().toString();
    }




}
