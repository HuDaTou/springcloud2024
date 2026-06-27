package com.overthinker.cloud.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.auth.api.EmailClient;
import com.overthinker.cloud.api.apis.auth.api.UserClient;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.LeaveWordIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLeaveWordDTO;
import com.overthinker.cloud.web.entity.PO.*;
import com.overthinker.cloud.web.entity.VO.LeaveWordListVO;
import com.overthinker.cloud.web.entity.VO.LeaveWordVO;
import com.overthinker.cloud.web.entity.constants.FunctionConst;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.enums.CommentEnum;
import com.overthinker.cloud.web.entity.enums.FavoriteEnum;
import com.overthinker.cloud.web.entity.enums.LikeEnum;
import com.overthinker.cloud.web.entity.enums.MailboxAlertsEnum;
import com.overthinker.cloud.web.mapper.*;
import com.overthinker.cloud.web.service.LeaveWordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * (LeaveWord)表服务实现类
 *
 * @author overH
 * @since 2023-11-03 15:01:11
 */
@Slf4j
@Service("leaveWordService")
@RequiredArgsConstructor
public class LeaveWordServiceImpl extends ServiceImpl<LeaveWordMapper, LeaveWord> implements LeaveWordService {

    private final UserClient userClient;
    private final CommentMapper commentMapper;
    private final LikeMapper likeMapper;
    private final FavoriteMapper favoriteMapper;
    private final LeaveWordMapper leaveWordMapper;
    private final EmailClient emailClient;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${mail.message-new-notice}")
    private Boolean messageNewNotice;

    @Override
    public List<LeaveWordVO> getLeaveWordList(String id) {
        return this.query()
                .eq(SQLConst.IS_CHECK, SQLConst.IS_CHECK_YES)
                .eq(id != null, SQLConst.ID, id)
                .orderByDesc(SQLConst.CREATE_TIME)
                .list().stream().map(leaveWord -> {
                    LeaveWordVO vo = leaveWord.copyProperties(LeaveWordVO.class);
                    ResultData<Map<String, Object>> userInfoResult = userClient.getUserInfoById(leaveWord.getUserId());
                    Map<String, Object> userInfo = userInfoResult.getData();
                    if (userInfo != null) {
                        vo.setNickname((String) userInfo.get("nickname"));
                        vo.setAvatar((String) userInfo.get("avatar"));
                    }
                    vo.setCommentCount(commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getType, CommentEnum.COMMENT_TYPE_LEAVE_WORD.getType()).eq(Comment::getIsCheck, SQLConst.IS_CHECK_YES).eq(Comment::getTypeId, leaveWord.getId())))
                            .setLikeCount(likeMapper.selectCount(new LambdaQueryWrapper<Like>().eq(Like::getType, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType()).eq(Like::getTypeId, leaveWord.getId())))
                            .setFavoriteCount(favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>().eq(Favorite::getType, CommentEnum.COMMENT_TYPE_LEAVE_WORD.getType()).eq(Favorite::getTypeId, leaveWord.getId())));
                    return vo;
                }).toList();
    }

    @Override
    public ResultData<Void> userLeaveWord(String content) {
        String parse = (String) JSON.parse(content);
        if (parse.length() > FunctionConst.LEAVE_WORD_CONTENT_LENGTH) {
            return ResultData.failure("留言内容过长");
        }
        LeaveWord build = new LeaveWord().setContent(parse)
                .setUserId(SecurityUtils.getUserId());

        if (this.save(build)) {
            ResultData<String> userEmailResult = userClient.getEmailById(SecurityUtils.getUserId());
            String userEmail = userEmailResult.getData();
            if (Objects.equals(userEmail, email) || !messageNewNotice) return ResultData.success();

            Map<String, Object> map = new HashMap<>();
            map.put("messageId", build.getId());
            emailClient.sendEmailNotification(email, MailboxAlertsEnum.MESSAGE_NOTIFICATION_EMAIL.getCodeStr(), map);

            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public List<LeaveWordListVO> getBackLeaveWordList(SearchLeaveWordDTO searchDTO) {
        LambdaQueryWrapper<LeaveWord> wrapper = new LambdaQueryWrapper<>();
        if (MyStringUtils.isNotNull(searchDTO)) {
            ResultData<List<Long>> userIdsResult = userClient.searchUserIdsByUsername(searchDTO.getUserName());
            List<Long> userIds = userIdsResult.getData() != null ? userIdsResult.getData() : List.of();
            if (!userIds.isEmpty())
                wrapper.in(MyStringUtils.isNotEmpty(searchDTO.getUserName()), LeaveWord::getUserId, userIds);
            else
                wrapper.eq(MyStringUtils.isNotNull(searchDTO.getUserName()), LeaveWord::getUserId, null);

            wrapper.eq(MyStringUtils.isNotNull(searchDTO.getIsCheck()), LeaveWord::getIsCheck, searchDTO.getIsCheck());
            if (MyStringUtils.isNotNull(searchDTO.getStartTime()) && MyStringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(LeaveWord::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<LeaveWord> leaveWords = leaveWordMapper.selectList(wrapper);
        if (!leaveWords.isEmpty()) {
            return leaveWords.stream().map(leaveWord -> {
                LeaveWordListVO vo = leaveWord.copyProperties(LeaveWordListVO.class);
                ResultData<String> usernameResult = userClient.getUsernameById(leaveWord.getUserId());
                vo.setUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
                return vo;
            }).toList();
        }
        return null;
    }

    @Override
    public ResultData<Void> isCheckLeaveWord(LeaveWordIsCheckDTO isCheckDTO) {
        if (leaveWordMapper.updateById(new LeaveWord().setId(isCheckDTO.getId()).setIsCheck(isCheckDTO.getIsCheck())) > 0)
            return ResultData.success();

        return ResultData.failure();
    }

    @Transactional
    @Override
    public ResultData<Void> deleteLeaveWord(List<Long> ids) {
        if (leaveWordMapper.deleteByIds(ids) > 0) {
            // 删除点赞、收藏、评论
            likeMapper.delete(new LambdaQueryWrapper<Like>().eq(Like::getType, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType()).and(a -> a.in(Like::getTypeId, ids)));
            favoriteMapper.delete(new LambdaQueryWrapper<Favorite>().eq(Favorite::getType, FavoriteEnum.FAVORITE_TYPE_LEAVE_WORD.getType()).and(a -> a.in(Favorite::getTypeId, ids)));
            commentMapper.delete(new LambdaQueryWrapper<Comment>().eq(Comment::getType, CommentEnum.COMMENT_TYPE_LEAVE_WORD.getType()).and(a -> a.in(Comment::getTypeId, ids)));
            return ResultData.success();
        }
        return ResultData.failure();
    }
}
