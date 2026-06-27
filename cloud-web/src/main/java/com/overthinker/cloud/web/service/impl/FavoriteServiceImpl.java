package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.auth.api.UserClient;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.FavoriteIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchFavoriteDTO;
import com.overthinker.cloud.web.entity.PO.Favorite;
import com.overthinker.cloud.web.entity.VO.FavoriteListVO;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.web.mapper.ArticleMapper;
import com.overthinker.cloud.web.mapper.FavoriteMapper;
import com.overthinker.cloud.web.mapper.LeaveWordMapper;
import com.overthinker.cloud.web.service.FavoriteService;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * (Favorite)表服务实现类
 *
 * @author overH
 * @since 2023-10-18 14:12:25
 */
@Slf4j
@Service("favoriteService")
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final MyRedisCache myRedisCache;
    private final UserClient userClient;
    private final ArticleMapper articleMapper;
    private final LeaveWordMapper leaveWordMapper;

    @Override
    public ResultData<Void> userFavorite(Integer type, Long typeId) {
        // 查询是否已经收藏
        Favorite favorite = favoriteMapper.selectOne(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, SecurityUtils.getUserId())
                .eq(Favorite::getType, type)
                .eq(Favorite::getTypeId, typeId));
        if (MyStringUtils.isNotNull(favorite)) return ResultData.failure();
        Favorite Savefavorite = new Favorite()
                .setId(null)
                .setUserId(SecurityUtils.getUserId())
                .setType(type)
                .setTypeId(typeId);
        myRedisCache.incrementCacheMapValue(RedisConstants.ARTICLE_FAVORITE_COUNT, typeId.toString(), 1);
        if (this.save(Savefavorite)) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public ResultData<Void> cancelFavorite(Integer type, Integer typeId) {
        // 查询是否已经收藏
        Favorite isFavorite = favoriteMapper.selectOne(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, SecurityUtils.getUserId())
                .eq(Favorite::getType, type)
                .eq(Favorite::getTypeId, typeId));
        if (Objects.isNull(isFavorite)) return ResultData.failure();
        boolean cancelFavorite = this.remove(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, SecurityUtils.getUserId())
                .eq(Favorite::getType, type)
                .eq(Favorite::getTypeId, typeId));
        myRedisCache.incrementCacheMapValue(RedisConstants.ARTICLE_FAVORITE_COUNT, typeId.toString(), -1);
        if (cancelFavorite) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public Boolean isFavorite(Integer type, Integer typeId) {
        if (SecurityUtils.isAuthenticated()) {
            // 是否收藏
            Favorite favorite = favoriteMapper.selectOne(new LambdaQueryWrapper<Favorite>()
                    .eq(Favorite::getUserId, SecurityUtils.getUserId())
                    .eq(Favorite::getType, type)
                    .eq(Favorite::getTypeId, typeId));
            return favorite != null;
        }
        return false;
    }

    @Override
    public List<FavoriteListVO> getBackFavoriteList(SearchFavoriteDTO searchDTO) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        if (MyStringUtils.isNotNull(searchDTO)) {
            ResultData<List<Long>> userIdsResult = userClient.searchUserIdsByUsername(searchDTO.getUserName());
            List<Long> userIds = userIdsResult.getData() != null ? userIdsResult.getData() : List.of();
            if (!userIds.isEmpty())
                wrapper.in(MyStringUtils.isNotEmpty(searchDTO.getUserName()), Favorite::getUserId, userIds);
            else
                wrapper.eq(MyStringUtils.isNotNull(searchDTO.getUserName()), Favorite::getUserId, null);

            wrapper.eq(MyStringUtils.isNotNull(searchDTO.getIsCheck()), Favorite::getIsCheck, searchDTO.getIsCheck())
                    .eq(MyStringUtils.isNotNull(searchDTO.getType()), Favorite::getType, searchDTO.getType());
            if (MyStringUtils.isNotNull(searchDTO.getStartTime()) && MyStringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(Favorite::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<Favorite> favorites = favoriteMapper.selectList(wrapper);
        if (!favorites.isEmpty()) {
            return favorites.stream().map(favorite -> {
                FavoriteListVO vo = favorite.copyProperties(FavoriteListVO.class);
                ResultData<String> usernameResult = userClient.getUsernameById(favorite.getUserId());
                vo.setUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
                switch (favorite.getType()) {
                    case 1 -> vo.setContent(articleMapper.selectById(favorite.getTypeId()).getArticleContent());
                    case 2 -> vo.setContent(leaveWordMapper.selectById(favorite.getTypeId()).getContent());
                }
                return vo;
            }).toList();
        }
        return null;
    }

    @Override
    public ResultData<Void> isCheckFavorite(FavoriteIsCheckDTO isCheckDTO) {
        if (favoriteMapper.updateById(new Favorite().setId(isCheckDTO.getId()).setIsCheck(isCheckDTO.getIsCheck())) > 0)
            return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public ResultData<Void> deleteFavorite(List<Long> ids) {
        if (favoriteMapper.deleteByIds(ids) > 0) return ResultData.success();
        return ResultData.failure();
    }
}
