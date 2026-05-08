package com.overthinker.cloud.web.entity.VO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author overH
 * <p>
 * 创建时间：2024/1/19 21:13
 */
@Data
public class TreeHoleListVO {
    //树洞表id
    private Long id;
    //用户名称
    private String userName;
    //内容
    private String content;
    // 是否通过
    private Integer isCheck;
    //创建时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
