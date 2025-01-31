//package com.overthinker.cloud.web.component;
//
//import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomIdGenerator implements IdentifierGenerator {
//    @Override
//    public Long nextId(Object entity) {
//        // 使用实体类名作为业务键，或者提取参数生成业务键
//        String bizKey = entity.getClass().getName();
//        // 根据业务键调用分布式ID生成服务
//        long id = ...; // 调用分布式ID生成逻辑
//        // 返回生成的ID值
//        return id;
//    }
//}