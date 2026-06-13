package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "t_photo")
public class Photo extends BaseData implements BasecopyProperties {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "相册/照片ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "创建用户ID", example = "10001")
    private Long userId;

    @Schema(description = "相册/照片名称", example = "我的相册")
    private String name;

    @Schema(description = "相册/照片描述", example = "旅行照片")
    private String description;

    @Schema(description = "资源类型：1-相册 2-照片", allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    @Schema(description = "父相册ID，顶级相册为null", example = "0")
    private Long parentId;

    @Schema(description = "照片URL", example = "https://example.com/image.jpg")
    private String url;

    @Schema(description = "审核状态：0-未通过 1-通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;

    @Schema(description = "照片大小(KB)", example = "2048.5")
    private Double size;
}