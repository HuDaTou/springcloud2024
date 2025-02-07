package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.constants.RedisConst;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.DTO.CommentIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCommentDTO;
import com.overthinker.cloud.web.entity.DTO.UserCommentDTO;
import com.overthinker.cloud.web.entity.PO.Comment;
import com.overthinker.cloud.web.entity.PO.LeaveWord;
import com.overthinker.cloud.web.entity.PO.Like;
import com.overthinker.cloud.web.entity.PO.User;
import com.overthinker.cloud.web.entity.VO.ArticleCommentVO;
import com.overthinker.cloud.web.entity.VO.CommentListVO;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.entity.enums.CommentEnum;
import com.overthinker.cloud.web.entity.enums.LikeEnum;
import com.overthinker.cloud.web.entity.enums.MailboxAlertsEnum;
import com.overthinker.cloud.web.mapper.CommentMapper;
import com.overthinker.cloud.web.mapper.LeaveWordMapper;
import com.overthinker.cloud.web.mapper.LikeMapper;
import com.overthinker.cloud.web.mapper.UserMapper;
import com.overthinker.cloud.web.service.CommentService;
import com.overthinker.cloud.web.service.LikeService;
import com.overthinker.cloud.web.service.PublicService;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
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
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LikeService likeService;

    @Resource
    private MyRedisCache myRedisCache;

    @Resource
    private LikeMapper likeMapper;

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
        List<ArticleCommentVO> commentsVOS = comments.stream().map(comment -> comment.asViewObject(ArticleCommentVO.class)).toList();
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

    @Resource
    private PublicService publicService;

    @Resource
    private LeaveWordMapper leaveWordMapper;

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
    public ResultData<String> userComment(UserCommentDTO commentDTO) {
        Comment comment = commentDTO.asViewObject(Comment.class, commentDto -> commentDto.setCommentUserId(SecurityUtils.getUserId()));
        if (this.save(comment)) {
            // 判断用是否为第三方登录没有邮箱
            User user = userMapper.selectById(SecurityUtils.getUserId());
            if (StringUtils.isEmpty(user.getEmail())) {
                // 提示绑定邮箱
                return ResultData.success("检测到您尚未绑定邮箱,无法开启邮箱提醒，请先绑定邮箱");
            }
            return this.commentEmailReminder(commentDTO, user, comment);
        }
        return ResultData.failure();
    }

    /**
     * 评论邮箱提醒
     *
     * @param commentDTO 前端DTO
     * @param user       用户
     * @param comment    新增评论消息
     * @return ResultData
     */
    public ResultData<String> commentEmailReminder(UserCommentDTO commentDTO, User user, Comment comment) {
        // 缓存评论数量+1
        myRedisCache.incrementCacheMapValue(RedisConst.ARTICLE_COMMENT_COUNT, commentDTO.getTypeId().toString(), 1);
        // 评论
        if (StringUtils.isNull(commentDTO.getReplyId())) {

            if ((commentDTO.getType() == 1 && !articleEmailNotice) || commentDTO.getType() == 2 && !messageEmailNotice)
                return ResultData.success();

            Map<String, Object> selectWhereMap = new HashMap<>();
            selectWhereMap.put("commentType", commentDTO.getType());
            selectWhereMap.put("commentId", comment.getId());

            // 留言提示对应发布留言的用户
            if (commentDTO.getType() == 1) {
                if (Objects.equals(fromUser, user.getEmail())) return ResultData.success();
                // 发邮箱给站长
                publicService.sendEmail(MailboxAlertsEnum.COMMENT_NOTIFICATION_EMAIL.getCodeStr(), fromUser, selectWhereMap);
            }

            if (commentDTO.getType() == 2) {
                // 查出回复的该留言用户的邮箱
                LeaveWord leaveWord = leaveWordMapper.selectOne(new LambdaQueryWrapper<LeaveWord>().eq(LeaveWord::getId, commentDTO.getTypeId()));
                User replyUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, leaveWord.getUserId()));
                // 用户没绑定邮箱，或者回复的留言是自己
                if (Objects.equals(replyUser.getEmail(), null) || Objects.equals(replyUser.getEmail(), user.getEmail()))
                    return ResultData.success();
                // 发送邮箱给该留言的用户
                publicService.sendEmail(MailboxAlertsEnum.COMMENT_NOTIFICATION_EMAIL.getCodeStr(), replyUser.getEmail(), selectWhereMap);
            }
        }
        // 回复评论
        if (Objects.nonNull(commentDTO.getReplyId())) {
            User replyUser = userMapper.selectById(commentDTO.getReplyUserId());
            if ((commentDTO.getType() == 1 && !articleReplyNotice) || (commentDTO.getType() == 2 && !messageReplyNotice))
                return ResultData.success();

            // 如果用户回复自己并且回复人是站长就无需提醒
            if (Objects.equals(replyUser.getEmail(), user.getEmail()) && Objects.equals(fromUser, user.getEmail())) {
                return ResultData.success();
            }

            Map<String, Object> selectWhereMap = new HashMap<>();
            selectWhereMap.put("commentType", commentDTO.getType());
            selectWhereMap.put("commentId", comment.getId());
            selectWhereMap.put("replyCommentId", commentDTO.getReplyId());

            // 回复人与被回复人不是站长本人的话就发送新增评论邮箱给站长
            if (!Objects.equals(user.getEmail(), fromUser) && !Objects.equals(replyUser.getEmail(), fromUser)) {
                publicService.sendEmail(MailboxAlertsEnum.COMMENT_NOTIFICATION_EMAIL.getCodeStr(), fromUser, selectWhereMap);
            }

            // 回复人不是站长本人并且不是自己回复自己，就发送回复通知
            if (!Objects.equals(user.getEmail(), replyUser.getEmail())) {
                publicService.sendEmail(MailboxAlertsEnum.REPLY_COMMENT_NOTIFICATION_EMAIL.getCodeStr(), replyUser.getEmail(), selectWhereMap);
            }
        }
        return ResultData.success();
    }


    @Override
    public List<CommentListVO> getBackCommentList(SearchCommentDTO searchDTO) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getCommentUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getCommentUserName()), Comment::getCommentUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getCommentUserName()), Comment::getCommentUserId, null);

            wrapper.like(StringUtils.isNotEmpty(searchDTO.getCommentContent()), Comment::getCommentContent, searchDTO.getCommentContent())
                    .eq(StringUtils.isNotNull(searchDTO.getType()), Comment::getType, searchDTO.getType())
                    .eq(StringUtils.isNotNull(searchDTO.getIsCheck()), Comment::getIsCheck, searchDTO.getIsCheck());
        }

        return commentMapper.selectList(wrapper.orderByDesc(Comment::getCreateTime)).stream().map(comment -> comment.asViewObject(CommentListVO.class,
                v -> v.setCommentUserName(userMapper.selectById(comment.getCommentUserId()).getUsername()))).collect(Collectors.toList());
    }

    @Override
    public ResultData<Void> isCheckComment(CommentIsCheckDTO isCheckDTO) {
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, isCheckDTO.getId()).or().eq(Comment::getParentId, isCheckDTO.getId());
        int updateCount = commentMapper.update(Comment.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build(), wrapper);
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
                myRedisCache.incrementCacheMapValue(RedisConst.ARTICLE_COMMENT_COUNT, articleId.toString(), updateCount);
            } else {
                myRedisCache.incrementCacheMapValue(RedisConst.ARTICLE_COMMENT_COUNT, articleId.toString(), -updateCount);
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
                        User user = userMapper.selectById(comment.getCommentUserId());
                        comment.setCommentUserNickname(user.getNickname())
                                .setCommentUserAvatar(user.getAvatar())
                                .setLikeCount(likeService.getLikeCount(LikeEnum.LIKE_TYPE_COMMENT.getType(), comment.getId()));
                    }
                    return Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(), parentId);
                })
                .peek(comment -> {
                    User user = userMapper.selectById(comment.getCommentUserId());
                    comment.setChildComment(getChildComment(comments, comment.getId()))
                            .setCommentUserNickname(user.getNickname())
                            .setCommentUserAvatar(user.getAvatar())
                            .setReplyUserNickname(userMapper.selectById(comment.getReplyUserId()).getNickname())
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
