//package com.overthinker.cloud.web;
//
//import com.overthinker.cloud.web.entity.test.CustomFieldBean;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@SpringBootTest
//public class CustomFieldValidatorTest {
//    @Autowired
//    private ProductService productService;
//
//    @Test(expected = ConstraintViolationException.class)
//    public void testInvalid() {
//        CustomFieldBean customFieldBean = new CustomFieldBean();
//        customFieldBean.setIpAddr("1.2.33");
//
//        this.productService.doCustomField(customFieldBean);
//    }
//
//    @Test
//    public void testValid() {
//        CustomFieldBean customFieldBean = new CustomFieldBean();
//        customFieldBean.setIpAddr("1.2.33.123");
//
//        this.productService.doCustomField(customFieldBean);
//    }
//}