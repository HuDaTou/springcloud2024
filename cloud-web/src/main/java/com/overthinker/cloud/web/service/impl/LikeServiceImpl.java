package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.PO.Like;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.web.entity.enums.LikeEnum;
import com.overthinker.cloud.web.mapper.LikeMapper;
import com.overthinker.cloud.web.service.LikeService;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * (Like)表服务实现类
 *
 * @author overH
 * @since 2023-10-18 19:41:19
 */
@Slf4j
@Service("likeService")
@RequiredArgsConstructor
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    private final LikeMapper likeMapper;
    private final MyRedisCache myRedisCache;

    @Override
    public ResultData<Void> userLike(Integer type, Integer typeId) {
        // 查询是否已经点赞
        Like like = likeMapper.selectOne(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, SecurityUtils.getUserId())
                .eq(Like::getType, type)
                .eq(Like::getTypeId, typeId));
        if (like != null) return ResultData.failure();
        Like saveLike = new Like()
                .setUserId(SecurityUtils.getUserId())
                .setType(type)
                .setTypeId(typeId);
        if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType()))
            myRedisCache.incrementCacheMapValue(RedisConstants.ARTICLE_LIKE_COUNT, typeId.toString(), 1);
        if (this.save(saveLike)) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public ResultData<Void> cancelLike(Integer type, Integer typeId) {
        // 查询是否已经点赞
        Like isLike = likeMapper.selectOne(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, SecurityUtils.getUserId())
                .eq(Like::getType, type)
                .eq(Like::getTypeId, typeId));
        if (Objects.isNull(isLike)) return ResultData.failure();
        boolean like = this.remove(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, SecurityUtils.getUserId())
                .eq(Like::getType, type)
                .eq(Like::getTypeId, typeId));
        if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType()))
            myRedisCache.incrementCacheMapValue(RedisConstants.ARTICLE_LIKE_COUNT, typeId.toString(), -1);
        if (like) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public ResultData<List<Like>> isLike(Integer type, Integer typeId) {
        if (SecurityUtils.isAuthenticated()) {
            LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
            if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType())) {
                List<Like> like = likeMapper.selectList(wrapper
                        .eq(Like::getUserId, SecurityUtils.getUserId())
                        .eq(Like::getType, type)
                        .eq(Like::getTypeId, typeId));
                if (!like.isEmpty()) return ResultData.success(like);
                else ResultData.failure((String) null);
            }
            if (Objects.equals(type, LikeEnum.LIKE_TYPE_COMMENT.getType()) || Objects.equals(type, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType())) {
                if (Objects.equals(type, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType())) wrapper.eq(Like::getTypeId, typeId);
                List<Like> like = likeMapper.selectList(wrapper
                        .eq(Like::getUserId, SecurityUtils.getUserId())
                        .eq(Like::getType, type));
                if (!like.isEmpty()) return ResultData.success(like);
                else ResultData.failure((String) null);
            }

        }
        return ResultData.failure((String) null);
    }

    @Override
    public Long getLikeCount(Integer likeTypeComment, Long id) {
        return likeMapper.selectCount(new LambdaQueryWrapper<Like>()
                .eq(Like::getType, likeTypeComment)
                .eq(Like::getTypeId, id));
    }

}
