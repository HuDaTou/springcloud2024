package com.overthinker.cloud.studyDrools.test;

import com.overthinker.cloud.studyDrools.entity.Order;
import com.overthinker.cloud.studyDrools.entity.PointsRule;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DroolsDemosApplicationTests {

    @Autowired
    private KieContainer kieContainer;

    @Test
    public void test(){
        //从Kie容器对象中获取会话对象
        KieSession session = kieContainer.newKieSession();

        //Fact对象，事实对象
        Order order = Order.builder()
                .orderNo("1234567890")
                .amout(1000)
                .score(0)
                .type("充电")
                .build();
        PointsRule pointsRule = PointsRule.builder()
                .orderType("加氢")
                .baseAmount(10)
                .points(20)
                .amountEnabled(true)
                .orderPoints(100)
                .orderEnabled(true)
                .build();
        PointsRule pointsRule2 = PointsRule.builder()
                .orderType("换电")
                .baseAmount(20)
                .points(10)
                .amountEnabled(true)
                .orderPoints(100)
                .orderEnabled(true)
                .build();
        PointsRule pointsRule3 = PointsRule.builder()
                .orderType("充电")
                .baseAmount(1)
                .points(2)
                .amountEnabled(true)
                .orderPoints(100)
                .orderEnabled(true)
                .build();
        session.insert(pointsRule);
        session.insert(pointsRule2);
        session.insert(pointsRule3);

        //将Order对象插入到工作内存中
        session.insert(order);
//        session.insert(pointsRule);

        //激活规则，由Drools框架自动进行规则匹配，如果规则匹配成功，则执行当前规则
        session.fireAllRules();
        //关闭会话
        session.dispose();

        System.out.println("订单金额：" + order.getAmout() +
                "，添加积分：" + order.getScore());
    }

    @Test
    public void test2() {
        // 从Kie容器对象中获取会话对象
        KieSession session = kieContainer.newKieSession();

        // Fact对象，积分规则事实对象
        PointsRule pointsRule = PointsRule.builder()
                .orderType("加氢")
                .baseAmount(10)
                .points(20)
                .amountEnabled(true)
                .orderPoints(100)
                .orderEnabled(true)
                .build();
        PointsRule pointsRule2 = PointsRule.builder()
                .orderType("换电")
                .baseAmount(20)
                .points(10)
                .amountEnabled(true)
                .orderPoints(100)
                .orderEnabled(true)
                .build();
        PointsRule pointsRule3 = PointsRule.builder()
                .orderType("充电")
                .baseAmount(1)
                .points(2)
                .amountEnabled(true)
                .orderPoints(100)
                .orderEnabled(true)
                .build();
        session.insert(pointsRule);
        session.insert(pointsRule2);
        session.insert(pointsRule3);

        // 第一个订单
        Order order1 = Order.builder()
                .orderNo("1")
                .amout(1000)
                .score(0)
                .type("充电")
                .build();
        // 第二个订单
        Order order2 = Order.builder()
                .orderNo("12")
                .amout(2000)
                .score(0)
                .type("换电")
                .build();

        // 第一个订单
        Order order3 = Order.builder()
                .orderNo("123")
                .amout(1000)
                .score(0)
                .type("充电")
                .build();
        // 第二个订单
        Order order4 = Order.builder()
                .orderNo("1234")
                .amout(2000)
                .score(0)
                .type("换电")
                .build();

        // 将Order对象插入到工作内存中
        session.insert(order1);
        session.insert(order2);

        // 激活规则，由Drools框架自动进行规则匹配，如果规则匹配成功，则执行当前规则
        session.fireAllRules();

        System.out.println("订单1金额：" + order1.getAmout() + "，添加积分：" + order1.getScore());
        System.out.println("订单2金额：" + order2.getAmout() + "，添加积分：" + order2.getScore());

        // 将Order对象插入到工作内存中
        session.insert(order1);
        session.insert(order2);

        // 激活规则，由Drools框架自动进行规则匹配，如果规则匹配成功，则执行当前规则
        session.fireAllRules();
        // 关闭会话
//        session.dispose();

        System.out.println("订单1金额：" + order1.getAmout() + "，添加积分：" + order1.getScore());
        System.out.println("订单2金额：" + order2.getAmout() + "，添加积分：" + order2.getScore());
    }

}