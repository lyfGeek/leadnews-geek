package com.geek.migration.service;

import com.geek.model.article.pojos.ApAuthor;

import java.util.List;

public interface IApAuthorService {

    List<ApAuthor> queryByIds(List<Integer> ids);

    ApAuthor getById(Long id);

}
