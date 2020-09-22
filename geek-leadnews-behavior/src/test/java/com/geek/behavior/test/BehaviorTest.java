package com.geek.behavior.test;

import com.geek.behavior.BehaviorJarApplication;
import com.geek.behavior.service.IAppShowBehaviorService;
import com.geek.model.article.pojos.ApArticle;
import com.geek.model.behavior.dtos.ShowBehaviorDto;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = BehaviorJarApplication.class)
@RunWith(SpringRunner.class)
public class BehaviorTest {

    @Autowired
    private IAppShowBehaviorService appShowBehaviorService;

//    @Autowired
//    private IAppLikesBehaviorService appLikesBehaviorService;

    @Test
    public void testSave() {
        ApUser user = new ApUser();
        user.setId(1L);
        AppThreadLocalUtils.setUser(user);// 当前线程存入一个用户。

        ShowBehaviorDto showBehaviorDto = new ShowBehaviorDto();
        List<ApArticle> list = new ArrayList<>();
        ApArticle apArticle = new ApArticle();
        apArticle.setId(10130);
        list.add(apArticle);
        showBehaviorDto.setArticleIds(list);
        appShowBehaviorService.saveShowBehavior(showBehaviorDto);
    }

//    @Test
//    public void testLikesSave() {
//        ApUser user = new ApUser();
//        user.setId(1l);
//        AppThreadLocalUtils.setUser(user);
//        LikesBehaviorDto dto = new LikesBehaviorDto();
//        dto.setEntryId(10120);
//        dto.setOperation((short) 0);
//        dto.setType((short) 0);
//        appLikesBehaviorService.saveLikesBehavior(dto);
//    }

}
