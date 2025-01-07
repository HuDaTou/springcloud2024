package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.constants.RedisConst;
import com.overthinker.cloud.web.entity.PO.Like;
import com.overthinker.cloud.web.entity.enums.UserEnum.LikeEnum;
import com.overthinker.cloud.web.mapper.LikeMapper;
import com.overthinker.cloud.web.service.LikeService;
import com.overthinker.cloud.web.utils.RedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * (Like)表服务实现类
 *
 * @author kuailemao
 * @since 2023-10-18 19:41:19
 */
@Service("likeService")
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResultData<Void> userLike(Integer type, Integer typeId) {
        // 查询是否已经点赞
        Like like = likeMapper.selectOne(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, SecurityUtils.getUserId())
                .eq(Like::getType, type)
                .eq(Like::getTypeId, typeId));
        if (like != null) return ResultData.failure();
        Like saveLike = Like.builder()
                .userId(SecurityUtils.getUserId())
                .type(type)
                .typeId(typeId).build();
        if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType()))
            redisCache.incrementCacheMapValue(RedisConst.ARTICLE_LIKE_COUNT, typeId.toString(), 1);
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
            redisCache.incrementCacheMapValue(RedisConst.ARTICLE_LIKE_COUNT, typeId.toString(), -1);
        if (like) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public ResultData<List<Like>> isLike(Integer type, Integer typeId) {
        if (SecurityUtils.isLogin()) {
            LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
            if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType())) {
                List<Like> like = likeMapper.selectList(wrapper
                        .eq(Like::getUserId, SecurityUtils.getUserId())
                        .eq(Like::getType, type)
                        .eq(Like::getTypeId, typeId));
                if (!like.isEmpty()) return ResultData.success(like);
                else ResultData.failure(null);
            }
            if (Objects.equals(type, LikeEnum.LIKE_TYPE_COMMENT.getType()) || Objects.equals(type, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType())) {
                if (Objects.equals(type, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType())) wrapper.eq(Like::getTypeId, typeId);
                List<Like> like = likeMapper.selectList(wrapper
                        .eq(Like::getUserId, SecurityUtils.getUserId())
                        .eq(Like::getType, type));
                if (!like.isEmpty()) return ResultData.success(like);
                else ResultData.failure(null);
            }

        }
        return ResultData.failure(null);
    }

    @Override
    public Long getLikeCount(Integer likeTypeComment, Long id) {
        return likeMapper.selectCount(new LambdaQueryWrapper<Like>()
                .eq(Like::getType, likeTypeComment)
                .eq(Like::getTypeId, id));
    }

}
