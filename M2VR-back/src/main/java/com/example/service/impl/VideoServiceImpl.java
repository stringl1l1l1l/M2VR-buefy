package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Mark;
import com.example.entity.Video;
import com.example.mapper.VideoMapper;
import com.example.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("videoService")
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;

    @Override
    public IPage<Video> findVideosByPages(int pageNum, int pageSize) {
        return videoMapper.selectPage(new Page<>(pageNum, pageSize), null);
    }

    @Override
    public List<Video> findAllVideos() {
        return videoMapper.selectList(null);
    }

    @Override
    public List<Video> findVideosByTopicId(int topicId) {
        return videoMapper.selectList(new LambdaQueryWrapper<Video>().eq(Video::getTopicId, topicId));
    }

    @Override
    public List<Video> findUnLabeledVediosByBvidList(List<String> bvidList) {
        return videoMapper.selectList(new LambdaQueryWrapper<Video>()
                .eq(Video::getLabel, 0)
                .in(Video::getBvid, bvidList));
    }

    @Override
    public Video findVideoByBvid(String bvid) {
        return videoMapper.selectOne(new LambdaQueryWrapper<Video>().eq(Video::getBvid, bvid));
    }

    @Override
    public List<Object> findNotMarkedBvidListWithTopicId(int topicId) {
        return videoMapper.selectObjs(new LambdaQueryWrapper<Video>()
                .select(Video::getBvid).eq(Video::getMark, false)
                .eq(Video::getTopicId, topicId));
    }

    @Override
    public int setMarkByBvid(Mark mark) {
        return videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getBvid, mark.getBegin())
                .set(Video::getMark, mark.getMark()))
                +
                videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                        .eq(Video::getBvid, mark.getEnd())
                        .set(Video::getMark, mark.getMark()));
    }

    public int setMarkMaskByBvid(String bvid, Integer markMask) {
        return videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getBvid, bvid)
                .set(Video::getMark, markMask));
    }

    @Override
    public int setLabelByBvid(String bvid, Integer label) {
        return videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getBvid, bvid)
                .set(Video::getLabel, label));
    }
}
