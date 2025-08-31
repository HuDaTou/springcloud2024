package com.overthinker.cloud.web;

import com.overthinker.cloud.web.utils.VideoUploadUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilsTest {

    @Resource
    private VideoUploadUtils videoUploadUtils;

    @Test
    public void test() {
        System.out.println("test");
    }

    /**
     * 测试VideoUploadUtils的单位转换方法
     * convertVideoSize
     */
    @Test
    public void testConvertVideoSize() {
        String s = videoUploadUtils.convertVideoSize(1231324231L);
        System.out.println(s);
    }
}
