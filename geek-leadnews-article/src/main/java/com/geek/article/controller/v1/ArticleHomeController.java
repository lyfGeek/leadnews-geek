package com.geek.article.controller.v1;

import com.geek.article.apis.IArticleHomeControllerApi;
import com.geek.article.service.IAppArticleService;
import com.geek.common.article.constans.ArticleConstant;
import com.geek.model.article.dtos.ArticleHomeDto;
import com.geek.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController implements IArticleHomeControllerApi {

    @Autowired
    private IAppArticleService appArticleService;

    /**
     * 加载文章首页。
     *
     * @param aticleHomeDto
     * @return
     */
    @Override
    @RequestMapping("/load")
    public ResponseResult load(ArticleHomeDto articleHomeDto) {
        return appArticleService.load(articleHomeDto, ArticleConstant.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载更多。
     *
     * @param articleHomeDto
     * @return
     */
    @Override
    @GetMapping("/loadmore")
    public ResponseResult loadMore(ArticleHomeDto articleHomeDto) {
        return appArticleService.load(articleHomeDto, ArticleConstant.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载最新。
     *
     * @param articleHomeDto
     * @return
     */
    @Override
    @GetMapping("/loadnew")
    public ResponseResult loadNew(ArticleHomeDto articleHomeDto) {
        return appArticleService.load(articleHomeDto, ArticleConstant.LOADTYPE_LOAD_NEW);
    }

}
