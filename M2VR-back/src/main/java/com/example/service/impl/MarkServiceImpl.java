package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.Mark;
import com.example.mapper.MarkMapper;
import com.example.service.MarkService;
import com.example.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service("markService")
public class MarkServiceImpl implements MarkService {
    @Resource
    private VideoService videoService;

    @Resource
    private MarkMapper markMapper;

    @Override
    public List<Mark> findAllMarks() {
        return markMapper.selectList(null);
    }

    @Override
    public List<Mark> findMarksByTopicId(int topicId) {
        return markMapper.selectList(new LambdaQueryWrapper<Mark>().eq(Mark::getTopicId, topicId));
    }

    @Override
    public List<Mark> findMarksByBegin(String begin) {
        return markMapper.selectList(new LambdaQueryWrapper<Mark>().eq(Mark::getBegin, begin));
    }

    @Override
    public List<Object> findTodoBvidList(String begin, int topicId) {
        List<Object> all = videoService.findNotMarkedBvidListWithTopicId(topicId);
        List<Object> done = markMapper.selectObjs(new LambdaQueryWrapper<Mark>()
                .select(Mark::getEnd)
                .eq(Mark::getBegin, begin));
        if (Objects.isNull(done))
            return all;
        return all.stream()
                .filter(item -> !Objects.equals(item, begin) && !done.contains(item))
                .collect(Collectors.toList());
    }

    @Override
    public int insertMark(Mark mark) {
        if (Objects.equals(mark.getBegin(), mark.getEnd()))
            return 0;

        Mark markChecked = markMapper.selectOne(new LambdaQueryWrapper<Mark>()
                .eq(Mark::getBegin, mark.getBegin())
                .eq(Mark::getEnd, mark.getEnd())
        );
        if (!Objects.isNull(markChecked))
            return 0;

        Mark symmetricMark = new Mark(mark.getId(), mark.getEnd(), mark.getBegin(), mark.getMark(), mark.getTopicId());
        return markMapper.insert(mark) + markMapper.insert(symmetricMark);
    }

    public int insertMarkWithOverride(Mark mark) {
        markMapper.selectOne(new LambdaQueryWrapper<Mark>());
        return markMapper.insert(mark);
    }

}
