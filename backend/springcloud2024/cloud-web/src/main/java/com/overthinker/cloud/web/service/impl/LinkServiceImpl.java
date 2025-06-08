package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.LinkDTO;
import com.overthinker.cloud.web.entity.DTO.LinkIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLinkDTO;
import com.overthinker.cloud.web.entity.PO.Link;
import com.overthinker.cloud.web.entity.PO.User;
import com.overthinker.cloud.web.entity.VO.LinkListVO;
import com.overthinker.cloud.web.entity.VO.LinkVO;
import com.overthinker.cloud.web.entity.constants.RedisConst;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.enums.MailboxAlertsEnum;
import com.overthinker.cloud.web.mapper.LinkMapper;
import com.overthinker.cloud.web.mapper.UserMapper;
import com.overthinker.cloud.web.service.LinkService;
import com.overthinker.cloud.web.service.PublicService;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import com.overthinker.cloud.web.utils.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (Link)表服务实现类
 *
 * @author overH
 * @since 2023-11-14 08:43:35
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Resource
    private LinkMapper linkMapper;

    @Resource
    private PublicService publicService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyRedisCache myRedisCache;

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
                // 2.向站长发送邮件
                publicService.sendEmail(MailboxAlertsEnum.FRIEND_LINK_APPLICATION.getCodeStr(), email, content);
            }
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public List<LinkVO> getLinkList() {
        List<Link> links = linkMapper.selectList(new LambdaQueryWrapper<Link>().eq(Link::getIsCheck, SQLConst.STATUS_PUBLIC));

        return links.stream().map(link -> link.copyProperties(LinkVO.class, v -> v.setAvatar(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, link.getUserId())).getAvatar()))).toList();
    }

    @Override
    public List<LinkListVO> getBackLinkList(SearchLinkDTO searchDTO) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getUserName()), Link::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getUserName()), Link::getUserId, null);

            wrapper.like(StringUtils.isNotEmpty(searchDTO.getName()), Link::getName, searchDTO.getName())
                    .eq(StringUtils.isNotNull(searchDTO.getIsCheck()), Link::getIsCheck, searchDTO.getIsCheck());
            if (StringUtils.isNotNull(searchDTO.getStartTime()) && StringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(Link::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<Link> links = linkMapper.selectList(wrapper);
        if (!links.isEmpty()) {
            return links.stream().map(link -> link.copyProperties(LinkListVO.class,
                    v -> v.setUserName(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, link.getUserId()))
                            .getUsername()))).toList();
        }
        return null;
    }

    @Override
    public ResultData<Void> isCheckLink(LinkIsCheckDTO isCheckDTO) {
        if (linkMapper.updateById(Link.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build()) > 0) {
            // 修改成功，发送邮件
            if (Objects.equals(isCheckDTO.getIsCheck(), SQLConst.STATUS_PUBLIC)) {
                if (passNotice) {
                    publicService.sendEmail(MailboxAlertsEnum.FRIEND_LINK_APPLICATION_PASS.getCodeStr(), linkMapper.selectById(isCheckDTO.getId()).getEmail(), null);
                    return ResultData.success(null, "操作成功，已发送通知邮件");
                }
            }
            return ResultData.success(null, "操作成功");
        }

        return ResultData.failure();
    }

    @Override
    public ResultData<Void> deleteLink(List<Long> ids) {
        if (linkMapper.deleteBatchIds(ids) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public String emailApplyLink(String verifyCode, HttpServletResponse response) {
        if (myRedisCache.isHasKey(RedisConst.EMAIL_VERIFICATION_LINK + verifyCode)) {
            // 通过该友链
            String linkIdAndEmail = myRedisCache.getCacheObject(RedisConst.EMAIL_VERIFICATION_LINK + verifyCode);
            // 空格切分
            String[] split = linkIdAndEmail.split(" ");
            if (linkMapper.updateById(Link.builder().id(Long.valueOf(split[0])).isCheck(SQLConst.IS_CHECK_YES).build()) > 0) {
                // 清除
                myRedisCache.deleteObject(RedisConst.EMAIL_VERIFICATION_LINK + verifyCode);
                if (passNotice) {
                    // 修改成功，发送邮件
                    publicService.sendEmail(MailboxAlertsEnum.FRIEND_LINK_APPLICATION_PASS.getCodeStr(), split[1], null);
                    return WebUtil.renderString(response, "操作成功，已发送通知邮件");
                }
                return WebUtil.renderString(response, "操作成功");
            }
        }
        return WebUtil.renderString(response, "操作失败，请重试");
    }
}
