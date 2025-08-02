package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Article)表实体类
 *
 * @author overH
 * @since 2023-10-15 02:38:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("t_article")
public class Article extends BaseData implements BasecopyProperties {

    @Schema(description = "文章ID", example = "123456789012345678")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "作者ID", example = "10001")
    private Long userId;

    @Schema(description = "分类ID", example = "2001")
    private Long categoryId;

    @Schema(description = "文章缩略图", example = "https://example.com/article-cover.jpg")
    private String articleCover;

    @Schema(description = "文章标题", example = "Spring Boot实战教程")
    private String articleTitle;

    @Schema(description = "文章内容", example = "本文将详细介绍Spring Boot的使用...")
    private String articleContent;

    @Schema(description = "文章类型(1原创 2转载 3翻译)", example = "1")
    private Integer articleType;

    @Schema(description = "是否置顶(0否 1是)", example = "0")
    private Integer isTop;

    @Schema(description = "文章状态(1公开 2私密 3草稿)", example = "1")
    private Integer status;

    @Schema(description = "访问量", example = "128")
    private Long visitCount;


    @Schema(description = "是否删除(0：未删除，1：已删除)", example = "0")
    private Integer isDeleted;
}