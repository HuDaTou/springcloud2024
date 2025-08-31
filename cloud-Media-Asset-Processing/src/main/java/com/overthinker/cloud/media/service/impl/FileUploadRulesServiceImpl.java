package com.overthinker.cloud.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.media.entity.PO.FileUploadRules;
import com.overthinker.cloud.media.service.FileUploadRulesService;
import com.overthinker.cloud.media.mapper.FileUploadRulesMapper;
import org.springframework.stereotype.Service;

/**
* @author overh
* @description 针对表【file_upload_rules(存储文件上传规则，如大小、类型和存储路径)】的数据库操作Service实现
* @createDate 2025-08-02 18:06:05
*/
@Service
public class FileUploadRulesServiceImpl extends ServiceImpl<FileUploadRulesMapper, FileUploadRules>
    implements FileUploadRulesService{

}




