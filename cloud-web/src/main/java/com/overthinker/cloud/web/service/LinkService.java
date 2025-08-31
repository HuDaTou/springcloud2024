package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.LinkDTO;
import com.overthinker.cloud.web.entity.DTO.LinkIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLinkDTO;
import com.overthinker.cloud.web.entity.PO.Link;
import com.overthinker.cloud.web.entity.VO.LinkListVO;
import com.overthinker.cloud.web.entity.VO.LinkVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


/**
 * (Link)表服务接口
 *
 * @author overH
 * @since 2023-11-14 08:43:35
 */
public interface LinkService extends IService<Link> {

    /**
     * 申请友链
     *
     * @param linkDTO 友链信息
     * @return 是否成功
     */
    ResultData<Void> applyLink(LinkDTO linkDTO);

    /**
     * 查询通过审核的友链
     */
    List<LinkVO> getLinkList();


    /**
     * 后台友链列表
     *
     * @return 结果
     */
    List<LinkListVO> getBackLinkList(SearchLinkDTO searchDTO);

    /**
     * 是否通过友链
     *
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResultData<Void> isCheckLink(LinkIsCheckDTO isCheckDTO);

    /**
     * 删除友链
     *
     * @param ids id 列表
     * @return 是否成功
     */
    ResultData<Void> deleteLink(List<Long> ids);

    /**
     * 邮箱审核友链申请
     *
     * @param verifyCode 校验码
     * @return 是否成功
     */
    String emailApplyLink(String verifyCode, HttpServletResponse response);
}
