package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.auth.api.EmailClient;
import com.overthinker.cloud.api.apis.auth.api.UserClient;
import com.overthinker.cloud.api.apis.auth.dto.UserCommentDTO;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.CommentIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCommentDTO;
import com.overthinker.cloud.web.entity.PO.Comment;
import com.overthinker.cloud.web.entity.PO.LeaveWord;
import com.overthinker.cloud.web.entity.PO.Like;
import com.overthinker.cloud.web.entity.VO.ArticleCommentVO;
import com.overthinker.cloud.web.entity.VO.CommentListVO;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.enums.CommentEnum;
import com.overthinker.cloud.web.entity.enums.LikeEnum;
import com.overthinker.cloud.web.entity.enums.MailboxAlertsEnum;
import com.overthinker.cloud.web.mapper.CommentMapper;
import com.overthinker.cloud.web.mapper.LeaveWordMapper;
import com.overthinker.cloud.web.mapper.LikeMapper;
import com.overthinker.cloud.web.service.CommentService;
import com.overthinker.cloud.web.service.LikeService;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (CommentEmail)表服务实现类
 *
 * @author overH
 */
@Slf4j
@Service("commentService")
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;
    private final UserClient userClient;
    private final LikeService likeService;
    private final MyRedisCache myRedisCache;
    private final LikeMapper likeMapper;
    private final EmailClient emailClient;
    private final LeaveWordMapper leaveWordMapper;

    @Value("${spring.mail.username}")
    private String fromUser;

    @Value("${mail.article-email-notice}")
    private Boolean articleEmailNotice;

    @Value("${mail.article-reply-notice}")
    private Boolean articleReplyNotice;

    @Value("${mail.message-email-notice}")
    private Boolean messageEmailNotice;

    @Value("${mail.message-reply-notice}")
    private Boolean messageReplyNotice;

    @Override
    public PageVO<List<ArticleCommentVO>> getComment(Integer type, Integer typeId, Integer pageNum, Integer pageSize) {
        // 查询父评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();


        queryWrapper
                .orderByDesc(Comment::getCreateTime)
                .eq(Comment::getType, type)
                .eq(Comment::getTypeId, typeId)
                .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK)
                .isNull(Comment::getParentId);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        IPage<Comment> commentIPage = commentMapper.selectPage(page, queryWrapper);
        List<Comment> comments = commentIPage.getRecords();
        // 查询所有子评论
        LambdaQueryWrapper<Comment> childQueryWrapper = new LambdaQueryWrapper<>();
        childQueryWrapper
                .orderByDesc(Comment::getCreateTime)
                .eq(Comment::getType, type)
                .eq(Comment::getTypeId, typeId)
                .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK)
                .isNotNull(Comment::getParentId);
        List<Comment> childComment = commentMapper.selectList(childQueryWrapper);
        if (!childComment.isEmpty()) comments.addAll(childComment);
        List<ArticleCommentVO> commentsVOS = comments.stream().map(comment -> comment.copyProperties(ArticleCommentVO.class)).toList();
        List<ArticleCommentVO> parentComments = commentsVOS.stream().filter(comment -> comment.getParentId() == null).toList();
        List<ArticleCommentVO> collect = parentComments.stream().peek(comment -> {
                    comment.setChildComment(getChildComment(commentsVOS, comment.getId()));
                    comment.setChildCommentCount(getChildCommentCount(commentsVOS, comment.getId()));
                    comment.setParentCommentCount(this.count(new LambdaQueryWrapper<Comment>()
                            .eq(Comment::getType, type)
                            .eq(Comment::getTypeId, typeId)
                            .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK)
                            .isNull(Comment::getParentId)));
                }
        ).toList();
        // 总评论数量
        LambdaQueryWrapper<Comment> countWrapper = new LambdaQueryWrapper<>();
        countWrapper
                .eq(Comment::getType, type)
                .eq(Comment::getTypeId, typeId)
                .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK);
        return new PageVO<>(collect, commentMapper.selectCount(countWrapper));
    }

    @Override
    public ResultData<String> userComment(UserCommentDTO commentDTO) {
        Comment comment = commentDTO.copyProperties(Comment.class, commentDto -> commentDto.setCommentUserId(SecurityUtils.getUserId()));
        if (this.save(comment)) {
            ResultData<String> emailResult = userClient.getEmailById(SecurityUtils.getUserId());
            if (MyStringUtils.isEmpty(emailResult.getData())) {
                return ResultData.success("检测到您尚未绑定邮箱,无法开启邮箱提醒，请先绑定邮箱");
            }
            return this.commentEmailReminder(commentDTO, emailResult.getData(), comment);
        }
        return ResultData.failure();
    }

    /**
     * 评论邮箱提醒
     *
     * @param commentDTO 前端DTO
     * @param userEmail  用户邮箱
     * @param comment    新增评论消息
     * @return ResultData
     */
    public ResultData<String> commentEmailReminder(UserCommentDTO commentDTO, String userEmail, Comment comment) {
        myRedisCache.incrementCacheMapValue(RedisConstants.ARTICLE_COMMENT_COUNT, commentDTO.getTypeId().toString(), 1);
        if (MyStringUtils.isNull(commentDTO.getReplyId())) {
            if ((commentDTO.getType() == 1 && !articleEmailNotice) || commentDTO.getType() == 2 && !messageEmailNotice)
                return ResultData.success();

            Map<String, Object> selectWhereMap = new HashMap<>();
            selectWhereMap.put("commentType", commentDTO.getType());
            selectWhereMap.put("commentId", comment.getId());

            if (commentDTO.getType() == 1) {
                if (Objects.equals(fromUser, userEmail)) return ResultData.success();
                emailClient.sendEmailNotification(fromUser, MailboxAlertsEnum.COMMENT_NOTIFICATION_EMAIL.getCodeStr(), selectWhereMap);
            }

            if (commentDTO.getType() == 2) {
                LeaveWord leaveWord = leaveWordMapper.selectOne(new LambdaQueryWrapper<LeaveWord>().eq(LeaveWord::getId, commentDTO.getTypeId()));
                ResultData<String> replyEmailResult = userClient.getEmailById(leaveWord.getUserId());
                String replyEmail = replyEmailResult.getData();
                if (MyStringUtils.isEmpty(replyEmail) || Objects.equals(replyEmail, userEmail))
                    return ResultData.success();
                emailClient.sendEmailNotification(replyEmail, MailboxAlertsEnum.COMMENT_NOTIFICATION_EMAIL.getCodeStr(), selectWhereMap);
            }
        }
        if (Objects.nonNull(commentDTO.getReplyId())) {
            ResultData<String> replyEmailResult = userClient.getEmailById(commentDTO.getReplyUserId());
            String replyEmail = replyEmailResult.getData();
            if ((commentDTO.getType() == 1 && !articleReplyNotice) || (commentDTO.getType() == 2 && !messageReplyNotice))
                return ResultData.success();

            if (Objects.equals(replyEmail, userEmail) && Objects.equals(fromUser, userEmail)) {
                return ResultData.success();
            }

            Map<String, Object> selectWhereMap = new HashMap<>();
            selectWhereMap.put("commentType", commentDTO.getType());
            selectWhereMap.put("commentId", comment.getId());
            selectWhereMap.put("replyCommentId", commentDTO.getReplyId());

            if (!Objects.equals(userEmail, fromUser) && !Objects.equals(replyEmail, fromUser)) {
                emailClient.sendEmailNotification(fromUser, MailboxAlertsEnum.COMMENT_NOTIFICATION_EMAIL.getCodeStr(), selectWhereMap);
            }

            if (!Objects.equals(userEmail, replyEmail)) {
                emailClient.sendEmailNotification(replyEmail, MailboxAlertsEnum.REPLY_COMMENT_NOTIFICATION_EMAIL.getCodeStr(), selectWhereMap);
            }
        }
        return ResultData.success();
    }


    @Override
    public List<CommentListVO> getBackCommentList(SearchCommentDTO searchDTO) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (MyStringUtils.isNotNull(searchDTO)) {
            ResultData<List<Long>> userIdsResult = userClient.searchUserIdsByUsername(searchDTO.getCommentUserName());
            List<Long> userIds = userIdsResult.getData() != null ? userIdsResult.getData() : List.of();
            if (!userIds.isEmpty())
                wrapper.in(MyStringUtils.isNotEmpty(searchDTO.getCommentUserName()), Comment::getCommentUserId, userIds);
            else
                wrapper.eq(MyStringUtils.isNotNull(searchDTO.getCommentUserName()), Comment::getCommentUserId, null);

            wrapper.like(MyStringUtils.isNotEmpty(searchDTO.getCommentContent()), Comment::getCommentContent, searchDTO.getCommentContent())
                    .eq(MyStringUtils.isNotNull(searchDTO.getType()), Comment::getType, searchDTO.getType())
                    .eq(MyStringUtils.isNotNull(searchDTO.getIsCheck()), Comment::getIsCheck, searchDTO.getIsCheck());
        }

        return commentMapper.selectList(wrapper.orderByDesc(Comment::getCreateTime)).stream().map(comment -> {
            CommentListVO vo = comment.copyProperties(CommentListVO.class);
            ResultData<String> usernameResult = userClient.getUsernameById(comment.getCommentUserId());
            vo.setCommentUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ResultData<Void> isCheckComment(CommentIsCheckDTO isCheckDTO) {
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, isCheckDTO.getId()).or().eq(Comment::getParentId, isCheckDTO.getId());
        int updateCount = commentMapper.update(new Comment().setId(isCheckDTO.getId()).setIsCheck(isCheckDTO.getIsCheck()), wrapper);
        if (updateCount > 0) {
            // 同步redis评论数量
            // 如果是文章评论，则改变redis中文章数量
            // 1.查询评论所在的文章id
            Integer articleId = commentMapper
                    .selectOne(
                            new LambdaQueryWrapper<Comment>()
                                    .eq(Comment::getId, isCheckDTO.getId())
                                    .eq(Comment::getType, CommentEnum.COMMENT_TYPE_ARTICLE.getType())).getTypeId();
            // 2.修改redis数量
            if (Objects.equals(isCheckDTO.getIsCheck(), SQLConst.COMMENT_IS_CHECK)) {
                myRedisCache.incrementCacheMapValue(RedisConstants.ARTICLE_COMMENT_COUNT, articleId.toString(), updateCount);
            } else {
                myRedisCache.incrementCacheMapValue(RedisConstants.ARTICLE_COMMENT_COUNT, articleId.toString(), -updateCount);
            }
            return ResultData.success();
        }

        return ResultData.failure();
    }


    @Override
    public ResultData<Void> deleteComment(Long id) {
        // 是否还有子评论
        if (commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, id)) > 0) {
            return ResultData.failure("该评论还有子评论");
        }
        if (commentMapper.deleteById(id) > 0) {
            // 删除评论的点赞
            likeMapper.delete(new LambdaQueryWrapper<Like>().eq(Like::getType, LikeEnum.LIKE_TYPE_COMMENT.getType()).and(a -> a.in(Like::getTypeId, id)));
            return ResultData.success();
        }
        return ResultData.failure();
    }

    private List<ArticleCommentVO> getChildComment(List<ArticleCommentVO> comments, Long parentId) {
        return comments.stream()
                .filter(comment -> {
                    if (Objects.isNull(comment.getParentId())) {
                        ResultData<Map<String, Object>> userInfoResult = userClient.getUserInfoById(comment.getCommentUserId());
                        Map<String, Object> userInfo = userInfoResult.getData();
                        if (userInfo != null) {
                            comment.setCommentUserNickname((String) userInfo.get("nickname"))
                                    .setCommentUserAvatar((String) userInfo.get("avatar"));
                        }
                        comment.setLikeCount(likeService.getLikeCount(LikeEnum.LIKE_TYPE_COMMENT.getType(), comment.getId()));
                    }
                    return Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(), parentId);
                })
                .peek(comment -> {
                    ResultData<Map<String, Object>> userInfoResult = userClient.getUserInfoById(comment.getCommentUserId());
                    Map<String, Object> userInfo = userInfoResult.getData();
                    if (userInfo != null) {
                        comment.setCommentUserNickname((String) userInfo.get("nickname"))
                                .setCommentUserAvatar((String) userInfo.get("avatar"));
                    }
                    ResultData<String> replyNicknameResult = userClient.getUsernameById(comment.getReplyUserId());
                    comment.setReplyUserNickname(replyNicknameResult.getData() != null ? replyNicknameResult.getData() : "")
                            .setChildComment(getChildComment(comments, comment.getId()))
                            .setLikeCount(likeService.getLikeCount(LikeEnum.LIKE_TYPE_COMMENT.getType(), comment.getId()));
                }).toList();
    }

    // 获取父评论底下的评论数量
    private Long getChildCommentCount(List<ArticleCommentVO> comments, Long parentId) {
        // 递归获取父评论的子评论数
        return comments.stream()
                .filter(comment -> Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(), parentId))
                .peek(comment -> {
                    // 回复子评论的数量
                    Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getReplyId, comment.getId()).eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK));
                    comment.setChildCommentCount(count);
                })
                .mapToLong(comment -> {
                    if (!comment.getChildComment().isEmpty()) {
                        return (1 + getChildCommentCount(comment.getChildComment(), comment.getId()));
                    } else {
                        return 1;
                    }
                })
                .sum();
    }
}
