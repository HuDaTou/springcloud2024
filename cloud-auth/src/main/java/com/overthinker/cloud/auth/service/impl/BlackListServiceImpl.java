package com.overthinker.cloud.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.constants.BlackListConst;
import com.overthinker.cloud.auth.entity.DTO.AddBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.SearchBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.UpdateBlackListDTO;
import com.overthinker.cloud.auth.entity.PO.BlackList;
import com.overthinker.cloud.auth.entity.VO.BlackListVO;
import com.overthinker.cloud.auth.entity.ip.BlackListIpInfo;
import com.overthinker.cloud.auth.mapper.BlackListMapper;
import com.overthinker.cloud.auth.mapper.UserMapper;
import com.overthinker.cloud.auth.service.BlackListService;
import com.overthinker.cloud.auth.service.IpService;


import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.web.utils.ServletUtils;
import com.overthinker.cloud.common.core.utils.StringUtils;
import com.overthinker.cloud.redis.utils.MyRedisCache;

import com.overthinker.cloud.auth.entity.PO.User;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (BlackList)表服务实现类
 *
 * @author overH
 * @since 2024-09-05 16:13:21
 */
@Service("blackListService")
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListService {

    @Resource
    private BlackListMapper blackListMapper;

    @Resource
    private IpService ipService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyRedisCache myRedisCache;

    @Override
    public ResultData<Void> addBlackList(AddBlackListDTO addBlackListDTO) {
        if (!addBlackListDTO.userIds().isEmpty()) {
            Long count = blackListMapper.selectCount(new LambdaQueryWrapper<BlackList>().in(BlackList::getUserId, addBlackListDTO.userIds()));
            if (count > 0) {
                return ResultData.failure("用户已存在黑名单中");
            }

            for (int i = 0; i < addBlackListDTO.userIds().size(); i++) {
                if (!saveBlackList(addBlackListDTO, i)) {
                    return ResultData.failure("添加黑名单失败");
                }
            }
        } else {
            if (!saveBlackList(addBlackListDTO, null)) {
                return ResultData.failure("添加黑名单失败");
            }
        }

        return ResultData.success();
    }

    protected Boolean saveBlackList(AddBlackListDTO addBlackListDTO, Integer index) {
        BlackList blackList = new BlackList()
                .setUserId(!addBlackListDTO.userIds().isEmpty() ? addBlackListDTO.userIds().get(index) : null)
                .setReason(addBlackListDTO.reason())
                .setType(!addBlackListDTO.userIds().isEmpty()
                        ? BlackListConst.BLACK_LIST_TYPE_USER
                        : BlackListConst.BLACK_LIST_TYPE_BOT)
                .setExpiresTime(addBlackListDTO.expiresTime());

        BlackListIpInfo blackListIpInfo = new BlackListIpInfo()
                .setCreateIp(!addBlackListDTO.userIds().isEmpty()
                        ? null
                        : Objects.requireNonNull(ServletUtils.getRequest()).getRemoteAddr());
        blackList.setIpInfo(blackListIpInfo);
        if (addBlackListDTO.userIds().isEmpty()) {
            Long idByIp = blackListMapper.getIdByIp(blackListIpInfo.getCreateIp());
            if (idByIp != null) {
                // 存在
                blackList.setId(idByIp);
            }
        }
        if (null != blackList.getId() ? this.updateById(blackList) : this.save(blackList)) {
            if (Objects.equals(blackList.getType(), BlackListConst.BLACK_LIST_TYPE_BOT)) {
                ipService.refreshIpDetailAsyncByBid(blackList.getId());
            }
            updateBlackListCache(blackList);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<BlackListVO> getBlackList(SearchBlackListDTO searchBlackListDTO) {
        LambdaQueryWrapper<BlackList> queryWrapper = new LambdaQueryWrapper<>();

        if (null != searchBlackListDTO) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchBlackListDTO.getUserName()));
            if (!users.isEmpty())
                queryWrapper.in(StringUtils.isNotEmpty(searchBlackListDTO.getUserName()), BlackList::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                queryWrapper.eq(StringUtils.isNotNull(searchBlackListDTO.getUserName()), BlackList::getUserId, null);

            queryWrapper.like(StrUtil.isNotBlank(searchBlackListDTO.getReason()), BlackList::getReason, searchBlackListDTO.getReason())
                    .eq(null != searchBlackListDTO.getType(), BlackList::getType, searchBlackListDTO.getType())
                    .between(StringUtils.isNotNull(searchBlackListDTO.getStartTime()) && StringUtils.isNotNull(searchBlackListDTO.getEndTime()), BlackList::getBannedTime, searchBlackListDTO.getStartTime(), searchBlackListDTO.getEndTime());
        }
        queryWrapper.orderByDesc(BlackList::getCreateTime);

        return this.list(queryWrapper).stream()
                .map(blackList ->
                        blackList.copyProperties(
                                BlackListVO.class, (black) ->
                                {
                                    if (blackList.getUserId() != null) {
                                        black.setUserName(
                                                userMapper.selectById(blackList.getUserId()).getUsername()
                                        );
                                    }
                                }
                        )
                )
                .toList();
    }

    @Override
    public ResultData<Void> updateBlackList(UpdateBlackListDTO updateBlackListDTO) {
        BlackList blackList = new BlackList()
                .setId(updateBlackListDTO.getId())
                .setReason(updateBlackListDTO.getReason())
                .setExpiresTime(updateBlackListDTO.getExpiresTime());
        if (this.updateById(blackList)) {
            // 修改缓存
            BlackList black = blackListMapper.selectById(blackList.getId());
            updateBlackListCache(black);
            return ResultData.success();
        }
        return ResultData.failure();
    }

    private void updateBlackListCache(BlackList blackList) {
        if (BlackListConst.BLACK_LIST_TYPE_BOT.equals(blackList.getType())) {
            // 更新redis缓存
            myRedisCache.setCacheMapValue(BlackListConst.BLACK_LIST_IP_KEY, blackList.getIpInfo().getCreateIp(), blackList.getExpiresTime());
        } else if ( BlackListConst.BLACK_LIST_TYPE_USER.equals(blackList.getType())) {
            myRedisCache.setCacheMapValue(BlackListConst.BLACK_LIST_UID_KEY, blackList.getUserId().toString(), blackList.getExpiresTime());
        }
    }

    @Override
    @Transactional
    public ResultData<Void> deleteBlackList(List<Long> ids) {
        // 清除缓存
        blackListMapper.selectBatchIds(ids).forEach(blackList -> {
            if (BlackListConst.BLACK_LIST_TYPE_BOT.equals(blackList.getType())) {
                // 清除缓存
                myRedisCache.deleteCacheMapValue(BlackListConst.BLACK_LIST_IP_KEY, blackList.getIpInfo().getCreateIp());
            } else if (BlackListConst.BLACK_LIST_TYPE_USER.equals(blackList.getType())) {
                myRedisCache.deleteCacheMapValue(BlackListConst.BLACK_LIST_UID_KEY, blackList.getUserId().toString());
            }
        });
        if (this.removeBatchByIds(ids)) {
            return ResultData.success();
        }
        return ResultData.failure();
    }
}
