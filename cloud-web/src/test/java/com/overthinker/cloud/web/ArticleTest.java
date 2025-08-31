package com.overthinker.cloud.web;

import com.overthinker.cloud.web.entity.PO.Article;
import com.overthinker.cloud.web.service.ArticleService;
import com.overthinker.cloud.web.service.impl.ArticleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleTest extends ArticleServiceImpl {
    @Resource
    private ArticleService articleService;

    @Test
    @Operation(summary = "测试文章的mybatis的数据插入", description = "测试数据mybatis数据插入")
    public void test() {
        Article article = Article.builder()
                .id(3L)
                .userId(22L)
                .categoryId(1L)
                .articleCover("https://www.baidu.com")
                .articleTitle("测试文章")
                .articleContent("测试文章内容")
                .articleType(1)
                .isTop(0)
                .status(1)
                .build();
        this.saveOrUpdate(article);

    }
}
