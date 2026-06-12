package com.overthinker.cloud.web.entity.server;

import com.overthinker.cloud.common.core.utils.MyDateUtils;
import com.overthinker.cloud.web.utils.Arith;
import lombok.Setter;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * JVM相关信息
 *
 * @author ruoyi
 */
@Setter
public class Jvm {
    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal() {
        return Arith.div(total, (1024 * 1024), 2);
    }

    public double getMax() {
        return Arith.div(max, (1024 * 1024), 2);
    }

    public double getFree() {
        return Arith.div(free, (1024 * 1024), 2);
    }

    public double getUsed() {
        return Arith.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage() {
        return Arith.mul(Arith.div(total - free, total, 4), 100);
    }

    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public String getVersion() {
        return version;
    }

    public String getHome() {
        return home;
    }

    /**
     * JDK启动时间
     */
    public String getStartTime() {
        LocalDateTime startTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(ManagementFactory.getRuntimeMXBean().getStartTime()),
                ZoneId.systemDefault());
        return MyDateUtils.format(startTime, MyDateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * JDK运行时间
     */
    public String getRunTime() {
        long runtimeMs = System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
        return MyDateUtils.formatDuration(runtimeMs);
    }

    /**
     * 运行参数
     */
    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }
}
