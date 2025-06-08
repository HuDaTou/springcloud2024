package com.overthinker.cloud.web.entity.PO.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class BaseData {

    @Schema(description = "创建时间", example = "2023-01-01 12:00:00")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间", example = "2023-01-02 15:30:00")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
