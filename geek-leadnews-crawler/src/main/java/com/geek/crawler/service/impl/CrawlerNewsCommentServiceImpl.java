package com.geek.crawler.service.impl;

import com.geek.crawler.service.ICrawlerNewsCommentService;
import com.geek.model.crawler.pojos.ClNewsComment;
import com.geek.model.mappers.crawerls.IClNewsCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrawlerNewsCommentServiceImpl implements ICrawlerNewsCommentService {

    @Autowired
    private IClNewsCommentMapper clNewsCommentMapper;

    @Override
    public void saveClNewsComment(ClNewsComment clNewsComment) {
        clNewsCommentMapper.insertSelective(clNewsComment);
    }

}
