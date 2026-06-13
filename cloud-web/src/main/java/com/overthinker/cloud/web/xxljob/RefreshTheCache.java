package com.overthinker.cloud.web.xxljob;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import com.overthinker.cloud.web.entity.PO.Article;
import com.overthinker.cloud.web.mapper.ArticleMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author overH
 * <p>
 * 创建时间：2024/1/1 22:25
 * 刷新缓存任务 / 5分钟刷新一次
 */
@Slf4j
public class RefreshTheCache  {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private MyRedisCache redisCache;


    @XxlJob("executeInternal")
    public void executeInternal() {
        log.info("-------------------------------开始同步文章浏览量到数据库-------------------------------");
        try {
            List<Long> articleIds = articleMapper.selectList(null).stream().map(Article::getId).toList();
            articleIds.forEach(id -> {
                Object cacheObj = redisCache.getCacheObject(RedisConstants.ARTICLE_VISIT_COUNT + id);
                if (cacheObj != null) {
                    Long cacheObject = ((Number) cacheObj).longValue();
                    articleMapper.update(null, new LambdaUpdateWrapper<Article>().eq(Article::getId, id).set(Article::getVisitCount, cacheObject));
                }
            });
            log.info("-------------------------------同步文章浏览量成功-------------------------------");
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("同步文章浏览量失败", e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}
