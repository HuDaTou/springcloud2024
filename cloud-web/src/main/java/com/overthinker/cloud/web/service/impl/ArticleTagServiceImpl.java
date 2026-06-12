package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.web.entity.PO.ArticleTag;
import com.overthinker.cloud.web.mapper.ArticleTagMapper;
import com.overthinker.cloud.web.service.ArticleTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (ArticleTag)表服务实现类
 *
 * @author overH
 * @since 2023-10-15 02:29:13
 */
@Slf4j
@Service("articleTagService")
@RequiredArgsConstructor
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
