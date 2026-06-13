package com.overthinker.cloud.auth.config;

import com.overthinker.cloud.auth.constants.BlackListConst;
import com.overthinker.cloud.auth.entity.PO.BlackList;
import com.overthinker.cloud.auth.mapper.BlackListMapper;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BlackListInitConfig {

    private final BlackListMapper blackListMapper;
    private final MyRedisCache myRedisCache;

    @PostConstruct
    public void initBlackListCache() {
        log.info("--------开始初始化黑名单缓存--------");
        try {
            List<BlackList> blackLists = blackListMapper.selectList(null);
            if (!blackLists.isEmpty()) {
                blackLists.forEach(blackList -> {
                    if (blackList.getUserId() != null && blackList.getExpiresTime() != null) {
                        myRedisCache.setCacheMapValue(BlackListConst.BLACK_LIST_UID_KEY, 
                                blackList.getUserId().toString(), blackList.getExpiresTime());
                    } else if (blackList.getIpInfo() != null && blackList.getIpInfo().getCreateIp() != null 
                            && blackList.getExpiresTime() != null) {
                        myRedisCache.setCacheMapValue(BlackListConst.BLACK_LIST_IP_KEY, 
                                blackList.getIpInfo().getCreateIp(), blackList.getExpiresTime());
                    }
                });
                log.info("--------成功初始化 {} 条黑名单缓存--------", blackLists.size());
            } else {
                log.info("--------没有黑名单数据，跳过初始化--------");
            }
        } catch (Exception e) {
            log.warn("--------初始化黑名单缓存失败，可能表不存在，请手动执行 sql/t_black_list.sql 创建表--------", e.getMessage());
        }
    }
}