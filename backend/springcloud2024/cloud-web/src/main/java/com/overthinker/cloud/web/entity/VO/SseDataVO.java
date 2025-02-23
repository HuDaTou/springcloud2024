package com.overthinker.cloud.web.entity.VO;

import lombok.Data;

@Data
public class SseDataVO {

//    在线数量
    private int onlineCount;

//    用户数量
    private Long userCount;

//    文章数量
    private Long articleCount;
//    图片数量
    private Long photoCount;
}
