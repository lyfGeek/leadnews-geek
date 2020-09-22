package com.geek.article.controller.v1;

import com.geek.article.service.IApArticleSearchService;
import com.geek.model.article.dtos.UserSearchDto;
import com.geek.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController implements IArticleSearchControllerApi {

    @Autowired
    private IApArticleSearchService apArticleSearchService;

    /**
     * 查询搜索历史。
     *
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/load_search_history")
    public ResponseResult findUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.findUserSearch(dto);
    }

    /**
     * 删除搜索历史。
     *
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/del_search")
    public ResponseResult delUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.delUserSearch(dto);
    }

    /**
     * 清空搜索历史记录。
     *
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/clear_search")
    public ResponseResult clearUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.clearUserSearch(dto);
    }

    /**
     * 今日热词。
     *
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/load_hot_keywords")
    public ResponseResult hotkeywords(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.hotkeywords(dto.getHotDate());
    }

    /**
     * 模糊查询。
     *
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/associate_search")
    public ResponseResult searchAssociate(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.searchAssociate(dto);
    }

    /**
     * es 分页查询。
     *
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/article_search")
    public ResponseResult esArticleSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.esArticleSearch(dto);
    }

}
