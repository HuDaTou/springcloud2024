package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.web.entity.PO.VideoTag;
import com.overthinker.cloud.web.mapper.VideoTagMapper;
import com.overthinker.cloud.web.service.VideoTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (VideoTag)表服务实现类
 *
 * @author overH
 * @since 2023-10-15 02:29:13
 */
@Slf4j
@Service("videoTagService")
@RequiredArgsConstructor
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag> implements VideoTagService {

}
