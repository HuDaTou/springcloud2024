package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.SearchTagDTO;
import com.overthinker.cloud.web.entity.DTO.TagDTO;
import com.overthinker.cloud.web.entity.PO.ArticleTag;
import com.overthinker.cloud.web.entity.PO.Tag;
import com.overthinker.cloud.web.entity.VO.TagVO;
import com.overthinker.cloud.web.entity.constants.FunctionConst;
import com.overthinker.cloud.web.mapper.ArticleTagMapper;
import com.overthinker.cloud.web.mapper.TagMapper;
import com.overthinker.cloud.web.service.TagService;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Tag)表服务实现类
 *
 * @author overH
 * @since 2023-10-15 02:29:14
 */
@Slf4j
@Service("tagService")
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;

    @Override
    public List<TagVO> listAllTag() {
        return this.query().list().stream().map(tag -> tag.copyProperties(TagVO.class, item -> item.setArticleCount(articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tag.getId()))))).toList();
    }

    @Override
    public ResultData<Void> addTag(TagDTO tagDTO) {
        if (this.save(tagDTO.copyProperties(Tag.class))) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public List<TagVO> searchTag(SearchTagDTO searchTagDTO) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(MyStringUtils.isNotEmpty(searchTagDTO.getTagName()), Tag::getTagName, searchTagDTO.getTagName());
        if (MyStringUtils.isNotNull(searchTagDTO.getStartTime()) && MyStringUtils.isNotNull(searchTagDTO.getEndTime()))
            queryWrapper.between(Tag::getCreateTime, searchTagDTO.getStartTime(), searchTagDTO.getEndTime());

        return tagMapper.selectList(queryWrapper)
                .stream()
                .map(tag ->
                        tag.copyProperties(TagVO.class, item ->
                                item.setArticleCount(articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                                        .eq(ArticleTag::getTagId, tag.getId())))))
                .toList();
    }

    @Override
    public TagVO getTagById(Long id) {
        return tagMapper.selectById(id).copyProperties(TagVO.class);
    }

    @Override
    public ResultData<Void> addOrUpdateTag(TagDTO tagDTO) {
        if (this.saveOrUpdate(tagDTO.copyProperties(Tag.class))) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public ResultData<Void> deleteTagByIds(List<Long> ids) {
        // 是否有剩下文章
        Long count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getTagId, ids));
        if (count > 0) return ResultData.failure(FunctionConst.TAG_EXIST_ARTICLE);
        // 执行删除
        if (this.removeByIds(ids)) return ResultData.success();
        return ResultData.failure();
    }
}
