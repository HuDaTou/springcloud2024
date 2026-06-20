package com.overthinker.cloud.media.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.media.entity.DTO.FileUploadRulesInfoDTO;
import com.overthinker.cloud.media.entity.PO.FileUploadRules;
import com.overthinker.cloud.media.entity.VO.FileUploadRulesVO;
import com.overthinker.cloud.media.service.FileUploadRulesService;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.overthinker.cloud.common.core.resp.PageParams;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文件上传规则管理控制器
 * <p>
 * 用于管理文件上传的规则，如大小、类型和存储路径
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/media/rules")
@Tag(name = "文件上传规则管理", description = "用于管理文件上传的规则，如大小、类型和存储路径")
@RequiredArgsConstructor
public class FileUploadRulesController extends BaseController {

    private final FileUploadRulesService fileUploadRulesService;

    @Operation(summary = "获取上传规则", description = "根据ID查询单个文件上传规则")
    @GetMapping("/{id}")
    @AccessLimit(seconds = 60, maxCount = 30, msg = "查询操作过于频繁，请稍后再试")
    public ResultData<FileUploadRulesVO> getRuleById(
            @Parameter(description = "规则ID", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @NotNull(message = "规则ID不能为空") Long id) {
        FileUploadRules rule = fileUploadRulesService.getById(id);
        FileUploadRulesVO vo = rule.copyProperties(FileUploadRulesVO.class);
        return ResultData.success(vo);
    }

    @Operation(summary = "新增上传规则", description = "创建一条新的文件上传规则")
    @PostMapping
    @AccessLimit(seconds = 60, maxCount = 10, msg = "新增操作过于频繁，请稍后再试")
    @LogAnnotation(module = "文件上传规则", operation = LogConst.INSERT)
    public ResultData<Void> createRule(
            @RequestBody @Valid @Parameter(description = "规则信息", required = true) 
            FileUploadRulesInfoDTO fileUploadRuleDto) {
        FileUploadRules rule = new FileUploadRules().copyFrom(fileUploadRuleDto);
        rule.setId(null);
        fileUploadRulesService.save(rule);
        log.info("新增文件上传规则成功，规则名称：{}", rule.getRuleName());
        return ResultData.success();
    }

    @Operation(summary = "更新上传规则", description = "根据ID更新一条已存在的文件上传规则")
    @PutMapping("/{id}")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "更新操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:rule:edit')")
    @LogAnnotation(module = "文件上传规则", operation = LogConst.UPDATE)
    public ResultData<Void> updateRule(
            @Parameter(description = "规则ID", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @NotNull(message = "规则ID不能为空") Long id,
            @RequestBody @Valid @Parameter(description = "规则信息", required = true) 
            FileUploadRulesInfoDTO fileUploadRuleDto) {
        FileUploadRules rule = new FileUploadRules().copyFrom(fileUploadRuleDto);
        rule.setId(id);
        fileUploadRulesService.updateById(rule);
        log.info("更新文件上传规则成功，规则ID：{}", id);
        return ResultData.success();
    }

    @Operation(summary = "删除上传规则", description = "根据ID删除一条文件上传规则")
    @DeleteMapping("/{id}")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "删除操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:rule:delete')")
    @LogAnnotation(module = "文件上传规则", operation = LogConst.DELETE)
    public ResultData<Void> deleteRule(
            @Parameter(description = "规则ID", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @NotNull(message = "规则ID不能为空") Long id) {
        fileUploadRulesService.removeById(id);
        log.info("删除文件上传规则成功，规则ID：{}", id);
        return ResultData.success();
    }

    @Operation(summary = "分页列出上传规则", description = "分页查询所有文件上传规则")
    @GetMapping("/list")
    @AccessLimit(seconds = 60, maxCount = 30, msg = "查询操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:rule:list')")
    public ResultData<IPage<FileUploadRulesVO>> listRules(
            @Parameter(description = "分页参数") @ModelAttribute PageParams pageParams) {
        Page<FileUploadRules> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        IPage<FileUploadRules> pageResult = fileUploadRulesService.page(page);
        IPage<FileUploadRulesVO> voPage = pageResult.convert(rule -> rule.copyProperties(FileUploadRulesVO.class));
        return ResultData.success(voPage);
    }
}
