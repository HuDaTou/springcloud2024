package com.overthinker.cloud.web.interceptor;


import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.overthinker.cloud.redis.utils.MyRedisCache;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.common.resp.ReturnCodeEnum;
import com.overthinker.cloud.common.annotation.AccessLimit;

import com.overthinker.cloud.web.entity.PO.BlackList;
import com.overthinker.cloud.redis.constants.RedisConst;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.enums.BlackListPolicy;

import com.overthinker.cloud.web.utils.IpUtils;


import com.overthinker.cloud.web.utils.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis限流拦截器
 */
@Slf4j
@Component("accessLimitInterceptor")
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Resource
    private MyRedisCache myRedisCache;



    private static final String EXPIRE_TIME_KEY_PREFIX = "expire_time_";

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        boolean result = true;
        // 是否是HandlerMethod实例
        if (handler instanceof HandlerMethod handlerMethod) {
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);

            // 方法上面如果没有限流注解就直接通过
            if (accessLimit == null)
                return result;
            // 如果方法上有限流注解
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String ip = IpUtils.getIpAddr(request);
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String key = "limit:" + method + ":" + uri + ":" + ip;

            try {
                // 固定过期时间
                String expireTimeKey = EXPIRE_TIME_KEY_PREFIX + key;

                // 从Redis中获取过期时间，保证访问时间与封禁时间一直
                Long expireTime = myRedisCache.getCacheObject(expireTimeKey);

                // redis 进行自增
                Long count = myRedisCache.increment(key, 1L);

                if (count == 1) {
                    // 第一次访问，设置过期时间
                    myRedisCache.expire(key, seconds, TimeUnit.SECONDS);
                    expireTime = System.currentTimeMillis();
                    myRedisCache.setCacheObject(expireTimeKey, expireTime);
                }

                // 封禁判断方法
                if (isBlocked(response, ip, uri, count, expireTime)) {
                    return false;
                }

//                TODO 暂且关闭限流
//                if (count > maxCount) {
//                    WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.REQUEST_FREQUENTLY.getCode(), accessLimit.msg()).asJsonString());
//                    // 限制
//                    log.warn("用户IP[" + ip + "]访问地址[" + uri + "]超过了限定的次数[" + maxCount + "]");
//                    result = false;
//                }
            } catch (RedisConnectionFailureException e) {
                log.error("redis连接异常", e);
                result = false;
            }
        }

        return result;
    }

    private Boolean isBlocked(HttpServletResponse response, String ip, String uri, Long count, Long expireTime) {
        Long timestampByIP = myRedisCache.getCacheMapValue(RedisConst.BLACK_LIST_IP_KEY, ip);
        Long timestampByUID = myRedisCache.getCacheMapValue(RedisConst.BLACK_LIST_UID_KEY, String.valueOf(SecurityUtils.getUserId()));

        // 封禁策略
        for (BlackListPolicy policy : BlackListPolicy.values()) {
            if (count == policy.getRequestThreshold()) {
                return handleBlackList(policy.getTimeUnit(), policy.getDuration(), expireTime, timestampByIP,
                        policy.getMessage(), StrUtil.format(policy.getReason(), ip, uri, count), response);
            }
        }

        if (timestampByIP != null || timestampByUID != null) {
            Long timestamp = timestampByIP != null ? timestampByIP : timestampByUID;
            // 判断是否已解封
            if (System.currentTimeMillis() > timestamp) {
                // 解封
                if (timestampByIP != null) {
                    myRedisCache.delCacheMapValue(RedisConst.BLACK_LIST_IP_KEY, ip);
                    blackListMapper.deleteByIp(ip);
                } else {
                    myRedisCache.delCacheMapValue(RedisConst.BLACK_LIST_UID_KEY, String.valueOf(SecurityUtils.getUserId()));
                    blackListMapper.delete(new LambdaQueryWrapper<BlackList>().eq(BlackList::getUserId, SecurityUtils.getUserId()));
                }
            } else {
                DateTime date = DateUtil.date(timestampByIP != null ? timestampByIP : timestampByUID);
                WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.BLACK_LIST_ERROR.getCode(), StrUtil.format("已被封禁，无法访问，距解封剩余：{}", DateUtil.formatBetween(new Date(), date, BetweenFormatter.Level.SECOND))).asJsonString());
                return true;
            }
        }
        return false;
    }


    private boolean handleBlackList(DateField dateField, int offset, Long expireTime, Long timestampByIP, String message, String reason, HttpServletResponse response) {
        // 如果当前已经封禁，并且封禁的时间大于当前时间这不会再次进行封禁
        if (timestampByIP != null && DateUtil.offset(DateUtil.date(expireTime), dateField, offset).getTime() < timestampByIP) {
            DateTime date = DateUtil.date(timestampByIP);
            WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.BLACK_LIST_ERROR.getCode(),
                    StrUtil.format("已被封禁，无法访问，距解封剩余：{}", DateUtil.formatBetween(new Date(), date, BetweenFormatter.Level.SECOND))).asJsonString());
            return true;
        }

        // 封禁、加入黑名单
        AddBlackListDTO addBlackListDTO = AddBlackListDTO.builder()
                .userIds((SecurityUtils.getUserId() == 0L || Objects.equals(SecurityUtils.getUserId(), SQLConst.ADMIN_ID)) ? List.of() : List.of(SecurityUtils.getUserId()))
                .reason(reason)
                .expiresTime(DateUtil.offset(DateUtil.date(expireTime), dateField, offset))
                .build();
        blackListService.addBlackList(addBlackListDTO);
        WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.BLACK_LIST_ERROR.getCode(), message).asJsonString());
        return true;
    }
}

