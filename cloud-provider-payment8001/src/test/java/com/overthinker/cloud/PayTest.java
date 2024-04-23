package com.overthinker.cloud;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.overthinker.cloud.entity.TPay;
import com.overthinker.cloud.mapper.TPayMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
public class PayTest {

    @Resource
    private TPayMapper tPayMapper;

    @Test
    public void selectById() {
        QueryWrapper<TPay> wrapper = new QueryWrapper<TPay>()
                .select("id", "pay_no", "order_no", "user_id", "amount", "deleted")
                .like("order_no", "r");
        List<TPay> tPays = tPayMapper.selectList(wrapper);

        tPays.forEach(System.out::println);
    }

    @Test
    public void updateById() {
        TPay tPay = new TPay();
        BigDecimal bigDecimal = new BigDecimal("1000");
        tPay.setAmount(bigDecimal);
        QueryWrapper<TPay> wrapper = new QueryWrapper<TPay>()
                .eq("id", 1);
        tPayMapper.update(tPay, wrapper);

    }
//    使用lamdba表达式
    @Test
    public void lambdaSelect() {


        LambdaQueryWrapper<TPay> tPayLambdaQueryWrapper = new LambdaQueryWrapper<TPay>()
                .select(TPay::getId, TPay::getPayNo, TPay::getOrderNo, TPay::getUserId, TPay::getAmount, TPay::getDeleted)
                .like(TPay::getOrderNo, "r");
        List<TPay> tPays = tPayMapper.selectList(tPayLambdaQueryWrapper);
    }
}