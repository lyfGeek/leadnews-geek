package com.geek.article.test;

import com.geek.article.ArticleJarApplication;
import com.geek.article.service.IAppArticleService;
import com.geek.common.article.constans.ArticleConstant;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = ArticleJarApplication.class)
@RunWith(SpringRunner.class)
public class ArticleTest {

    @Autowired
    private IAppArticleService appArticleService;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testArticle() {
        ApUser apUser = new ApUser();
        apUser.setId(2104L);
        AppThreadLocalUtils.setUser(apUser);
//        ArticleHomeDto dto = new ArticleHomeDto();
//        dto.setTag("3");
        ResponseResult responseResult = appArticleService.load(null, ArticleConstant.LOADTYPE_LOAD_MORE);
        System.out.println(responseResult.getData());

//        ResponseResult responseResult = appArticleService.load(null, ArticleConstant.LOADTYPE_LOAD_MORE);
//        System.out.println("responseResult = " + responseResult);
//        System.out.println(responseResult.getData());
    }

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("name", "geek");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }

}
