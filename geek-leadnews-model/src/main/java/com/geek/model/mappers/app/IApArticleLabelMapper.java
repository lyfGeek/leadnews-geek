package com.geek.model.mappers.app;

import com.geek.model.article.pojos.ApArticleLabel;

import java.util.List;

public interface IApArticleLabelMapper {

    int insert(ApArticleLabel record);

    int insertSelective(ApArticleLabel record);

    int updateByPrimaryKeySelective(ApArticleLabel record);

    List<ApArticleLabel> selectList(ApArticleLabel apArticleLabel);

}
