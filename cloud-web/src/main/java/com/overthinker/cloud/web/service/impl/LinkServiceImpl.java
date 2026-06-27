package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.auth.api.EmailClient;
import com.overthinker.cloud.api.apis.auth.api.UserClient;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.LinkDTO;
import com.overthinker.cloud.web.entity.DTO.LinkIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLinkDTO;
import com.overthinker.cloud.web.entity.PO.Link;
import com.overthinker.cloud.web.entity.VO.LinkListVO;
import com.overthinker.cloud.web.entity.VO.LinkVO;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.enums.MailboxAlertsEnum;
import com.overthinker.cloud.web.mapper.LinkMapper;
import com.overthinker.cloud.web.service.LinkService;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import com.overthinker.cloud.web.utils.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * (Link)表服务实现类
 *
 * @author overH
 * @since 2023-11-14 08:43:35
 */
@Slf4j
@Service("linkService")
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    private final LinkMapper linkMapper;
    private final EmailClient emailClient;
    private final UserClient userClient;
    private final MyRedisCache myRedisCache;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${mail.apply-notice}")
    private Boolean applyNotice;

    @Value("${mail.pass-notice}")
    private Boolean passNotice;

    @Override
    public ResultData<Void> applyLink(LinkDTO linkDTO) {
        Link link = linkDTO.copyProperties(Link.class);
        link.setUserId(SecurityUtils.getUserId());
        // 1.数据库添加
        if (this.save(link)) {
            Map<String, Object> content = new HashMap<>();
            content.put("name", link.getName());
            content.put("url", link.getUrl());
            content.put("description", link.getDescription());
            content.put("background", link.getBackground());
            content.put("linkEmail", link.getEmail());
            content.put("linkId", link.getId());

            if (applyNotice) {
                emailClient.sendEmailNotification(email, MailboxAlertsEnum.FRIEND_LINK_APPLICATION.getCodeStr(), content);
            }
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public List<LinkVO> getLinkList() {
        List<Link> links = linkMapper.selectList(new LambdaQueryWrapper<Link>().eq(Link::getIsCheck, SQLConst.STATUS_PUBLIC));

        return links.stream().map(link -> {
            LinkVO vo = link.copyProperties(LinkVO.class);
            ResultData<Map<String, Object>> userInfoResult = userClient.getUserInfoById(link.getUserId());
            Map<String, Object> userInfo = userInfoResult.getData();
            if (userInfo != null) {
                vo.setAvatar((String) userInfo.get("avatar"));
            }
            return vo;
        }).toList();
    }

    @Override
    public List<LinkListVO> getBackLinkList(SearchLinkDTO searchDTO) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        if (MyStringUtils.isNotNull(searchDTO)) {
            ResultData<List<Long>> userIdsResult = userClient.searchUserIdsByUsername(searchDTO.getUserName());
            List<Long> userIds = userIdsResult.getData() != null ? userIdsResult.getData() : List.of();
            if (!userIds.isEmpty())
                wrapper.in(MyStringUtils.isNotEmpty(searchDTO.getUserName()), Link::getUserId, userIds);
            else
                wrapper.eq(MyStringUtils.isNotNull(searchDTO.getUserName()), Link::getUserId, null);

            wrapper.like(MyStringUtils.isNotEmpty(searchDTO.getName()), Link::getName, searchDTO.getName())
                    .eq(MyStringUtils.isNotNull(searchDTO.getIsCheck()), Link::getIsCheck, searchDTO.getIsCheck());
            if (MyStringUtils.isNotNull(searchDTO.getStartTime()) && MyStringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(Link::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<Link> links = linkMapper.selectList(wrapper);
        if (!links.isEmpty()) {
            return links.stream().map(link -> {
                LinkListVO vo = link.copyProperties(LinkListVO.class);
                ResultData<String> usernameResult = userClient.getUsernameById(link.getUserId());
                vo.setUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
                return vo;
            }).toList();
        }
        return null;
    }

    @Override
    public ResultData<Void> isCheckLink(LinkIsCheckDTO isCheckDTO) {
        if (linkMapper.updateById(new Link().setId(isCheckDTO.getId()).setIsCheck(isCheckDTO.getIsCheck())) > 0) {
            // 修改成功，发送邮件
            if (Objects.equals(isCheckDTO.getIsCheck(), SQLConst.STATUS_PUBLIC)) {
                if (passNotice) {
                    emailClient.sendEmailNotification(linkMapper.selectById(isCheckDTO.getId()).getEmail(), MailboxAlertsEnum.FRIEND_LINK_APPLICATION_PASS.getCodeStr(), null);
                    return ResultData.success(null, "操作成功，已发送通知邮件");
                }
            }
            return ResultData.success(null, "操作成功");
        }

        return ResultData.failure();
    }

    @Override
    public ResultData<Void> deleteLink(List<Long> ids) {
        if (linkMapper.deleteByIds(ids) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public String emailApplyLink(String verifyCode, HttpServletResponse response) {
        if (myRedisCache.isHasKey(RedisConstants.EMAIL_VERIFICATION_LINK + verifyCode)) {
            // 通过该友链
            String linkIdAndEmail = myRedisCache.getCacheObject(RedisConstants.EMAIL_VERIFICATION_LINK + verifyCode);
            // 空格切分
            String[] split = linkIdAndEmail.split(" ");
            if (linkMapper.updateById(new Link().setId(Long.valueOf(split[0])).setIsCheck(SQLConst.IS_CHECK_YES)) > 0) {
                // 清除
                myRedisCache.deleteObject(RedisConstants.EMAIL_VERIFICATION_LINK + verifyCode);
                if (passNotice) {
                    emailClient.sendEmailNotification(split[1], MailboxAlertsEnum.FRIEND_LINK_APPLICATION_PASS.getCodeStr(), null);
                    return WebUtil.renderString(response, "操作成功，已发送通知邮件");
                }
                return WebUtil.renderString(response, "操作成功");
            }
        }
        return WebUtil.renderString(response, "操作失败，请重试");
    }
}
