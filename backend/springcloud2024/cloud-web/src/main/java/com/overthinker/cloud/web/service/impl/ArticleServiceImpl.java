package com.overthinker.cloud.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.entity.DTO.ArticleDTO;
import com.overthinker.cloud.web.entity.DTO.SearchArticleDTO;
import com.overthinker.cloud.web.entity.PO.*;
import com.overthinker.cloud.web.entity.VO.*;
import com.overthinker.cloud.web.entity.constants.RedisConst;
import com.overthinker.cloud.web.entity.constants.SQLConst;
import com.overthinker.cloud.web.entity.enums.*;
import com.overthinker.cloud.web.exception.BusinessException;
import com.overthinker.cloud.web.exception.FileUploadException;
import com.overthinker.cloud.web.mapper.*;
import com.overthinker.cloud.web.service.*;
import com.overthinker.cloud.web.utils.FileUploadUtils;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (Article)表服务实现类
 *
 * @author overH
 * @since 2023-10-15 02:29:13
 */
@Slf4j
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private LikeService likeService;

    @Resource
    private CommentService commentService;

    @Resource
    private MyRedisCache myRedisCache;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private CommentMapper commentMapper;


    @Override
    public PageVO<List<ArticleVO>> listAllArticle(Integer pageNum, Integer pageSize) {
        boolean hasKey = myRedisCache.isHasKey(RedisConst.ARTICLE_COMMENT_COUNT) && myRedisCache.isHasKey(RedisConst.ARTICLE_FAVORITE_COUNT) && myRedisCache.isHasKey(RedisConst.ARTICLE_LIKE_COUNT);
        // 文章
        Page<Article> page = new Page<>(pageNum, pageSize);
        this.page(page, new LambdaQueryWrapper<Article>().eq(Article::getStatus, SQLConst.PUBLIC_STATUS).orderByDesc(Article::getCreateTime));
        List<Article> list = page.getRecords();
        // 文章分类
        // 1. 优化：使用 Map 存储分类和标签信息，避免 N+1 问题
        Map<Long, String> categoryMap = categoryMapper.selectBatchIds(list.stream().map(Article::getCategoryId).toList())
                .stream().collect(Collectors.toMap(Category::getId, Category::getCategoryName));

        List<ArticleTag> articleTags = articleTagMapper.selectBatchIds(list.stream().map(Article::getId).toList());
        Map<Long, String> tagMap = tagMapper.selectBatchIds(articleTags.stream().map(ArticleTag::getTagId).toList())
                .stream().collect(Collectors.toMap(Tag::getId, Tag::getTagName));

        List<ArticleVO> articleVOS = list.stream().map(article -> {
            ArticleVO articleVO = article.copyProperties(ArticleVO.class);
            // 2. 优化：使用 Map 获取分类和标签信息
            articleVO.setCategoryName(categoryMap.get(article.getCategoryId()));
            articleVO.setTags(articleTags.stream()
                    .filter(at -> Objects.equals(at.getArticleId(), article.getId()))
                    .map(at -> tagMap.get(at.getTagId()))
                    .toList());
            return articleVO;
        }).toList();

        if (hasKey) {
            articleVOS = articleVOS.stream().peek(articleVO -> {
                setArticleCount(articleVO, RedisConst.ARTICLE_FAVORITE_COUNT, CountTypeEnum.FAVORITE);
                setArticleCount(articleVO, RedisConst.ARTICLE_LIKE_COUNT, CountTypeEnum.LIKE);
                setArticleCount(articleVO, RedisConst.ARTICLE_COMMENT_COUNT, CountTypeEnum.COMMENT);
            }).toList();
        }

        return new PageVO<>(articleVOS, page.getTotal());
    }

    private void setArticleCount(@NotNull ArticleVO articleVO, String redisKey, CountTypeEnum articleFieldName) {
        String articleId = articleVO.getId().toString();
        Object countObj = myRedisCache.getCacheMap(redisKey).get(articleId);
        long count = 0L;
        if (countObj != null) {
            count = Long.parseLong(countObj.toString());
        } else {
            // 缓存发布新文章时数量缓存不存在
            myRedisCache.setCacheMap(redisKey, Map.of(articleId, 0));
        }

        if (articleFieldName.equals(CountTypeEnum.FAVORITE)) {
            articleVO.setFavoriteCount(count);
        } else if (articleFieldName.equals(CountTypeEnum.LIKE)) {
            articleVO.setLikeCount(count);
        } else if (articleFieldName.equals(CountTypeEnum.COMMENT)) {
            articleVO.setCommentCount(count);
        }
    }

    @Override
    public List<RecommendArticleVO> listRecommendArticle() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsTop, SQLConst.RECOMMEND_ARTICLE).and(i -> i.eq(Article::getStatus, SQLConst.PUBLIC_STATUS));
        List<Article> articles = articleMapper.selectList(wrapper);
        return articles.stream().map(article -> article.copyProperties(RecommendArticleVO.class)).toList();
    }

    @Override
    public List<RandomArticleVO> listRandomArticle() {
        List<Article> randomArticles = articleMapper.selectRandomArticles(SQLConst.PUBLIC_STATUS, SQLConst.RANDOM_ARTICLE_COUNT);
        return randomArticles.stream()
                .map(article -> article.copyProperties(RandomArticleVO.class))
                .toList();
    }

    @Override
    public ArticleDetailVO getArticleDetail(Integer id) {
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().eq(Article::getStatus, SQLConst.PUBLIC_STATUS).and(i -> i.eq(Article::getId, id)));
        if (StringUtils.isNull(article)) return null;
        // 文章分类
        Category category = categoryMapper.selectById(article.getCategoryId());
        // 文章关系
        List<ArticleTag> articleTags = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, article.getId()));
        // 标签
        List<Tag> tags = tagMapper.selectBatchIds(articleTags.stream().map(ArticleTag::getTagId).toList());
        // 当前文章的上一篇文章与下一篇文章,大于当前文章的最小文章与小于当前文章的最大文章
        LambdaQueryWrapper<Article> preAndNextWrapper = new LambdaQueryWrapper<>();
        preAndNextWrapper.lt(Article::getId, id);
        Article preArticle = articleMapper.selectOne(preAndNextWrapper.orderByDesc(Article::getId).last(SQLConst.LIMIT_ONE_SQL));
        preAndNextWrapper.clear();
        preAndNextWrapper.gt(Article::getId, id);
        Article nextArticle = articleMapper.selectOne(preAndNextWrapper.orderByAsc(Article::getId).last(SQLConst.LIMIT_ONE_SQL));

        return article.copyProperties(ArticleDetailVO.class, vo -> {
            vo.setCategoryName(category.getCategoryName());
            vo.setCategoryId(category.getId());
            vo.setTags(tags.stream().map(tag -> tag.copyProperties(TagVO.class)).toList());
            vo.setCommentCount(commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getTypeId, article.getId()).eq(Comment::getType, CommentEnum.COMMENT_TYPE_ARTICLE.getType())));
            vo.setLikeCount(likeService.count(new LambdaQueryWrapper<Like>().eq(Like::getTypeId, article.getId()).eq(Like::getType, LikeEnum.LIKE_TYPE_ARTICLE.getType())));
            vo.setFavoriteCount(favoriteService.count(new LambdaQueryWrapper<Favorite>().eq(Favorite::getTypeId, article.getId()).eq(Favorite::getType, FavoriteEnum.FAVORITE_TYPE_ARTICLE.getType())));
            vo.setPreArticleId(preArticle == null ? 0 : preArticle.getId());
            vo.setPreArticleTitle(preArticle == null ? "" : preArticle.getArticleTitle());
            vo.setNextArticleId(nextArticle == null ? 0 : nextArticle.getId());
            vo.setNextArticleTitle(nextArticle == null ? "" : nextArticle.getArticleTitle());
        });
    }

    @Override
    public List<RelatedArticleVO> relatedArticleList(Integer categoryId, Integer articleId) {
        // 文章id不等于当前文章id,相关推荐排除自己，5条
        List<Article> articles = articleMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, SQLConst.PUBLIC_STATUS)
                        .and(i -> i.eq(Article::getCategoryId, categoryId))
                        .ne(Article::getId, articleId)
        );
        List<Article> articlesLimit5 = articles.stream().limit(SQLConst.RELATED_ARTICLE_COUNT).toList();
        return articlesLimit5.stream().map(article -> article.copyProperties(RelatedArticleVO.class)).toList();
    }

    @Override
    public List<TimeLineVO> listTimeLine() {
        List<Article> list = this.query().list();
        return list.stream().map(article -> article.copyProperties(TimeLineVO.class)).toList();
    }

    @Override
    public List<CategoryArticleVO> listCategoryArticle(Integer type, Long typeId) {
        List<Article> articles;
        if (type == 1)
            articles = articleMapper.selectList(new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, typeId));
        else if (type == 2) {
            List<Long> articleIds = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, typeId)).stream().map(ArticleTag::getArticleId).toList();
            if (!articleIds.isEmpty()) articles = articleMapper.selectBatchIds(articleIds);
            else articles = List.of();
        } else articles = List.of();

        if (Objects.isNull(articles) || articles.isEmpty()) return null;
        List<ArticleTag> articleTags = articleTagMapper.selectBatchIds(articles.stream().map(Article::getId).toList());
        List<Tag> tags = tagMapper.selectBatchIds(articleTags.stream().map(ArticleTag::getTagId).toList());

        return articles.stream().map(article -> article.copyProperties(CategoryArticleVO.class, item -> {
            item.setCategoryId(articles.stream().filter(art -> Objects.equals(art.getId(), article.getId())).findFirst().orElseThrow().getCategoryId());
            item.setTags(tags.stream().filter(tag -> articleTags.stream().anyMatch(articleTag -> Objects.equals(articleTag.getArticleId(), article.getId()) && Objects.equals(articleTag.getTagId(), tag.getId()))).map(tag -> tag.copyProperties(TagVO.class)).toList());
        })).toList();
    }

    @Override
    public void addVisitCount(Long id) {
        if (myRedisCache.isHasKey(RedisConst.ARTICLE_VISIT_COUNT + id))
            myRedisCache.increment(RedisConst.ARTICLE_VISIT_COUNT + id, 1L);
        else myRedisCache.setCacheObject(RedisConst.ARTICLE_VISIT_COUNT + id, 0);
    }

    @Override
    public ResultData<String> uploadArticleCover(MultipartFile articleCover) {
        try {
            String articleCoverUrl = null;
            try {
                articleCoverUrl = fileUploadUtils.uploadImage(UploadEnum.ARTICLE_COVER, articleCover);
            } catch (FileUploadException e) {
                return ResultData.failure(e.getMessage());
            }
            if (StringUtils.isNotNull(articleCoverUrl))
                return ResultData.success(articleCoverUrl);
            else
                return ResultData.failure("上传格式错误");
        } catch (Exception e) {
            log.error("文章封面上传失败", e);
            return ResultData.failure("文章封面上传失败");
        }
    }

    @Resource
    private ArticleTagService articleTagService;

    @Transactional
    @Override
    public ResultData<Void> publish(ArticleDTO articleDTO) {
//        Article article = articleDTO.asViewObject(Article.class, v -> v.setUserId(SecurityUtils.getUserId()));
        Article article = BeanUtil.copyProperties(articleDTO, Article.class);
        article.setUserId(SecurityUtils.getUserId());


        if (this.saveOrUpdate(article)) {
            // 清除标签关系
            articleTagMapper.deleteById(article.getId());
            // 新增标签关系
            List<ArticleTag> articleTags = articleDTO.getTagId().stream().map(articleTag -> ArticleTag.builder().articleId(article.getId()).tagId(articleTag).build()).toList();
            articleTagService.saveBatch(articleTags);
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Value("${minio.bucketName}")
    private String bucketName;

    @Override
    public ResultData<Void> deleteArticleCover(String articleCoverUrl) {
        try {
            // 提取图片名称
            String articleCoverName = articleCoverUrl.substring(articleCoverUrl.indexOf(bucketName) + bucketName.length());
            fileUploadUtils.deleteFiles(List.of(articleCoverName));
            return ResultData.success();
        } catch (Exception e) {
            log.error("删除文章封面失败", e);
            return ResultData.failure();
        }
    }

    @Override
    public ResultData<String> uploadArticleImage(MultipartFile articleImage) {
        try {
            String url = fileUploadUtils.uploadImage(UploadEnum.ARTICLE_IMAGE, articleImage);
            if (StringUtils.isNotNull(url))
                return ResultData.success(url);
            else
                return ResultData.failure("上传格式错误");
        } catch (Exception e) {
            log.error("文章图片上传失败", e);
        }
        return null;
    }

    @Override
    public List<ArticleListVO> listArticle() {
        List<ArticleListVO> articleListVOS = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .orderByDesc(Article::getCreateTime)).stream().map(article -> article.copyProperties(ArticleListVO.class)).toList();

        if (!articleListVOS.isEmpty()) {
            articleListVOS.forEach(articleListVO -> {
                articleListVO.setCategoryName(categoryMapper.selectById(articleListVO.getCategoryId()).getCategoryName());
                articleListVO.setUserName(userMapper.selectById(articleListVO.getUserId()).getUsername());
                // 查询文章标签
                List<Long> tagIds = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleListVO.getId())).stream().map(ArticleTag::getTagId).toList();
                articleListVO.setTagsName(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getTagName).toList());
            });
            return articleListVOS;
        }
        return null;
    }

    @Override
    public List<ArticleListVO> searchArticle(SearchArticleDTO searchArticleDTO) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotNull(searchArticleDTO.getArticleTitle()), Article::getArticleTitle, searchArticleDTO.getArticleTitle())
                .eq(StringUtils.isNotNull(searchArticleDTO.getCategoryId()), Article::getCategoryId, searchArticleDTO.getCategoryId())
                .eq(StringUtils.isNotNull(searchArticleDTO.getStatus()), Article::getStatus, searchArticleDTO.getStatus())
                .eq(StringUtils.isNotNull(searchArticleDTO.getIsTop()), Article::getIsTop, searchArticleDTO.getIsTop());
        List<ArticleListVO> articleListVOS = articleMapper.selectList(wrapper).stream().map(article -> article.copyProperties(ArticleListVO.class)).toList();
        if (!articleListVOS.isEmpty()) {
            articleListVOS.forEach(articleListVO -> {
                articleListVO.setCategoryName(categoryMapper.selectById(articleListVO.getCategoryId()).getCategoryName());
                articleListVO.setUserName(userMapper.selectById(articleListVO.getUserId()).getUsername());
                // 查询文章标签
                List<Long> tagIds = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleListVO.getId())).stream().map(ArticleTag::getTagId).toList();
                articleListVO.setTagsName(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getTagName).toList());
            });
            return articleListVOS;
        }
        return null;
    }

    @Override
    public ResultData<Void> updateStatus(Long id, Integer status) {
        if (this.update(new LambdaUpdateWrapper<Article>().eq(Article::getId, id).set(Article::getStatus, status))) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public ResultData<Void> updateIsTop(Long id, Integer isTop) {
        if (this.update(new LambdaUpdateWrapper<Article>().eq(Article::getId, id).set(Article::getIsTop, isTop))) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public ArticleDTO getArticleDTO(Long id) {
        ArticleDTO articleDTO = articleMapper.selectById(id).copyProperties(ArticleDTO.class);
        if (StringUtils.isNotNull(articleDTO)) {
            // 查询文章标签
            List<Long> tagIds = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleDTO.getId())).stream().map(ArticleTag::getTagId).toList();
            articleDTO.setTagId(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getId).toList());
            return articleDTO;
        }
        return null;
    }

    @Transactional
    @Override
    public Void deleteArticle(List<Long> ids) {

        if (removeByIds(ids)) {
            // 删除标签关系
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getArticleId, ids));
            // 删除点赞、收藏、评论
            likeMapper.delete(new LambdaQueryWrapper<Like>().eq(Like::getType, LikeEnum.LIKE_TYPE_ARTICLE.getType()).and(a -> a.in(Like::getTypeId, ids)));
            favoriteMapper.delete(new LambdaQueryWrapper<Favorite>().eq(Favorite::getType, FavoriteEnum.FAVORITE_TYPE_ARTICLE.getType()).and(a -> a.in(Favorite::getTypeId, ids)));
            commentMapper.delete(new LambdaQueryWrapper<Comment>().eq(Comment::getType, CommentEnum.COMMENT_TYPE_ARTICLE.getType()).and(a -> a.in(Comment::getTypeId, ids)));

        } else {
            throw new BusinessException(ReturnCodeEnum.RC999);
        }
        return null;

    }

    @Override
    public List<InitSearchTitleVO> initSearchByTitle() {
        List<Article> articles = this.list(new LambdaQueryWrapper<Article>().eq(Article::getStatus, SQLConst.PUBLIC_STATUS));
        Map<Long, String> categoryMap = categoryMapper.selectList(null).stream().collect(Collectors.toMap(Category::getId, Category::getCategoryName));
        if (articles.isEmpty()) {
            return List.of();
        }
        return articles.stream().map(article -> article.copyProperties(InitSearchTitleVO.class, item -> item.setCategoryName(categoryMap.get(article.getCategoryId())))).toList();
    }

    @Override
    public List<HotArticleVO> listHotArticle() {
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<Article>().eq(Article::getStatus, SQLConst.PUBLIC_STATUS).orderByDesc(Article::getVisitCount).last("LIMIT 5"));
        if (!articles.isEmpty()) {
            return articles.stream().map(article -> article.copyProperties(HotArticleVO.class)).toList();
        }
        return List.of();
    }

    @Override
    public List<SearchArticleByContentVO> searchArticleByContent(String keyword) {
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<Article>().like(Article::getArticleContent, keyword).eq(Article::getStatus, SQLConst.PUBLIC_STATUS));
        Map<Long, String> categoryMap = categoryMapper.selectList(null).stream().collect(Collectors.toMap(Category::getId, Category::getCategoryName));
        if (!articles.isEmpty()) {
            List<SearchArticleByContentVO> listVos = articles.stream().map(article -> article.copyProperties(SearchArticleByContentVO.class, vo -> {
                vo.setCategoryName(categoryMap.get(article.getCategoryId()));
            })).toList();
            int index = -1;
            for (SearchArticleByContentVO articleVo : listVos) {
                String content = articleVo.getArticleContent();
                index = content.toLowerCase().indexOf(keyword.toLowerCase());
                if (index != -1) {
                    int end = Math.min(content.length(), index + keyword.length() + 20);
                    String highlighted = keyword + content.substring(index + keyword.length(), end);
                    articleVo.setArticleContent(stripMarkdown(highlighted));
                }
            }
            if (index != -1) {
                return listVos;
            }
        }
        return List.of();
    }

    @Override
    public SseEmitter getArticleCount() {
        return null;
    }

    /**
     * 去掉markdown格式
     *
     * @param markdown markdown
     * @return txt
     */
    private String stripMarkdown(String markdown) {
        return markdown.replaceAll("(?m)^\\s*#.*$", "") // 去掉标题
                .replaceAll("\\*\\*(.*?)\\*\\*", "$1") // 去掉加粗
                .replaceAll("\\*(.*?)\\*", "$1") // 去掉斜体
                .replaceAll("`([^`]*)`", "$1") // 去掉行内代码
                .replaceAll("~~(.*?)~~", "$1") // 去掉删除线
                .replaceAll("\\[(.*?)\\]\\(.*?\\)", "$1") // 去掉链接
                .replaceAll("!\\[.*?\\]\\(.*?\\)", "") // 去掉图片
                .replaceAll(">\\s?", "") // 去掉引用
                .replaceAll("(?m)^\\s*[-*+]\\s+", "") // 去掉无序列表
                .replaceAll("(?m)^\\s*\\d+\\.\\s+", "") // 去掉有序列表
                .replaceAll("\\n", " "); // 去掉换行符
    }
}
