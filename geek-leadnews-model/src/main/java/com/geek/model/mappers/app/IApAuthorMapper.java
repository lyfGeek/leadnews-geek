package com.geek.model.mappers.app;

import com.geek.model.article.pojos.ApAuthor;

import java.util.List;

public interface IApAuthorMapper {

    ApAuthor selectById(Integer id);

    ApAuthor selectByAuthorName(String authorName);

    void insert(ApAuthor apAuthor);

    List<ApAuthor> selectByIds(List<Integer> ids);

}
