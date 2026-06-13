package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.overthinker.cloud.web.entity.PO.Comment;
import com.overthinker.cloud.web.entity.PO.Favorite;
import com.overthinker.cloud.web.entity.PO.Like;
import com.overthinker.cloud.web.entity.VO.ArticleVO;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.enums.CommentEnum;
import com.overthinker.cloud.web.entity.enums.FavoriteEnum;
import com.overthinker.cloud.web.entity.enums.LikeEnum;
import com.overthinker.cloud.web.mapper.*;
import com.overthinker.cloud.web.service.RedisService;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/22 15:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final MyRedisCache myRedisCache;
    private final ArticleMapper articleMapper;
    private final FavoriteMapper favoriteMapper;
    private final LikeMapper likeMapper;
    private final CommentMapper commentMapper;

    @Override
    public void articleCountClear() {
        log.info("--------执行清除redis文章相关数量缓存--------");
        boolean isDel = myRedisCache.deleteObject(RedisConstants.ARTICLE_FAVORITE_COUNT);
        isDel = isDel && myRedisCache.deleteObject(RedisConstants.ARTICLE_LIKE_COUNT);
        isDel = isDel && myRedisCache.deleteObject(RedisConstants.ARTICLE_COMMENT_COUNT);
        if (isDel) log.info("--------清除redis文章相关数量缓存成功--------");
        else log.info("--------清除redis文章相关数量缓存失败--------");
    }

    @Override
    public void articleVisitCount() {
        try {
            articleMapper.selectList(null).forEach(article -> myRedisCache.setCacheObject(RedisConstants.ARTICLE_VISIT_COUNT + article.getId(), article.getVisitCount()));
            log.info("--------执行redis文章访问量缓存成功--------");
        } catch (Exception e) {
            log.error("--------执行redis文章访问量缓存失败", e);
        }
    }

    @Override
    public void clearLimitCache() {
        log.info("--------执行清除redis限流缓存--------:");
        try {
            Collection<String> keys = myRedisCache.keys("limit*");
            if (myRedisCache.deleteObject(keys) > 0) log.info("--------清除redis限流缓存成功--------");
            else log.info("--------没有redis限流缓存，无法清除--------");
        } catch (Exception e) {
            log.error("--------执行清除redis限流缓存失败", e);
        }

    }

    @Override
    public void initCount() {
        log.info("--------开始执行缓存文章点赞数量，评论数量，收藏数量--------");
        // 文章收藏量
        List<ArticleVO> articleVOS = articleMapper.selectList(null).stream().map(article -> article.copyProperties(ArticleVO.class)).toList();
        articleVOS.forEach(articleVO -> {
            // 文章收藏量
            articleVO.setFavoriteCount(favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>().eq(Favorite::getTypeId, articleVO.getId()).eq(Favorite::getType, FavoriteEnum.FAVORITE_TYPE_ARTICLE.getType())));
            // 文章点赞量
            articleVO.setLikeCount(likeMapper.selectCount(new LambdaQueryWrapper<Like>().eq(Like::getTypeId, articleVO.getId()).eq(Like::getType, LikeEnum.LIKE_TYPE_ARTICLE.getType())));
            // 文章评论量
            articleVO.setCommentCount(commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getTypeId, articleVO.getId()).eq(Comment::getType, CommentEnum.COMMENT_TYPE_ARTICLE.getType()).eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK)));
        });
        Map<String, Long> favoriteCount = articleVOS.stream().collect(Collectors.toMap(articleVO -> articleVO.getId().toString(), ArticleVO::getFavoriteCount));
        Map<String, Long> likeCount = articleVOS.stream().collect(Collectors.toMap(articleVO -> articleVO.getId().toString(), ArticleVO::getLikeCount));
        Map<String, Long> commentCount = articleVOS.stream().collect(Collectors.toMap(articleVO -> articleVO.getId().toString(), ArticleVO::getCommentCount));
        myRedisCache.setCacheMap(RedisConstants.ARTICLE_FAVORITE_COUNT, favoriteCount);
        myRedisCache.setCacheMap(RedisConstants.ARTICLE_LIKE_COUNT, likeCount);
        myRedisCache.setCacheMap(RedisConstants.ARTICLE_COMMENT_COUNT, commentCount);
        log.info("--------成功执行缓存文章点赞数量，评论数量，收藏数量--------");
    }
}
