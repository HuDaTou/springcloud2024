package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.SearchTreeHoleDTO;
import com.overthinker.cloud.web.entity.DTO.TreeHoleIsCheckDTO;
import com.overthinker.cloud.web.entity.PO.TreeHole;
import com.overthinker.cloud.web.entity.VO.TreeHoleListVO;
import com.overthinker.cloud.web.entity.VO.TreeHoleVO;

import java.util.List;


/**
 * (TreeHole)表服务接口
 *
 * @author overH
 * @since 2023-10-30 11:14:14
 */
public interface TreeHoleService extends IService<TreeHole> {
    /**
     * 新增树洞
     *
     * @param content 树洞内容
     * @return 是否成功
     */
    ResultData<Void> addTreeHole(String content);

    /**
     * 查看树洞
     *
     * @return 树洞列表
     */
    List<TreeHoleVO> getTreeHole();

    /**
     * 后台树洞列表
     *
     * @return 结果
     */
    List<TreeHoleListVO> getBackTreeHoleList(SearchTreeHoleDTO searchDTO);

    /**
     * 是否通过树洞
     *
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResultData<Void> isCheckTreeHole(TreeHoleIsCheckDTO isCheckDTO);

    /**
     * 删除树洞
     *
     * @param ids id 列表
     * @return 是否成功
     */
    ResultData<Void> deleteTreeHole(List<Long> ids);
}
