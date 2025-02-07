package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.constants.Const;
import com.overthinker.cloud.web.entity.DTO.LoginLogDTO;
import com.overthinker.cloud.web.entity.DTO.LoginLogDeleteDTO;
import com.overthinker.cloud.web.entity.PO.LoginLog;
import com.overthinker.cloud.web.entity.VO.LoginLogVO;
import com.overthinker.cloud.web.mapper.LoginLogMapper;
import com.overthinker.cloud.web.service.LoginLogService;
import com.overthinker.cloud.web.utils.BrowserUtil;
import com.overthinker.cloud.web.utils.IpUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * (LoginLog)表服务实现类
 *
 * @author overH
 * @since 2023-12-08 14:38:44
 */
@Slf4j
@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routingKey.log-login}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange.log}")
    private String exchange;

    @Override
    public void loginLog(HttpServletRequest request, String userName, Integer state, String message) {
        String browserName = BrowserUtil.browserName(request);
        String ipAddress = IpUtils.getIpAddr(request);
        String os = BrowserUtil.osName(request);
        int requestType;
        String typeHeader = request.getHeader(Const.TYPE_HEADER);
        if (StringUtils.isNotEmpty(typeHeader) && typeHeader.equals(Const.FRONTEND_REQUEST)) {
            requestType = 0;
        } else if (StringUtils.isNotEmpty(typeHeader) && typeHeader.equals(Const.BACKEND_REQUEST)) {
            requestType = 1;
        } else {
            requestType = 2;
        }
        if (userName == null) {
            userName = "未知用户";
        }
        LoginLog logEntity = LoginLog.builder()
                .userName(userName)
                .ip(ipAddress)
                .browser(browserName)
                .os(os)
                .type(requestType)
                .state(state)
                .message(message)
                .build();

        rabbitTemplate.convertAndSend(exchange, routingKey, logEntity);
        log.info("{}", "发送登录日志信息--rabbitMQ");
    }

    @Override
    public List<LoginLogVO> searchLoginLog(LoginLogDTO loginLogDTO) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(loginLogDTO)) {
            wrapper.like(StringUtils.isNotEmpty(loginLogDTO.getAddress()), LoginLog::getAddress, loginLogDTO.getAddress())
                    .like(StringUtils.isNotEmpty(loginLogDTO.getUserName()), LoginLog::getUserName, loginLogDTO.getUserName())
                    .eq(StringUtils.isNotNull(loginLogDTO.getState()), LoginLog::getState, loginLogDTO.getState());
            if (StringUtils.isNotNull(loginLogDTO.getLoginTimeStart()) && StringUtils.isNotNull(loginLogDTO.getLoginTimeEnd())) {
                wrapper.gt(LoginLog::getCreateTime, loginLogDTO.getLoginTimeStart()).and(a -> a.lt(LoginLog::getCreateTime, loginLogDTO.getLoginTimeEnd()));
            }
        }
        wrapper.orderByDesc(LoginLog::getCreateTime);
        return loginLogMapper.selectList(wrapper).stream().map(loginLog -> loginLog.asViewObject(LoginLogVO.class,v -> v.setLoginTime(loginLog.getCreateTime()))).toList();
    }

    @Transactional
    @Override
    public ResultData<Void> deleteLoginLog(LoginLogDeleteDTO loginLogDeleteDTO) {
        if (this.removeByIds(loginLogDeleteDTO.getIds())) {
            return ResultData.success();
        }
        return ResultData.failure();
    }
}
