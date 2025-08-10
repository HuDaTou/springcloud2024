package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.entity.BasecopyProperties;
import com.overthinker.cloud.common.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Banners)表实体类
 *
 * @author overH
 * @since 2024-08-28 09:51:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_banners")
public class Banners extends BaseData implements BasecopyProperties {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "图片主键ID", example = "1234567890123456789")
    private Long id;

    // 图片路径
    @Schema(description = "图片存储路径", example = "/upload/images/2023/06/12345.jpg")
    private String path;

    // 图片大小 (字节)
    @Schema(description = "图片大小（字节）", example = "204800")
    private Long size;

    // 图片类型 (MIME)
    @Schema(description = "图片MIME类型", example = "image/jpeg")
    private String type;

    // 上传人id
    @Schema(description = "上传用户ID", example = "10001")
    private Long userId;

    // 图片顺序
    @Schema(description = "图片显示顺序", example = "1")
    private Integer sortOrder;
}

