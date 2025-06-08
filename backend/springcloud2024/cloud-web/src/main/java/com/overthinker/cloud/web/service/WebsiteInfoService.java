package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.StationmasterInfoDTO;
import com.overthinker.cloud.web.entity.DTO.WebsiteInfoDTO;
import com.overthinker.cloud.web.entity.PO.WebsiteInfo;
import com.overthinker.cloud.web.entity.VO.WebsiteInfoVO;
import com.overthinker.cloud.web.entity.enums.UploadEnum;
import org.springframework.web.multipart.MultipartFile;


/**
 * (WebsiteInfo)表服务接口
 *
 * @author overH
 * @since 2023-12-27 14:07:34
 */
public interface WebsiteInfoService extends IService<WebsiteInfo> {

    /**
     * 上传或更新图片
     *
     * @param uploadEnum 目录
     * @param avatar     图片
     * @param type       类型
     * @return 是否成功&图片地址
     */
    ResultData<String> uploadImageInsertOrUpdate(UploadEnum uploadEnum, MultipartFile avatar, Integer type);

    /**
     * 查询网站信息
     *
     * @return 网站信息
     */
    WebsiteInfoVO selectWebsiteInfo();

    /**
     * 修改站长信息
     *
     * @param stationmasterInfoDTO 站长信息
     * @return 是否成功
     */
    ResultData<Void> updateStationmasterInfo(StationmasterInfoDTO stationmasterInfoDTO);

    /**
     * 修改网站信息
     *
     * @param websiteInfoDTO 网站信息
     * @return 是否成功
     */
    ResultData<Void> updateWebsiteInfo(WebsiteInfoDTO websiteInfoDTO);
}
