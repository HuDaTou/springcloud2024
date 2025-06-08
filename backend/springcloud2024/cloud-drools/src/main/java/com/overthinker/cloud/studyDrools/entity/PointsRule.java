package com.overthinker.cloud.studyDrools.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PointsRule {
    private Long id;
    private String orderType; // 订单类型：CHARGE/SWAP/HYDROGEN
    private Integer baseAmount; // 金额基数（单位：元）
    private Integer points;     // 对应积分
    private Boolean amountEnabled;   // 金额积分是否启用
    private Integer orderPoints;   // 订单积分
    private Boolean orderEnabled;   // 订单积分是否启用
    private LocalDateTime updateTime;
}