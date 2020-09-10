package com.geek.admin.service.test;

import com.geek.admin.service.IReviewCrawlerArticleService;
import com.geek.admin.service.IReviewMediaArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewArticleTest {

    @Autowired
    private IReviewMediaArticleService reviewMediaArticleService;
    @Autowired
    private IReviewCrawlerArticleService reviewCrawlerArticleService;

    @Test
    public void testReview() {
        reviewMediaArticleService.autoReviewArticleByMedia(4101);
    }

    @Test
    public void testReviewCraeler() throws Exception {
        reviewCrawlerArticleService.autoReviewArticleByCrawler(35181);
    }
}
