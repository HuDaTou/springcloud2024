package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.annotation.AccessLimit;
import com.overthinker.cloud.web.annotation.CheckBlacklist;
import com.overthinker.cloud.web.annotation.LogAnnotation;
import com.overthinker.cloud.controller.base.BaseController;
import com.overthinker.cloud.web.entity.DTO.LinkDTO;
import com.overthinker.cloud.web.entity.DTO.LinkIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLinkDTO;
import com.overthinker.cloud.web.entity.VO.LinkListVO;
import com.overthinker.cloud.web.entity.VO.LinkVO;
import com.overthinker.cloud.web.entity.constants.LogConst;
import com.overthinker.cloud.web.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Link)表控制层
 *
 * @author overH
 * @since 2023-11-14 08:48:32
 */
@Tag(name = "友链相关接口")
@RestController
@Validated
@RequestMapping("link")
public class LinkController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private LinkService linkService;

    @CheckBlacklist
    @Operation(summary = "申请友链")
    @Parameter(name = "linkDTO", description = "友链申请信息")
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/apply")
    public ResultData<Void> applyLink(@RequestBody @Valid LinkDTO linkDTO) {
        return linkService.applyLink(linkDTO);
    }

    @Operation(summary = "查询所有通过申请的友链")
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/list")
    public ResultData<List<LinkVO>> getLinkList() {
        return messageHandler(() -> linkService.getLinkList());
    }

    @PreAuthorize("hasAnyAuthority('blog:link:list')")
    @Operation(summary = "后台友链列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResultData<List<LinkListVO>> backList() {
        return messageHandler(() -> linkService.getBackLinkList(null));
    }

    @PreAuthorize("hasAnyAuthority('blog:link:search')")
    @Operation(summary = "搜索后台友链列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResultData<List<LinkListVO>> backList(@RequestBody SearchLinkDTO searchDTO) {
        return messageHandler(() -> linkService.getBackLinkList(searchDTO));
    }

    @PreAuthorize("hasAnyAuthority('blog:link:isCheck')")
    @Operation(summary = "修改友链是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResultData<Void> isCheck(@RequestBody @Valid LinkIsCheckDTO linkIsCheckDTO) {
        return linkService.isCheckLink(linkIsCheckDTO);
    }

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
     * @param verifyCode 校验码
     * @param response 响应
     * @return 提示信息
     */
    @Operation(summary = "邮箱审核友链")
    @AccessLimit(seconds = 60, maxCount = 60)
    @LogAnnotation(module = "友链审核", operation = LogConst.APPROVE)
    @Parameter(name = "verifyCode", description = "校验码")
    @GetMapping("/email/apply")
    public String emailApply(@RequestParam("verifyCode") @NotBlank(message = "校验码不能为空") String verifyCode, HttpServletResponse response) {
        return linkService.emailApplyLink(verifyCode, response);
    }
}

