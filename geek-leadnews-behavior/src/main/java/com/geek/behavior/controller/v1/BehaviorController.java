//package com.geek.behavior.controller.v1;
//
//import com.geek.article.apis.IBehaviorControllerApi;
//import com.geek.behavior.service.IAppLikesBehaviorService;
//import com.geek.behavior.service.IAppReadBehaviorService;
//import com.geek.behavior.service.IAppShowBehaviorService;
//import com.geek.behavior.service.IAppUnLikesBehaviorService;
//import com.geek.model.behavior.dtos.LikesBehaviorDto;
//import com.geek.model.behavior.dtos.ReadBehaviorDto;
//import com.geek.model.behavior.dtos.ShowBehaviorDto;
//import com.geek.model.behavior.dtos.UnLikesBehaviorDto;
//import com.geek.model.common.dtos.ResponseResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/behavior")
//public class BehaviorController implements IBehaviorControllerApi {
//
//    @Autowired
//    private IAppShowBehaviorService appShowBehaviorService;
//    @Autowired
//    private IAppLikesBehaviorService appLikesBehaviorService;
//    @Autowired
//    private IAppUnLikesBehaviorService appUnLikesBehaviorService;
//    @Autowired
//    private IAppReadBehaviorService appReadBehaviorService;
//
//    @Override
//    @PostMapping("/show_behavior")
//    public ResponseResult saveShowBehavior(@RequestBody ShowBehaviorDto showBehaviorDto) {
//        return appShowBehaviorService.saveShowBehavior(showBehaviorDto);
//    }
//
//    @Override
//    @PostMapping("/like_behavior")
//    public ResponseResult saveLikesBehavior(@RequestBody LikesBehaviorDto likesBehaviorDto) {
//        return appLikesBehaviorService.saveLikesBehavior(likesBehaviorDto);
//    }
//
//    @Override
//    @PostMapping("/unlike_behavior")
//    public ResponseResult saveUnlikesBehavior(@RequestBody UnLikesBehaviorDto unLikesBehaviorDto) {
//        return appUnLikesBehaviorService.saveUnLikesBehavior(unLikesBehaviorDto);
//    }
//
//    @Override
//    @PostMapping("/read_behavior")
//    public ResponseResult saveReadBehavior(@RequestBody ReadBehaviorDto readBehaviorDto) {
//        return appReadBehaviorService.saveReadBehavior(readBehaviorDto);
//    }
//
//}
