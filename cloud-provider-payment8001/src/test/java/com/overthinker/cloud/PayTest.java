package com.overthinker.cloud;

import com.overthinker.cloud.mapper.TPayMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;



public class PayTest {

    private TPayMapper tPayMapper;
        public void selectById() {
            tPayMapper.selectById(1);
        }
}
