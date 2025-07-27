package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.web.entity.PO.User;
import com.overthinker.cloud.web.mapper.*;
import com.overthinker.cloud.web.service.IpService;
import com.overthinker.cloud.web.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private LikeMapper likeMapper;
    @Resource
    private FavoriteMapper favoriteMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TreeHoleMapper treeHoleMapper;
    @Resource
    private LeaveWordMapper leaveWordMapper;
    @Resource
    private ChatGptMapper chatGptMapper;
    @Resource
    private LinkMapper linkMapper;
    @Resource
    private IpService ipService;

    // ... (other methods remain here) ...

    

    // ... (other methods remain here) ...
}