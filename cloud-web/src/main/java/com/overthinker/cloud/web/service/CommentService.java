package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.CommentIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCommentDTO;

import com.overthinker.cloud.web.entity.PO.Comment;
import com.overthinker.cloud.web.entity.VO.ArticleCommentVO;
import com.overthinker.cloud.web.entity.VO.CommentListVO;
import com.overthinker.cloud.web.entity.VO.PageVO;

import java.util.List;


/**
 * (CommentEmail)表服务接口
 *
 * @author overH
 * @since 2023-10-19 15:44:57
 */
public interface CommentService extends IService<Comment> {
    /**
     * 查询文章评论
     */
    PageVO<List<ArticleCommentVO>> getComment(Integer type, Integer typeId, Integer pageNum, Integer pageSize);

    /**
     * 添加评论
     */
    ResultData<String> userComment(UserCommentDTO commentDTO);

    /**
     * 后台评论列表
     *
     * @return 结果
     */
    List<CommentListVO> getBackCommentList(SearchCommentDTO searchDTO);

    /**
     * 是否通过评论
     *
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResultData<Void> isCheckComment(CommentIsCheckDTO isCheckDTO);

    /**
     * 删除评论
     *
     * @param id id 列表
     * @return 是否成功
     */
    ResultData<Void> deleteComment(Long id);
}
