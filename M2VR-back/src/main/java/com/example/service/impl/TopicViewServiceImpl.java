package com.example.service.impl;

import com.example.entity.TopicView;
import com.example.mapper.TopicViewMapper;
import com.example.service.TopicViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("topicViewService")
public class TopicViewServiceImpl implements TopicViewService {

    @Resource
    private TopicViewMapper topicViewMapper;

    @Override
    public List<TopicView> findAllTopicViews() {
        return topicViewMapper.selectList(null);
    }
}
