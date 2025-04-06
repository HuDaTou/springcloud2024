package com.overthinker.cloud.web.entity.DTO;

import com.overthinker.cloud.web.entity.BaseData;
import lombok.Data;

@Data
public class SearchVideoDTO implements BaseData {
    // 分类id
    private Long categoryId;
    //视频标题
    private String videoTitle;
    //视频状态 (1公开 2私密）
    private Integer permission;
}
