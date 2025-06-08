package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.web.entity.PO.base.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "t_photo")
public class Photo extends BaseData implements BasecopyProperties {
    // 自增id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "相册/照片ID", example = "1234567890123456789")
    private Long id;

    // 创建者id
    @Schema(description = "创建用户ID", example = "10001")
    private Long userId;

    // 名称
    @Schema(description = "相册/照片名称", example = "我的相册")
    private String name;

    // 描述
    @Schema(description = "相册/照片描述", example = "旅行照片")
    private String description;

    // 类型（1：相册 2：照片）
    @Schema(description = "资源类型：1-相册 2-照片", allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    // 父相册id
    @Schema(description = "父相册ID，顶级相册为null", example = "0")
    private Long parentId;

    // 图片地址
    @Schema(description = "照片URL", example = "https://example.com/image.jpg")
    private String url;

    // 是否通过 (0否 1是)
    @Schema(description = "审核状态：0-未通过 1-通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;

    // 照片体积大小(kb)
    @Schema(description = "照片大小(KB)", example = "2048.5")
    private Double size;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}