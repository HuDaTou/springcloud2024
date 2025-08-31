package com.overthinker.cloud.studyDrools.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {


    private String orderNo;
    private double amout;
    private double score;
    private String type;


}
