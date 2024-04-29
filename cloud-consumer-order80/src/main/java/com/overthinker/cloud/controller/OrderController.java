package com.overthinker.cloud.controller;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.overthinker.cloud.apis.PayFeignApi;
import com.overthinker.cloud.entityDTO.PayDTO;
import com.overthinker.cloud.entityVO.PayVO;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Resource
    private PayFeignApi payFeignApi;

  /**
   * 新增一条支付相关流水记录
   * @param payDTO
   * @return
   */
    @PostMapping("/feign/pay/add")
    public ResultData<String> addPay(@RequestBody PayDTO payDTO) {

    return payFeignApi.addPay(payDTO);
    }


  /**
   * 删除支付流水信息
   * @param id
   * @return
   */
    @DeleteMapping("/feign/pay/delete/{id}")
    public ResultData<Integer> deletePay(@PathVariable("id") Long id) {

    return payFeignApi.deletePay(id);
    }

  /**
   * 修改支付流水信息
   * @param payDTO
   * @return
   */
    @PutMapping("/feign/pay/update")
    public ResultData<Integer> updatePay(@RequestBody PayDTO payDTO) {
        return payFeignApi.updatePay(payDTO);
    }

    @GetMapping("/feign/pay/get/{id}")
    public ResultData<PayVO> getPayInfo(@PathVariable("id") Integer id) {
        return payFeignApi.getPayInfo(id);
    }

    @GetMapping("/feign/pay/getList")
    public ResultData<List<PayVO>> getPayInfoVO() {
        ResultData resultData = null;
        try {
            System.out.println("开始调用远程服务" + DateUtil.now());
            resultData = payFeignApi.listPayInfo();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用远程服务失败" + DateUtil.now());
            ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
        }
        return resultData;
    }

//    @GetMapping("/feign/pay/list")
//    public ResultData list() {
//        return payFeignApi.list();
//    }






}
