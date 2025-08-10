package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.web.entity.PO.Like;

import java.util.List;


/**
 * (Like)表服务接口
 *
 * @author overH
 * @since 2023-10-18 19:41:19
 */
public interface LikeService extends IService<Like> {

    /**
     * 点赞文章
     *
     * @param type   点赞类型
     * @param typeId 点赞id
     * @return 点赞结果
     */
    ResultData<Void> userLike(Integer type, Integer typeId);

    /**
     * 取消点赞
     *
     * @param type   点赞类型
     * @param typeId 点赞id
     * @return 取消点赞结果
     */
    ResultData<Void> cancelLike(Integer type, Integer typeId);

    /**
     * 是否点赞
     *
     * @param type   点赞类型
     * @param typeId 点赞id
     * @return 是否点赞
     */
    ResultData<List<Like>> isLike(Integer type, Integer typeId);

    /**
     * 获取点赞数
     *
     * @param likeTypeComment 点赞类型
     * @param id              点赞id
     * @return 点赞数量
     */
    Long getLikeCount(Integer likeTypeComment, Long id);
}
