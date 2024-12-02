package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Mark;
import com.example.entity.Video;

import java.util.List;

public interface VideoService {
    IPage<Video> findVideosByPages(int pageNum, int pageSize);

    List<Video> findAllVideos();

    List<Video> findVideosByTopicId(int topicId);

    List<Video> findUnLabeledVediosByBvidList(List<String> bvidList);

    Video findVideoByBvid(String bvid);

    List<Object> findNotMarkedBvidListWithTopicId(int topicId);

    int setMarkByBvid(Mark mark);

    int setMarkMaskByBvid(String bvid, Integer markMask);

    int setLabelByBvid(String bvid, Integer label);
}
