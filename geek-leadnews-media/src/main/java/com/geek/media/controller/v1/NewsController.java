package com.geek.media.controller.v1;

import com.geek.common.media.constant.WmMediaConstant;
import com.geek.media.service.INewsService;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.media.dtos.WmNewsDto;
import com.geek.model.media.dtos.WmNewsPageReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/media/news")
public class NewsController implements INewsControllerApi {

    @Autowired
    private INewsService newsService;

    @Override
    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto dto) {
        return newsService.saveNews(dto, WmMediaConstant.WM_NEWS_SUMMIT_STATUS);
    }

    @Override
    @PostMapping("/save_draft")
    public ResponseResult saveDraftNews(@RequestBody WmNewsDto dto) {
        return newsService.saveNews(dto, WmMediaConstant.WM_NEWS_DRAFT_STATUS);
    }

    @Override
    @PostMapping("/list")
    public ResponseResult listByUser(@RequestBody WmNewsPageReqDto dto) {
        return newsService.listByUser(dto);
    }

    @Override
    @PostMapping("/news")
    public ResponseResult wmNews(@RequestBody WmNewsDto dto) {
        return newsService.findWmNewsById(dto);
    }

    @Override
    @PostMapping("/del_news")
    public ResponseResult delNews(@RequestBody WmNewsDto dto) {
        return newsService.delNews(dto);
    }

}
