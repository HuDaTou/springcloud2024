package com.overthinker.cloud.api.apis;

import com.overthinker.cloud.entity.DTO.PayDTO;
import com.overthinker.cloud.entity.VO.PayVO;
import com.overthinker.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "cloud-payment-service")
//@RequestMapping("/t-pay")
public interface PayFeignApi {
    /**
     * 新增一条支付相关流水记录
     * @param payDTO
     * @return
     */
    @PostMapping("/t-pay/add")
    ResultData<String> addPay(@RequestBody PayDTO payDTO);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/t-pay/{id}")
    ResultData<Integer> deletePay(@PathVariable("id") Long id);


    /**
     * 修改支付流水信息
     * @param payDTO
     * @return
     */
    @PutMapping("/t-pay/update")
    ResultData<Integer> updatePay(@RequestBody PayDTO payDTO);

    /**
     * 按照主键记录查询支付流水信息
     * @param id
     * @return
     */
    @GetMapping("/t-pay/{id}")
    ResultData<PayVO> getPayInfo(@PathVariable("id") Integer id);


    /**
     * 按照主键记录批量查询支付流水信息
     * @param ids
     * @return
     */
    @GetMapping("/t-pay")
    ResultData<List<PayVO>> getPayInfoByDTO(@RequestParam("ids") List<Long> ids);

    /**
     * 查询所有支付流水信息
     * @return
     */
    @GetMapping("/t-pay/list")
    ResultData<List<PayVO>> listPayInfo();



    /**
     * 服务熔断降级接口测试
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/circuit/{id}")
    String myCircuit(@PathVariable("id") Integer id);


    /**
     * Resilience4j Bulkhead 的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/bulkhead/{id}")
    public String myBulkhead(@PathVariable("id") Integer id);

    /**
     * Resilience4j Ratelimit 的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/ratelimit/{id}")
    public String myRatelimit(@PathVariable("id") Integer id);

    /**
     * Micrometer(Sleuth)进行链路监控的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/micrometer/{id}")
    public String myMicrometer(@PathVariable("id") Integer id);



}
