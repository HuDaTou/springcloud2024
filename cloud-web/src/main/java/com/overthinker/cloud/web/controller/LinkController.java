package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.annotation.CheckBlacklist;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.DTO.LinkDTO;
import com.overthinker.cloud.web.entity.DTO.LinkIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLinkDTO;
import com.overthinker.cloud.web.entity.VO.LinkListVO;
import com.overthinker.cloud.web.entity.VO.LinkVO;
import com.overthinker.cloud.web.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 友链控制器
 * <p>
 * 处理友链的管理接口，包括友链申请、列表查询、审核、删除和邮箱审核等操作
 * </p>
 *
 * @author overH
 * @since 2023-11-14
 */
@Tag(name = "友链相关接口")
@RestController
@Validated
@RequestMapping("link")
@RequiredArgsConstructor
public class LinkController extends BaseController {

    private final LinkService linkService;

    /**
     * 申请友链
     *
     * @param linkDTO 友链申请信息
     * @return 操作结果
     */
    @CheckBlacklist
    @Operation(summary = "申请友链")
    @Parameter(name = "linkDTO", description = "友链申请信息")
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/apply")
    public ResultData<Void> applyLink(@RequestBody @Valid LinkDTO linkDTO) {
        return linkService.applyLink(linkDTO);
    }

    /**
     * 查询所有通过审核的友链
     *
     * @return 友链列表
     */
    @Operation(summary = "查询所有通过申请的友链")
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/list")
    public ResultData<List<LinkVO>> getLinkList() {
        return messageHandler(() -> linkService.getLinkList());
    }

    /**
     * 获取后台友链列表
     *
     * @return 友链列表
     */
    @PreAuthorize("hasAnyAuthority('blog:link:list')")
    @Operation(summary = "后台友链列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResultData<List<LinkListVO>> backList() {
        return messageHandler(() -> linkService.getBackLinkList(null));
    }

    /**
     * 搜索后台友链列表
     *
     * @param searchDTO 搜索条件
     * @return 友链列表
     */
    @PreAuthorize("hasAnyAuthority('blog:link:search')")
    @Operation(summary = "搜索后台友链列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResultData<List<LinkListVO>> backList(@RequestBody SearchLinkDTO searchDTO) {
        return messageHandler(() -> linkService.getBackLinkList(searchDTO));
    }

    /**
     * 修改友链审核状态
     *
     * @param linkIsCheckDTO 审核信息
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:link:isCheck')")
    @Operation(summary = "修改友链是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResultData<Void> isCheck(@RequestBody @Valid LinkIsCheckDTO linkIsCheckDTO) {
        return linkService.isCheckLink(linkIsCheckDTO);
    }

    /**
     * 删除友链
     *
     * @param ids 友链ID列表
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:link:delete')")
    @Operation(summary = "删除友链")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.DELETE)
    @DeleteMapping("/back/delete")
    public ResultData<Void> delete(@RequestBody List<Long> ids) {
        return linkService.deleteLink(ids);
    }

    /**
     * 邮箱审核友链接口
     *
     * @param verifyCode 校验码
     * @param response   响应
     * @return 提示信息
     */
    @Operation(summary = "邮箱审核友链")
    @AccessLimit(seconds = 60, maxCount = 60)
    @LogAnnotation(module = "友链审核", operation = LogConst.APPROVE)
    @Parameter(name = "verifyCode", description = "校验码")
    @GetMapping("/email/apply")
    public String emailApply(@RequestParam("verifyCode") @NotBlank(message = "校验码不能为空") String verifyCode,
                            HttpServletResponse response) {
        return linkService.emailApplyLink(verifyCode, response);
    }
}