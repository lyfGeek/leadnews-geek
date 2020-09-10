package com.geek.crawler.test;

import com.geek.common.kafka.KafkaSender;
import com.geek.common.kafka.messages.SubmitArticleAuthMessage;
import com.geek.model.mess.admin.SubmitArticleAuto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JobTest {

    @Autowired
    KafkaSender kafkaSender;

    @Test
    public void test() {
        SubmitArticleAuto submitArticleAuto = new SubmitArticleAuto();
        submitArticleAuto.setArticleId(35182);
        submitArticleAuto.setType(SubmitArticleAuto.ArticleType.CRAWLER);
        SubmitArticleAuthMessage authMessage = new SubmitArticleAuthMessage();
        authMessage.setData(submitArticleAuto);
        kafkaSender.sendSubmitArticleAuthMessage(authMessage);
    }
}
