package com.overthinker.cloud.web.entity.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeletePhotoOrAlbumDTO {

    @NotNull(message = "id不能为空")
    private Long id;

    @Min(value = 1, message = "参数错误")
    @Max(value = 2, message = "参数错误")
    private Integer type;

    private Long parentId;
}

