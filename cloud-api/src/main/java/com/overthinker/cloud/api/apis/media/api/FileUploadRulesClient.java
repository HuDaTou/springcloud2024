package com.overthinker.cloud.api.apis.media.api;

import com.overthinker.cloud.api.apis.media.DTO.FileUploadRulesRequest;
import com.overthinker.cloud.api.config.FeignClientCredentialsConfig;
import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 文件上传规则 Feign 客户端
 * <p>
 * 提供文件上传规则的增删改查接口，供内部服务调用
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@FeignClient(name = "cloud-media-asset-processing", configuration = FeignClientCredentialsConfig.class)
public interface FileUploadRulesClient {

    /**
     * 根据ID获取上传规则
     *
     * @param id 规则ID
     * @return 规则信息
     */
    @GetMapping("/api/media/rules/{id}")
    ResultData<Map<String, Object>> getRuleById(@PathVariable("id") Long id);

    /**
     * 新增上传规则
     *
     * @param request 规则信息
     * @return 操作结果
     */
    @PostMapping("/api/media/rules")
    ResultData<Void> createRule(@RequestBody FileUploadRulesRequest request);

    /**
     * 更新上传规则
     *
     * @param id      规则ID
     * @param request 规则信息
     * @return 操作结果
     */
    @PutMapping("/api/media/rules/{id}")
    ResultData<Void> updateRule(@PathVariable("id") Long id,
                                @RequestBody FileUploadRulesRequest request);

    /**
     * 删除上传规则
     *
     * @param id 规则ID
     * @return 操作结果
     */
    @DeleteMapping("/api/media/rules/{id}")
    ResultData<Void> deleteRule(@PathVariable("id") Long id);

    /**
     * 分页列出上传规则
     *
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return 规则分页列表
     */
    @GetMapping("/api/media/rules/list")
    ResultData<Map<String, Object>> listRules(
            @RequestParam(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize);
}
