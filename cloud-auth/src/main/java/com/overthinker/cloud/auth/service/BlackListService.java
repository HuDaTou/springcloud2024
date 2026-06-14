package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.api.apis.auth.dto.AddBlackListRequest;
import com.overthinker.cloud.api.apis.auth.dto.BlackListCheckResponse;
import com.overthinker.cloud.auth.entity.DTO.AddBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.SearchBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.UpdateBlackListDTO;
import com.overthinker.cloud.auth.entity.PO.BlackList;
import com.overthinker.cloud.auth.entity.VO.BlackListVO;
import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.stereotype.Service;


import java.util.List;


/**
 * (BlackList)表服务接口
 *
 * @author overH
 * @since 2024-09-05 16:13:20
 */
@Service

public interface BlackListService extends IService<BlackList> {

    /**
     * 新增数据
     *
     * @param addBlackListDTO 新增对象
     * @return 新增结果
     */
    ResultData<Void> addBlackList(AddBlackListDTO addBlackListDTO);

    /**
     * 获取黑名单
     *
     * @return 黑名单
     */
    List<BlackListVO> getBlackList(SearchBlackListDTO searchBlackListDTO);

    /**
     * 修改数据
     *
     * @param updateBlackListDTO 修改对象
     * @return 修改结果
     */
    ResultData<Void> updateBlackList(UpdateBlackListDTO updateBlackListDTO);

    /**
     * 删除黑名单
     *
     * @param ids 黑名单id
     * @return 是否成功
     */
    ResultData<Void> deleteBlackList(List<Long> ids);

    /**
     * 检查IP或用户是否在黑名单中
     *
     * @param ip    IP地址
     * @param userId 用户ID
     * @return 检查结果
     */
    BlackListCheckResponse checkBlacklist(String ip, Long userId);

    /**
     * 手动解除黑名单封禁
     *
     * @param ip    IP地址
     * @param userId 用户ID
     */
    void expireBlacklist(String ip, Long userId);

    /**
     * 内部接口添加黑名单
     *
     * @param request 添加请求
     */
    void addBlacklistInternal(AddBlackListRequest request);
}
