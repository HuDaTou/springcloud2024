package com.overthinker.cloud.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.overthinker.cloud.web.entity.PO.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Article)表数据库访问层
 *
 * @author overH
 * @since 2023-10-15 02:29:11
 */
public interface ArticleMapper extends BaseMapper<Article> {

    //    @Select("SELECT * FROM t_article WHERE status = #{status} and is_deleted = 0 ORDER BY RAND() LIMIT #{limit}")
    List<Article> selectRandomArticles(@Param("status") Integer status, @Param("limit") Integer limit);
}
