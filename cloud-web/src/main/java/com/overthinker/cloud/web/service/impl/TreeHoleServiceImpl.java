package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.auth.api.UserClient;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.SearchTreeHoleDTO;
import com.overthinker.cloud.web.entity.DTO.TreeHoleIsCheckDTO;
import com.overthinker.cloud.web.entity.PO.TreeHole;
import com.overthinker.cloud.web.entity.VO.TreeHoleListVO;
import com.overthinker.cloud.web.entity.VO.TreeHoleVO;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.mapper.TreeHoleMapper;
import com.overthinker.cloud.web.service.TreeHoleService;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (TreeHole)表服务实现类
 *
 * @author overH
 * @since 2023-10-30 11:14:14
 */
@Slf4j
@Service("treeHoleService")
@RequiredArgsConstructor
public class TreeHoleServiceImpl extends ServiceImpl<TreeHoleMapper, TreeHole> implements TreeHoleService {

    private final UserClient userClient;
    private final TreeHoleMapper treeHoleMapper;

    @Override
    public ResultData<Void> addTreeHole(String content) {
        if (this.save(new TreeHole().setUserId(SecurityUtils.getUserId()).setContent(content))) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public List<TreeHoleVO> getTreeHole() {
        List<TreeHole> treeHoles = treeHoleMapper.selectList(new LambdaQueryWrapper<TreeHole>().eq(TreeHole::getIsCheck, SQLConst.IS_CHECK_YES));
        if (treeHoles.isEmpty()) return null;
        return treeHoles.stream().map(treeHole -> {
            TreeHoleVO vo = treeHole.copyProperties(TreeHoleVO.class);
            ResultData<String> usernameResult = userClient.getUsernameById(treeHole.getUserId());
            vo.setNickname(usernameResult.getData() != null ? usernameResult.getData() : "");
            ResultData<Map<String, Object>> userInfoResult = userClient.getUserInfoById(treeHole.getUserId());
            Map<String, Object> userInfo = userInfoResult.getData();
            if (userInfo != null) {
                vo.setAvatar((String) userInfo.get("avatar"));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TreeHoleListVO> getBackTreeHoleList(SearchTreeHoleDTO searchDTO) {
        LambdaQueryWrapper<TreeHole> wrapper = new LambdaQueryWrapper<>();
        if (MyStringUtils.isNotNull(searchDTO)) {
            ResultData<List<Long>> userIdsResult = userClient.searchUserIdsByUsername(searchDTO.getUserName());
            List<Long> userIds = userIdsResult.getData() != null ? userIdsResult.getData() : List.of();
            if (!userIds.isEmpty())
                wrapper.in(MyStringUtils.isNotEmpty(searchDTO.getUserName()), TreeHole::getUserId, userIds);
            else
                wrapper.eq(MyStringUtils.isNotNull(searchDTO.getUserName()), TreeHole::getUserId, null);

            wrapper.eq(MyStringUtils.isNotNull(searchDTO.getIsCheck()), TreeHole::getIsCheck, searchDTO.getIsCheck());
            if (MyStringUtils.isNotNull(searchDTO.getStartTime()) && MyStringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(TreeHole::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<TreeHole> treeHoles = treeHoleMapper.selectList(wrapper);
        if (!treeHoles.isEmpty()) {
            return treeHoles.stream().map(treeHole -> {
                TreeHoleListVO vo = treeHole.copyProperties(TreeHoleListVO.class);
                ResultData<String> usernameResult = userClient.getUsernameById(treeHole.getUserId());
                vo.setUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
                return vo;
            }).toList();
        }
        return null;
    }

    @Override
    public ResultData<Void> isCheckTreeHole(TreeHoleIsCheckDTO isCheckDTO) {
        if (treeHoleMapper.updateById(new TreeHole().setId(isCheckDTO.getId()).setIsCheck(isCheckDTO.getIsCheck())) > 0)
            return ResultData.success();

        return ResultData.failure();
    }

    @Override
    public ResultData<Void> deleteTreeHole(List<Long> ids) {
        if (treeHoleMapper.deleteByIds(ids) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }


}
