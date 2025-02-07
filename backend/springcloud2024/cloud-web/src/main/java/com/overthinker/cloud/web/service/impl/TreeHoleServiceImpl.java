package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.DTO.SearchTreeHoleDTO;
import com.overthinker.cloud.web.entity.DTO.TreeHoleIsCheckDTO;
import com.overthinker.cloud.web.entity.PO.TreeHole;
import com.overthinker.cloud.web.entity.PO.User;
import com.overthinker.cloud.web.entity.VO.TreeHoleListVO;
import com.overthinker.cloud.web.entity.VO.TreeHoleVO;
import com.overthinker.cloud.web.mapper.TreeHoleMapper;
import com.overthinker.cloud.web.mapper.UserMapper;
import com.overthinker.cloud.web.service.TreeHoleService;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (TreeHole)表服务实现类
 *
 * @author overH
 * @since 2023-10-30 11:14:14
 */
@Service("treeHoleService")
public class TreeHoleServiceImpl extends ServiceImpl<TreeHoleMapper, TreeHole> implements TreeHoleService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TreeHoleMapper treeHoleMapper;

    @Override
    public ResultData<Void> addTreeHole(String content) {
        if (this.save(TreeHole.builder().userId(SecurityUtils.getUserId()).content(content).build())) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public List<TreeHoleVO> getTreeHole() {
        List<TreeHole> treeHoles = treeHoleMapper.selectList(new LambdaQueryWrapper<TreeHole>().eq(TreeHole::getIsCheck, SQLConst.IS_CHECK_YES));
        if (treeHoles.isEmpty()) return null;
        return treeHoles.stream().map(treeHole -> treeHole.asViewObject(TreeHoleVO.class, treeHoleVO -> {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, treeHole.getUserId()));
            treeHoleVO.setNickname(user.getUsername());
            treeHoleVO.setAvatar(user.getAvatar());
        })).collect(Collectors.toList());
    }

    @Override
    public List<TreeHoleListVO> getBackTreeHoleList(SearchTreeHoleDTO searchDTO) {
        LambdaQueryWrapper<TreeHole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getUserName()), TreeHole::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getUserName()), TreeHole::getUserId, null);

            wrapper.eq(StringUtils.isNotNull(searchDTO.getIsCheck()), TreeHole::getIsCheck, searchDTO.getIsCheck());
            if (StringUtils.isNotNull(searchDTO.getStartTime()) && StringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(TreeHole::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<TreeHole> treeHoles = treeHoleMapper.selectList(wrapper);
        if (!treeHoles.isEmpty()) {
            return treeHoles.stream().map(treeHole -> treeHole.asViewObject(TreeHoleListVO.class,
                    v -> v.setUserName(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, treeHole.getUserId()))
                            .getUsername()))).toList();
        }
        return null;
    }

    @Override
    public ResultData<Void> isCheckTreeHole(TreeHoleIsCheckDTO isCheckDTO) {
        if (treeHoleMapper.updateById(TreeHole.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build()) > 0)
            return ResultData.success();

        return ResultData.failure();
    }

    @Override
    public ResultData<Void> deleteTreeHole(List<Long> ids) {
        if (treeHoleMapper.deleteBatchIds(ids) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }


}
