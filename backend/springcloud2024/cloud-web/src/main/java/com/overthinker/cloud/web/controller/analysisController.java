package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "统计相关接口")
@RequestMapping("/analysis")
@Validated
public class analysisController extends BaseController {


}
