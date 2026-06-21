package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.media.ENUM.MediaUploadRuleEnum;
import com.overthinker.cloud.api.apis.media.api.MediaClient;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.entity.PO.Banners;
import com.overthinker.cloud.web.entity.constants.RespConst;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.mapper.BannersMapper;
import com.overthinker.cloud.web.service.BannersService;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * (Banners)表服务实现类
 *
 * @author overH
 * @since 2024-08-28 09:51:22
 */
@Slf4j
@Service("bannersService")
@RequiredArgsConstructor
public class BannersServiceImpl extends ServiceImpl<BannersMapper, Banners> implements BannersService {

    private final BannersMapper bannersMapper;
    private final MediaClient mediaClient;

    @Override
    public List<String> getBanners() {
        List<Banners> banners = bannersMapper.selectList(new LambdaQueryWrapper<Banners>().orderByAsc(Banners::getSortOrder));
        if (!banners.isEmpty()) return banners.stream().map(Banners::getPath).toList();
        return List.of();
    }

    @Override
    public List<Banners> backGetBanners() {
        List<Banners> banners = bannersMapper.selectList(new LambdaQueryWrapper<Banners>().orderByAsc(Banners::getSortOrder));
        if (!banners.isEmpty()) {
            return banners;
        }
        return List.of();
    }

    @Override
    public ResultData<Banners> uploadBannerImage(MultipartFile bannerImage) {
        try {
            // 是否到达Banner数量上限
            if (bannersMapper.selectCount(null) >= SQLConst.BANNER_MAX_COUNT) {
                return ResultData.failure(RespConst.BANNER_MAX_COUNT_MSG);
            }

            ResultData<Map<String, Object>> result = mediaClient.uploadFileWithRuleName(
                    SecurityUtils.getUserId(),
                    bannerImage,
                    MediaUploadRuleEnum.UI_BANNERS.name()
            );

            if (result.getCode().equals(ReturnCodeEnum.SUCCESS.getCode()) && result.getData() != null) {
                String bannerUrl = (String) result.getData().get("fileUrl");
                if (MyStringUtils.isNotNull(bannerUrl)) {
                    Banners banner = new Banners().setSize(bannerImage.getSize())
                            .setType(bannerImage.getContentType())
                            .setUserId(SecurityUtils.getUserId())
                            .setSortOrder((int) (bannersMapper.selectCount(null) + 1))
                            .setPath(bannerUrl);
                    bannersMapper.insert(banner);
                    return ResultData.success(banner);
                }
            }
            return ResultData.failure("上传失败");
        } catch (Exception e) {
            log.error(MediaUploadRuleEnum.UI_BANNERS.getDescription() + "上传失败", e);
            return ResultData.failure();
        }
    }

    @Override
    public ResultData<String> removeBannerById(Long id) {
        Banners banner = bannersMapper.selectById(id);
        if (this.removeById(id)) {
            String objectName = extractObjectName(banner.getPath());
            ResultData<Void> result = mediaClient.deleteFile(SecurityUtils.getUserId(), objectName);
            if (result.getCode().equals(ReturnCodeEnum.SUCCESS.getCode())) {
                return ResultData.success("删除成功");
            }
            return ResultData.failure("删除失败：" + result.getMsg());
        }
        return ResultData.failure("删除失败");
    }

    @Override
    public ResultData<String> updateSortOrder(List<Banners> banners) {
        bannersMapper.delete(Wrappers.emptyWrapper());
        for (int i = 0; i < banners.size(); i++) {
            banners.get(i).setSortOrder(i + 1);
            bannersMapper.insert(banners.get(i));
        }
        return ResultData.success();
    }

    private String extractObjectName(String fileUrl) {
        if (MyStringUtils.isNull(fileUrl)) {
            return "";
        }
        try {
            java.net.URI uri = java.net.URI.create(fileUrl);
            String path = uri.getPath();
            if (path != null && path.startsWith("/")) {
                int secondSlash = path.indexOf('/', 1);
                if (secondSlash != -1) {
                    return path.substring(secondSlash + 1);
                }
            }
        } catch (Exception ignored) {
        }
        return fileUrl;
    }
}