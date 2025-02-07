package com.overthinker.cloud.web.validators;


// 自定义校验器类

import com.overthinker.cloud.web.annotation.FileFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileFormatValidator implements ConstraintValidator<FileFormat, MultipartFile> {

    private List<String> allowedExtensions;

    @Override
    public void initialize(FileFormat constraintAnnotation) {
        // 初始化方法，获取注解中的允许扩展名列表
        this.allowedExtensions = Arrays.asList(constraintAnnotation.allowedExtensions());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        int lastIndex = originalFilename.lastIndexOf('.');
        if (lastIndex == -1) {
            return false;
        }
        String extension = originalFilename.substring(lastIndex + 1).toLowerCase();
        // 执行校验逻辑，判断文件扩展名是否在允许列表中
        return allowedExtensions.contains(extension);
    }
}