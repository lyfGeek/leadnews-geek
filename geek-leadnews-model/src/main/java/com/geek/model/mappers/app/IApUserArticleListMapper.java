package com.geek.model.mappers.app;

import com.geek.model.article.dtos.ArticleHomeDto;
import com.geek.model.user.pojos.ApUser;
import com.geek.model.user.pojos.ApUserArticleList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IApUserArticleListMapper {

    List<ApUserArticleList> loadArticleIdListByUser(@Param("user") ApUser user, @Param("dto") ArticleHomeDto dto, @Param("type") Short type);

}
