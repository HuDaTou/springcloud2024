package com.overthinker.cloud.web.entity.ip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlackListIpInfo {
    // 黑名单的ip
    private String createIp;
    //添加时的ip详情
    private IpDetail ipDetail;
}