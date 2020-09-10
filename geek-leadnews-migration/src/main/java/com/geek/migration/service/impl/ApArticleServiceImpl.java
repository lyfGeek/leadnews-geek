package com.geek.migration.service.impl;

import com.geek.migration.service.IApArticleService;
import com.geek.model.article.pojos.ApArticle;
import com.geek.model.mappers.app.IApArticleMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ApArticleServiceImpl implements IApArticleService {

    @Autowired
    private IApArticleMapper apArticleMapper;

    @Override
    public ApArticle getById(Long id) {
        return apArticleMapper.selectById(id);
    }

    @Override
    public List<ApArticle> getUnSyncArticleList() {
        ApArticle apArticle = new ApArticle();
        apArticle.setSyncStatus(false);
        return apArticleMapper.selectList(apArticle);
    }

    @Override
    public void updateSyncStatus(ApArticle apArticle) {
        log.info("开始更新数据同步状态,aparticle：{}", apArticle);
        if (null != apArticle) {
            apArticle.setSyncStatus(true);
            apArticleMapper.updateSyncStatus(apArticle);
        }
    }

}
