package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.Mark;
import com.example.entity.ResponseResult;
import com.example.entity.Video;
import com.example.service.VideoService;
import io.jsonwebtoken.lang.Objects;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api
@Validated
@RestController
@RequestMapping("/video")
public class VideoController {
    @Resource
    private VideoService videoService;

    //    @PreAuthorize(value = "hasAuthority('sys:get')")
    @GetMapping("/findVideosByPages/{pageNum}")
    public ResponseResult<IPage<Video>> findVideosByPages(@PathVariable(required = false) Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        if (pageNum == null)
            pageNum = 1;
        IPage<Video> page = videoService.findVideosByPages(pageNum, pageSize);
        return new ResponseResult<>(200, "操作成功", page);
    }

    @GetMapping("/findAllVideos")
    public ResponseResult<List<Video>> findAllVideos() {
        List<Video> allVideos = videoService.findAllVideos();
        return new ResponseResult<>(200, "操作成功", allVideos);
    }

    @GetMapping("/findVideosByTopicId/{topicId}")
    public ResponseResult<List<Video>> findVideosByTopicId(@PathVariable Integer topicId) {
        List<Video> list = videoService.findVideosByTopicId(topicId);
        return new ResponseResult<>(200, "操作成功", list);
    }

    @GetMapping("/findVideoByBvid/{bvid}")
    public ResponseResult<Video> findVideoByBvid(@PathVariable String bvid) {
        Video video = videoService.findVideoByBvid(bvid);
        return new ResponseResult<>(200, "操作成功", video);
    }

    @GetMapping("/findNotMarkedBvidListWithTopicId/{topicId}")
    public ResponseResult<List<Object>> findNotMarkedBvidListWithTopicId(@PathVariable Integer topicId) {
        List<Object> list = videoService.findNotMarkedBvidListWithTopicId(topicId);
        return new ResponseResult<>(200, "操作成功", list);
    }

    @PostMapping("/findUnLabeledVediosByBvidList")
    public ResponseResult<List<Video>> findUnLabeledVediosByBvidList(@RequestBody List<String> bvidList) {
        List<Video> list = new ArrayList<>();
        if(!bvidList.isEmpty())
            list = videoService.findUnLabeledVediosByBvidList(bvidList);
        return new ResponseResult<>(200, "操作成功", list);
    }

    @PutMapping("/setMarkByBvid")
    public ResponseResult<Map<String, Integer>> setMarkByBvid(@RequestBody Mark mark) {
        int res = videoService.setMarkByBvid(mark);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @GetMapping("/setMarkMaskByBvid/{bvid}/{mask}")
    public ResponseResult<Map<String, Integer>> setMarkMaskByBvid(@PathVariable String bvid, @PathVariable Integer mask) {
        int res = videoService.setMarkMaskByBvid(bvid, mask);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

    @GetMapping("/setLabelByBvid/{bvid}/{label}")
    public ResponseResult<Map<String, Integer>> setLabelByBvid(@PathVariable String bvid, @PathVariable Integer label) {
        int res = videoService.setLabelByBvid(bvid, label);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(200, "操作成功", map);
    }

}
