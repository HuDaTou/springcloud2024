package com.overthinker.cloud.media.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.common.base.BaseController;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.media.entity.DTO.FileUploadRulesInfoDTO;
import com.overthinker.cloud.media.entity.PO.FileUploadRules;
import com.overthinker.cloud.media.service.FileUploadRulesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文件上传规则管理 Controller
 *
 * @author overthinker
 * @since 2025-08-02
 */
@RestController
@RequestMapping("/media/rules")
@Tag(name = "文件上传规则管理", description = "用于管理文件上传的规则，如大小、类型和存储路径")
@RequiredArgsConstructor
public class FileUploadRulesController extends BaseController {

    private final FileUploadRulesService fileUploadRulesService;


    /**
     * 根据ID获取上传规则
     *
     * @param id 规则ID
     * @return 规则实体
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取上传规则", description = "根据ID查询单个文件上传规则")
    public ResultData<FileUploadRules> getRuleById(@PathVariable Long id) {
        FileUploadRules rule = fileUploadRulesService.getById(id);
        return ResultData.success(rule);
    }

    /**
     * 新增上传规则
     *
     * @param fileUploadRuleDto 规则实体DTO
     * @return 是否成功
     */
    @PostMapping
    @Operation(summary = "新增上传规则", description = "创建一条新的文件上传规则")
    public ResultData<Boolean> createRule(@RequestBody FileUploadRulesInfoDTO fileUploadRuleDto) {
        FileUploadRules rule = new FileUploadRules().copyFrom(fileUploadRuleDto);
        // 确保新增操作时ID为null，避免意外更新
        rule.setId(null);
        boolean success = fileUploadRulesService.save(rule);
        return ResultData.success(success);
    }

    /**
     * 根据ID更新上传规则
     *
     * @param id                规则ID
     * @param fileUploadRuleDto 规则实体DTO
     * @return 是否成功
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新上传规则", description = "根据ID更新一条已存在的文件上传规则")
    public ResultData<Boolean> updateRule(@PathVariable Integer id, @RequestBody FileUploadRulesInfoDTO fileUploadRuleDto) {
        FileUploadRules rule = new FileUploadRules().copyFrom(fileUploadRuleDto);
        rule.setId(id); // 从URL路径中设置要更新的规则ID
        boolean success = fileUploadRulesService.updateById(rule);
        return ResultData.success(success);
    }

    /**
     * 根据ID删除上传规则
     *
     * @param id 规则ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除上传规则", description = "根据ID删除一条文件上传规则")
    public ResultData<Boolean> deleteRule(@PathVariable Long id) {
        boolean success = fileUploadRulesService.removeById(id);
        return ResultData.success(success);
    }

    /**
     * 分页列出所有上传规则
     *
     * @param current 当前页
     * @param size 每页数量
     * @return 规则列表（分页）
     */
    @GetMapping("/list")
    @Operation(summary = "分页列出上传规则", description = "分页查询所有文件上传规则")
    public ResultData<IPage<FileUploadRules>> listRules(@RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size) {
        Page<FileUploadRules> page = new Page<>(current, size);
        IPage<FileUploadRules> pageResult = fileUploadRulesService.page(page);
        return ResultData.success(pageResult);
    }
}
