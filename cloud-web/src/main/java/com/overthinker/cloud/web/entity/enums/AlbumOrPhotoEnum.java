package com.overthinker.cloud.web.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlbumOrPhotoEnum {
    ALBUM(1, "相册"),
    PHOTO(2, "照片");

    private final Integer code;
    private final String desc;
}
