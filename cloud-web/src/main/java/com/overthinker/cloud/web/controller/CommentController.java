package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.api.apis.auth.dto.UserCommentDTO;
import com.overthinker.cloud.common.core.annotation.CheckBlacklist;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.DTO.CommentIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCommentDTO;
import com.overthinker.cloud.web.entity.VO.ArticleCommentVO;
import com.overthinker.cloud.web.entity.VO.CommentListVO;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器
 * <p>
 * 处理评论的管理接口，包括评论列表查询、用户评论、审核和删除等操作
 * </p>
 *
 * @author overH
 * @since 2023-11-03
 */
@RestController
@Tag(name = "评论相关接口")
@RequestMapping("/comment")
@Validated
@RequiredArgsConstructor
public class CommentController extends BaseController {

    private final CommentService commentService;

    /**
     * 获取评论列表
     *
     * @param type     评论类型
     * @param typeId   评论关联ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页评论列表
     */
    @Operation(summary = "获取评论")
    @Parameters({
            @Parameter(name = "type", description = "评论类型", required = true),
            @Parameter(name = "typeId", description = "评论id", required = true),
            @Parameter(name = "pageNum", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页数量", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/getComment")
    public ResultData<PageVO<List<ArticleCommentVO>>> comment(
            @Valid @NotNull @RequestParam("type") Integer type,
            @Valid @NotNull @RequestParam("typeId") Integer typeId,
            @Valid @NotNull @RequestParam("pageNum") Integer pageNum,
            @Valid @NotNull @RequestParam("pageSize") Integer pageSize
    ) {
        return messageHandler(() -> commentService.getComment(type, typeId, pageNum, pageSize));
    }

    /**
     * 用户添加评论
     *
     * @param commentDTO 评论信息
     * @return 操作结果
     */
    @CheckBlacklist
    @Operation(summary = "用户添加评论")
    @Parameter(name = "commentDTO", description = "评论信息", required = true)
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/add/comment")
    public ResultData<String> userComment(@Valid @RequestBody UserCommentDTO commentDTO) {
        return commentService.userComment(commentDTO);
    }

    /**
     * 获取后台评论列表
     *
     * @return 评论列表
     */
    @PreAuthorize("hasAnyAuthority('blog:comment:list')")
    @Operation(summary = "后台评论列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "评论管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResultData<List<CommentListVO>> backList() {
        return messageHandler(() -> commentService.getBackCommentList(null));
    }

    /**
     * 搜索后台评论列表
     *
     * @param searchDTO 搜索条件
     * @return 评论列表
     */
    @PreAuthorize("hasAnyAuthority('blog:comment:search')")
    @Operation(summary = "搜索后台评论列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "评论管理", operation = LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResultData<List<CommentListVO>> backList(@RequestBody SearchCommentDTO searchDTO) {
        return messageHandler(() -> commentService.getBackCommentList(searchDTO));
    }

    /**
     * 修改评论审核状态
     *
     * @param commentIsCheckDTO 审核信息
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:comment:isCheck')")
    @Operation(summary = "修改评论是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "评论管理", operation = LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResultData<Void> isCheck(@RequestBody @Valid CommentIsCheckDTO commentIsCheckDTO) {
        return commentService.isCheckComment(commentIsCheckDTO);
    }

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:comment:delete')")
    @Operation(summary = "删除评论")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "评论管理", operation = LogConst.DELETE)
    @DeleteMapping("/back/delete/{id}")
    public ResultData<Void> delete(@PathVariable("id") Long id) {
        return commentService.deleteComment(id);
    }
}