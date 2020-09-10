package com.geek.article.apis;

import com.geek.model.behavior.dtos.LikesBehaviorDto;
import com.geek.model.behavior.dtos.ReadBehaviorDto;
import com.geek.model.behavior.dtos.ShowBehaviorDto;
import com.geek.model.behavior.dtos.UnLikesBehaviorDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IBehaviorControllerApi {

    /**
     * 保存用户点击文章的行为。
     *
     * @param showBehaviorDto
     * @return
     */
    ResponseResult saveShowBehavior(ShowBehaviorDto showBehaviorDto);

    ResponseResult saveLikesBehavior(LikesBehaviorDto likesBehaviorDto);

    ResponseResult saveUnlikesBehavior(UnLikesBehaviorDto unLikesBehaviorDto);

    ResponseResult saveReadBehavior(ReadBehaviorDto readBehaviorDto);

}
