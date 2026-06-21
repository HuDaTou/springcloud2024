// package com.overthinker.cloud.web.controller;

// import com.overthinker.cloud.api.apis.media.ENUM.MediaUploadRuleEnum;
// import com.overthinker.cloud.common.core.annotation.LogAnnotation;
// import com.overthinker.cloud.common.core.annotation.LogConst;
// import com.overthinker.cloud.common.core.base.BaseController;
// import com.overthinker.cloud.common.core.resp.ResultData;
// import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
// import com.overthinker.cloud.web.entity.DTO.StationmasterInfoDTO;
// import com.overthinker.cloud.web.entity.DTO.WebsiteInfoDTO;
// import com.overthinker.cloud.web.entity.VO.WebsiteInfoVO;
// import com.overthinker.cloud.web.service.WebsiteInfoService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// /**
//  * 网站信息控制器
//  * <p>
//  * 处理网站基本信息的管理接口，包括头像上传、背景上传、信息查询和修改等操作
//  * </p>
//  *
//  * @author overH
//  * @since 2023-12-27
//  */
// @Tag(name = "网站基本信息")
// @RestController
// @RequestMapping("websiteInfo")
// @RequiredArgsConstructor
// public class WebsiteInfoController extends BaseController {

//     private final WebsiteInfoService websiteInfoService;

//     /**
//      * 上传站长头像
//      *
//      * @param avatar 头像文件
//      * @return 头像访问URL
//      */
//     @PreAuthorize("hasAnyAuthority('blog:update:websiteInfo')")
//     @Operation(summary = "上传站长头像")
//     @Parameter(name = "avatar", description = "头像")
//     @AccessLimit(seconds = 60, maxCount = 5)
//     @LogAnnotation(module = "信息管理", operation = LogConst.UPLOAD_IMAGE)
//     @PostMapping("/upload/avatar")
//     public ResultData<String> upload(@RequestParam("avatar") MultipartFile avatar) {
//         return websiteInfoService.uploadImageInsertOrUpdate(MediaUploadRuleEnum.WEBSITE_INFO_AVATAR, avatar, 0);
//     }

//     /**
//      * 上传资料卡背景
//      *
//      * @param background 背景图文件
//      * @return 背景访问URL
//      */
//     @PreAuthorize("hasAnyAuthority('blog:update:websiteInfo')")
//     @Operation(summary = "上传站长资料卡背景")
//     @Parameter(name = "background", description = "资料卡片背景")
//     @AccessLimit(seconds = 60, maxCount = 5)
//     @LogAnnotation(module = "信息管理", operation = LogConst.UPLOAD_IMAGE)
//     @PostMapping("/upload/background")
//     public ResultData<String> uploadBackground(@RequestParam("background") MultipartFile background) {
//         return websiteInfoService.uploadImageInsertOrUpdate(MediaUploadRuleEnum.WEBSITE_INFO_BACKGROUND, background, 1);
//     }

//     /**
//      * 查询网站信息（后端）
//      *
//      * @return 网站信息
//      */
//     @PreAuthorize("hasAnyAuthority('blog:get:websiteInfo')")
//     @Operation(summary = "查看网站信息-后端")
//     @AccessLimit(seconds = 60, maxCount = 30)
//     @GetMapping
//     public ResultData<WebsiteInfoVO> selectWebsiteInfo() {
//         return ResultData.success(websiteInfoService.selectWebsiteInfo());
//     }

//     /**
//      * 查询网站信息（前端）
//      *
//      * @return 网站信息
//      */
//     @Operation(summary = "查看网站信息-前端")
//     @AccessLimit(seconds = 60, maxCount = 30)
//     @GetMapping("/front")
//     public ResultData<WebsiteInfoVO> selectWebsiteInfoByFront() {
//         return ResultData.success(websiteInfoService.selectWebsiteInfo());
//     }

//     /**
//      * 修改站长信息
//      *
//      * @param stationmasterInfoDTO 站长信息
//      * @return 操作结果
//      */
//     @PreAuthorize("hasAnyAuthority('blog:update:websiteInfo')")
//     @Operation(summary = "修改或创建站长信息")
//     @Parameter(name = "stationmasterInfoDTO", description = "站长信息")
//     @PostMapping("/stationmaster")
//     public ResultData<Void> updateStationmasterInfo(@Valid @RequestBody StationmasterInfoDTO stationmasterInfoDTO) {
//         return websiteInfoService.updateStationmasterInfo(stationmasterInfoDTO);
//     }

//     /**
//      * 修改网站信息
//      *
//      * @param websiteInfoDTO 网站信息
//      * @return 操作结果
//      */
//     @PreAuthorize("hasAnyAuthority('blog:update:websiteInfo')")
//     @Operation(summary = "修改或创建网站信息")
//     @Parameter(name = "websiteInfoDTO", description = "网站信息")
//     @PostMapping("/webInfo")
//     public ResultData<Void> updateWebsiteInfo(@Valid @RequestBody WebsiteInfoDTO websiteInfoDTO) {
//         return websiteInfoService.updateWebsiteInfo(websiteInfoDTO);
//     }
// }