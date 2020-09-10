package com.geek.migration.service.impl;

import com.geek.migration.service.IApArticleConfigService;
import com.geek.model.article.pojos.ApArticleConfig;
import com.geek.model.mappers.app.IApArticleConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApArticleConfigServiceImpl implements IApArticleConfigService {

    @Autowired
    private IApArticleConfigMapper apArticleConfigMapper;

    @Override
    public List<ApArticleConfig> queryByArticleIds(List<String> ids) {
        return apArticleConfigMapper.selectByArticleIds(ids);
    }

    @Override
    public ApArticleConfig getByArticleId(Integer id) {
        return apArticleConfigMapper.selectByArticleId(id);
    }

}
