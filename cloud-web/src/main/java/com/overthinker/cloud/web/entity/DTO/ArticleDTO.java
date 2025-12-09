package com.overthinker.cloud.web.entity.DTO;


import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ArticleDTO(
    Long id,

    @NotNull(message = "分类id不能为空")
    Long categoryId,

    @NotNull(message = "标签id不能为空")
    List<Long> tagId,

    @NotNull(message = "文章缩略图不能为空")
    String articleCover,

    @NotNull(message = "文章标题不能为空")
    String articleTitle,

    @NotNull(message = "文章内容不能为空")
    String articleContent,

    @NotNull(message = "文章类型不能为空")
    Integer articleType,

    @NotNull(message = "是否置顶不能为空")
    Integer isTop,

    @NotNull(message = "文章状态不能为空")
    Integer status
) implements BasecopyProperties {}