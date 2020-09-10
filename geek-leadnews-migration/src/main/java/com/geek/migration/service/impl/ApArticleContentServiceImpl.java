package com.geek.migration.service.impl;

import com.geek.migration.service.IApArticleContentService;
import com.geek.model.article.pojos.ApArticleContent;
import com.geek.model.mappers.app.IApArticleContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApArticleContentServiceImpl implements IApArticleContentService {

    @Autowired
    private IApArticleContentMapper apArticleContentMapper;

    @Override
    public List<ApArticleContent> queryByArticleIds(List<String> ids) {
        return apArticleContentMapper.selectByArticleIds(ids);
    }

    @Override
    public ApArticleContent getByArticleId(Integer id) {
        return apArticleContentMapper.selectByArticleId(id);
    }

}
