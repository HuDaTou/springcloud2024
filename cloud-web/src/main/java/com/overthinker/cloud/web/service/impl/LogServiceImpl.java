package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.LogDTO;
import com.overthinker.cloud.web.entity.DTO.LogDeleteDTO;
import com.overthinker.cloud.web.entity.PO.Log;
import com.overthinker.cloud.web.entity.VO.LogVO;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.mapper.LogMapper;
import com.overthinker.cloud.web.service.LogService;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * (Log)表服务实现类
 *
 * @author overH
 * @since 2023-12-12 09:12:32
 */
@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public PageVO searchLog(LogDTO logDTO, Long current, Long pageSize) {
        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(logDTO)) {
            wrapper.like(StringUtils.isNotEmpty(logDTO.getIp()), Log::getIp, logDTO.getIp())
                    .like(StringUtils.isNotEmpty(logDTO.getModule()),Log::getModule, logDTO.getModule())
                    .like(StringUtils.isNotEmpty(logDTO.getUserName()),Log::getUserName, logDTO.getUserName())
                    .like(StringUtils.isNotEmpty(logDTO.getOperation()),Log::getOperation, logDTO.getOperation())
                    .eq(StringUtils.isNotNull(logDTO.getState()),Log::getState, logDTO.getState());
            if (StringUtils.isNotNull(logDTO.getLogTimeStart()) && StringUtils.isNotNull(logDTO.getLogTimeEnd())) {
                wrapper.gt(Log::getCreateTime, logDTO.getLogTimeStart()).and(a -> a.lt(Log::getCreateTime, logDTO.getLogTimeEnd()));
            }
        }
        wrapper.orderByDesc(Log::getCreateTime);
        Page<Log> page = new Page<>(current, pageSize);
        logMapper.selectPage(page,wrapper);
        List<LogVO> logVOS = page.getRecords().stream().map(log -> log.asViewObject(LogVO.class, v -> v.setLoginTime(log.getCreateTime()))).toList();

        return PageVO.builder().page(logVOS).total(page.getTotal()).build();
    }

    @Transactional
    @Override
    public ResultData<Void> deleteLog(LogDeleteDTO logDeleteDTO) {
        if (this.removeByIds(logDeleteDTO.getIds())) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

}
