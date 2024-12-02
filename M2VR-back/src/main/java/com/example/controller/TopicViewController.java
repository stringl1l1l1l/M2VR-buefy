package com.example.controller;

import com.example.entity.ResponseResult;
import com.example.entity.TopicView;
import com.example.service.TopicViewService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api
@Validated
@RestController
@RequestMapping("/topic")
public class TopicViewController {
    @Resource
    private TopicViewService topicViewService;

    @GetMapping("/findAllTopics")
    public ResponseResult<List<TopicView>> findAllTopics() {
        List<TopicView> list = topicViewService.findAllTopicViews();
        return new ResponseResult<>(200, "操作成功", list);
    }
}
