package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.entity.BasecopyProperties;
import com.overthinker.cloud.common.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (WebsiteInfo)表实体类
 *
 * @author overH
 * @since 2023-12-27 14:07:33
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("sys_website_info")
public class WebsiteInfo extends BaseData implements BasecopyProperties {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "配置ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "站长头像URL", example = "https://example.com/avatar.jpg")
    private String webmasterAvatar;

    @Schema(description = "站长名称", example = "张三")
    private String webmasterName;

    @Schema(description = "站长个性签名", example = "热爱技术，分享生活")
    private String webmasterCopy;

    @Schema(description = "资料卡背景图URL", example = "https://example.com/background.jpg")
    private String webmasterProfileBackground;

    @Schema(description = "Gitee链接", example = "https://gitee.com/username")
    private String giteeLink;

    @Schema(description = "GitHub链接", example = "https://github.com/username")
    private String githubLink;

    @Schema(description = "网站名称", example = "我的个人网站")
    private String websiteName;

    @Schema(description = "顶部通知栏内容", example = "欢迎访问我的网站！")
    private String headerNotification;

    @Schema(description = "侧边栏公告", example = "网站正在进行优化，如有问题请联系管理员")
    private String sidebarAnnouncement;

    @Schema(description = "备案信息", example = "粤ICP备123456号")
    private String recordInfo;

    @Schema(description = "网站开始运行时间", example = "2023-01-01T00:00:00Z")
    private Date startTime;

    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
