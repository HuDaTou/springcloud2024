package com.overthinker.cloud.controller;


import cn.hutool.core.bean.BeanUtil;
import com.overthinker.cloud.entity.TPay;
import com.overthinker.cloud.entityDTO.PayDTO;
import com.overthinker.cloud.entityVO.PayVO;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.service.impl.TPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

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


    /**
     * 新增一条支付相关流水记录
     * @param payDTO
     * @return
     */
    @PostMapping(value = "/add")
    public ResultData<String> addPay(@RequestBody PayDTO payDTO) {
        TPay tPay = BeanUtil.copyProperties(payDTO, TPay.class);
        tPayService.save(tPay);
        log.info("插入成功" + tPay.getId());
        return ResultData.success("成功插入记录，返回值：" + tPay.getId());

    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResultData<Integer> deletePay(@PathVariable("id") Long id) {
        tPayService.removeById(id);

        return ResultData.success(1);
    }

    /**
     * 修改
     *
     * @param payDTO
     * @return
     */
    @PutMapping(value = "/update")
    public ResultData<Integer> updatePay(@RequestBody PayDTO payDTO) {
        TPay tPay = BeanUtil.copyProperties(payDTO, TPay.class);
        tPayService.updateById(tPay);
        return ResultData.success(1);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResultData<PayVO> getPayBid(@PathVariable("id") Long id) {
//        如果id为负数抛出异常
        if (id < 0) throw new RuntimeException("id不能为负数");
        TPay tPay = tPayService.getById(id);
        PayVO payVO = BeanUtil.copyProperties(tPay, PayVO.class);
        return ResultData.success(payVO);
    }

    /**
     * 根据ids批量查询
     * @param ids
     * @return
     */
    @GetMapping
    public ResultData<List<PayVO>> getPay(@RequestParam("ids") List<Long> ids) {
        List<TPay> tPay = tPayService.listByIds(ids);
        List<PayVO> payVOS = BeanUtil.copyToList(tPay, PayVO.class);
        return ResultData.success(payVOS);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/list")
    public ResultData<List<PayVO>> listPay() {
//        暂停63秒钟线程，故意写bug，测试出feign的默认调用超时时间
        try {
            TimeUnit.SECONDS.sleep(63);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        查询全部Pay
        List<TPay> list = tPayService.list();
//        Pay转换为PayVO
        List<PayVO> payVOS = BeanUtil.copyToList(list, PayVO.class);
        return ResultData.success(payVOS);
    }



    @Value("${server.port}")
    private String port;

    @GetMapping("/getPort")
    public String getPort(@Value("${user.overthinker}") String user) {

            return "当前端口号：" + port + "  user:" + user;
    }
}
