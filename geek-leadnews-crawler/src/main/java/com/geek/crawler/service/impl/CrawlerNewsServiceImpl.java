package com.geek.crawler.service.impl;

import com.geek.crawler.service.ICrawlerNewsService;
import com.geek.model.crawler.pojos.ClNews;
import com.geek.model.mappers.crawerls.IClNewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlerNewsServiceImpl implements ICrawlerNewsService {

    @Autowired
    private IClNewsMapper clNewsMapper;

    @Override
    public void saveNews(ClNews clNews) {
        clNewsMapper.insertSelective(clNews);
    }

    @Override
    public void updateNews(ClNews clNews) {
        clNewsMapper.updateByPrimaryKey(clNews);
    }

    @Override
    public void deleteByUrl(String url) {
        clNewsMapper.deleteByUrl(url);
    }

    @Override
    public List<ClNews> queryList(ClNews clNews) {
        return clNewsMapper.selectList(clNews);
    }

}
