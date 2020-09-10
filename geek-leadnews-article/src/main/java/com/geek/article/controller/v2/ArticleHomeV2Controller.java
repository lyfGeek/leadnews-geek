//package com.geek.article.controller.v2;
//
//import com.geek.article.apis.ArticleHomeControllerApi;
//import com.geek.article.service.IAppArticleService;
//import com.geek.common.article.constans.ArticleConstant;
//import com.geek.model.article.dtos.ArticleHomeDto;
//import com.geek.model.common.dtos.ResponseResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v2/article")
//public class ArticleHomeV2Controller implements ArticleHomeControllerApi {
//
//    @Autowired
//    private IAppArticleService appArticleService;
//
//    @Override
//    @PostMapping("/load")
//    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
//        return appArticleService.loadV2(ArticleConstant.LOADTYPE_LOAD_MORE, dto, true);
//    }
//
//    @Override
//    @PostMapping("/loadmore")
//    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
//        return appArticleService.loadV2(ArticleConstant.LOADTYPE_LOAD_MORE, dto, false);
//    }
//
//    @Override
//    @PostMapping("/loadnew")
//    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
//        return appArticleService.loadV2(ArticleConstant.LOADTYPE_LOAD_NEW, dto, false);
//    }
//}
