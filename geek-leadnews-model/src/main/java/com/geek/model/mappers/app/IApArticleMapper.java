package com.geek.model.mappers.app;

import com.geek.model.article.dtos.ArticleHomeDto;
import com.geek.model.article.pojos.ApArticle;
import com.geek.model.user.pojos.ApUserArticleList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IApArticleMapper {

    List<ApArticle> loadArticleListByLocation(@Param("dto") ArticleHomeDto articleHomeDto, @Param("type") Short type);

    /**
     * @param list 用户推荐列表。
     * @return
     */
    List<ApArticle> loadArticleListByIdList(List<ApUserArticleList> list);

    ApArticle selectById(Long id);

    void insert(ApArticle apArticle);

    List<ApArticle> loadLastArticleForHot(String lastDay);

    /**
     * 更新文章数。
     *
     * @param articleId
     * @param viewCount
     * @param collectCount
     * @param commentCount
     * @param likeCount
     * @return
     */
    int updateArticleView(@Param("articleId") Integer articleId, @Param("viewCount") long viewCount, @Param("collectCount") long collectCount, @Param("commentCount") long commentCount, @Param("likeCount") long likeCount);

    /**
     * 依据文章IDS来获取文章详细内容。
     *
     * @param list
     * @return
     */
    List<ApArticle> loadArticleListByIdListV2(List<Integer> list);

    /**
     * 查询。
     *
     * @param apArticle
     * @return
     */
    List<ApArticle> selectList(ApArticle apArticle);

    /**
     * 更新。
     *
     * @param apArticle
     */
    void updateSyncStatus(ApArticle apArticle);

}
