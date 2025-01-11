package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.PO.Banners;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * (Banners)表服务接口
 *
 * @author overH
 * @since 2024-08-28 09:51:16
 */
public interface BannersService extends IService<Banners> {

    /**
     * 后台获取所有前台首页Banner图片
     * @return banners Entity（List）
     */
    List<Banners> backGetBanners();

    /**
     * 上传Banner图片
     *
     * @param bannerImage Banner图片
     * @return ResultData
     */
    ResultData<Banners> uploadBannerImage(MultipartFile bannerImage);

    /**
     * 删除Banner
     *
     * @param id Banner ID
     * @return ResultData
     */
    ResultData<String> removeBannerById(Long id);

    /**
     * 更新Banner顺序
     * @param updateBannerSortOrderDTO  更新Banner顺序
     * @return ResultData
     */
    ResultData<String> updateSortOrder(List<Banners> updateBannerSortOrderDTO);


    /**
     * 前台查询首页 banner列表
     *
     * @return banners Entity（List）
     */
    List<String> getBanners();
}
