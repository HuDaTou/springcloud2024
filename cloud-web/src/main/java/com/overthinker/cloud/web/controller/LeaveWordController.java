package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.annotation.CheckBlacklist;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.DTO.LeaveWordIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLeaveWordDTO;
import com.overthinker.cloud.web.entity.VO.LeaveWordListVO;
import com.overthinker.cloud.web.entity.VO.LeaveWordVO;
import com.overthinker.cloud.web.service.LeaveWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 留言控制器
 * <p>
 * 处理留言的管理接口，包括留言列表查询、用户留言、审核和删除等操作
 * </p>
 *
 * @author overH
 * @since 2023-11-03
 */
@RestController
@RequestMapping("leaveWord")
@Validated
@Tag(name = "留言板", description = "留言板相关接口")
@RequiredArgsConstructor
public class LeaveWordController extends BaseController {

    private final LeaveWordService leaveWordService;

    /**
     * 获取留言板列表
     *
     * @param id 留言板ID（可选）
     * @return 留言列表
     */
    @Operation(summary = "获取留言板列表")
    @Parameters({
            @Parameter(name = "id", description = "留言板id", in = ParameterIn.QUERY)
    })
    @GetMapping("/list")
    @AccessLimit(seconds = 60, maxCount = 10)
    public ResultData<List<LeaveWordVO>> list(@RequestParam(value = "id", required = false) String id) {
        return messageHandler(() -> leaveWordService.getLeaveWordList(id));
    }

    /**
     * 用户留言
     *
     * @param content 留言内容
     * @return 操作结果
     */
    @CheckBlacklist
    @Operation(summary = "用户留言")
    @PostMapping("/auth/userLeaveWord")
    @AccessLimit(seconds = 60, maxCount = 10)
    public ResultData<Void> userLeaveWord(@RequestBody @NotNull String content) {
        return leaveWordService.userLeaveWord(content);
    }

    /**
     * 获取后台留言列表
     *
     * @return 留言列表
     */
    @PreAuthorize("hasAnyAuthority('blog:leaveword:list')")
    @Operation(summary = "后台留言列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "留言管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResultData<List<LeaveWordListVO>> backList() {
        return messageHandler(() -> leaveWordService.getBackLeaveWordList(null));
    }

    /**
     * 搜索后台留言列表
     *
     * @param searchDTO 搜索条件
     * @return 留言列表
     */
    @PreAuthorize("hasAnyAuthority('blog:leaveword:search')")
    @Operation(summary = "搜索后台留言列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "留言管理", operation = LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResultData<List<LeaveWordListVO>> backList(@RequestBody SearchLeaveWordDTO searchDTO) {
        return messageHandler(() -> leaveWordService.getBackLeaveWordList(searchDTO));
    }

    /**
     * 修改留言审核状态
     *
     * @param leaveWordIsCheckDTO 审核信息
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:leaveword:isCheck')")
    @Operation(summary = "修改留言是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "留言管理", operation = LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResultData<Void> isCheck(@RequestBody @Valid LeaveWordIsCheckDTO leaveWordIsCheckDTO) {
        return leaveWordService.isCheckLeaveWord(leaveWordIsCheckDTO);
    }

    /**
     * 删除留言
     *
     * @param ids 留言ID列表
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:leaveword:delete')")
    @Operation(summary = "删除留言")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "留言管理", operation = LogConst.DELETE)
    @DeleteMapping("/back/delete")
    public ResultData<Void> delete(@RequestBody List<Long> ids) {
        return leaveWordService.deleteLeaveWord(ids);
    }
}