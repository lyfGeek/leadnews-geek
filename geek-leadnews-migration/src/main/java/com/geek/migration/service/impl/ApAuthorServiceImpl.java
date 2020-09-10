package com.geek.migration.service.impl;

import com.geek.migration.service.IApAuthorService;
import com.geek.model.article.pojos.ApAuthor;
import com.geek.model.mappers.app.IApAuthorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApAuthorServiceImpl implements IApAuthorService {

    @Autowired
    private IApAuthorMapper apAuthorMapper;

    @Override
    public List<ApAuthor> queryByIds(List<Integer> ids) {
        return apAuthorMapper.selectByIds(ids);
    }

    @Override
    public ApAuthor getById(Long id) {
        if (null != id) {
            return apAuthorMapper.selectById(id.intValue());
        }
        return null;
    }

}
