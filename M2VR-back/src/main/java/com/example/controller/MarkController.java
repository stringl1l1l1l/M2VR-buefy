package com.example.controller;

import com.example.constant.MARK_EMUM;
import com.example.entity.Mark;
import com.example.entity.ResponseResult;
import com.example.service.MarkService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Api
@Validated
@RestController
@RequestMapping("/mark")
public class MarkController {

    @Resource
    private MarkService markService;

    @GetMapping("/findAllMarks")
    public ResponseResult<List<Mark>> findAllMarks() {
        List<Mark> list = markService.findAllMarks();
        return new ResponseResult<>(200, "操作成功", list);
    }

    @GetMapping("/findMarksByTopicId/{topicId}")
    public ResponseResult<List<Mark>> findMarksByTopicId(@PathVariable Integer topicId) {
        List<Mark> list = markService.findMarksByTopicId(topicId);
        return new ResponseResult<>(200, "操作成功", list);
    }

    @GetMapping("/findMarksByBegin/{begin}")
    public ResponseResult<List<Mark>> findMarksByBegin(@PathVariable String begin) {
        List<Mark> list = markService.findMarksByBegin(begin);
        return new ResponseResult<>(200, "操作成功", list);
    }

    @GetMapping("/findTodoBvidList/{begin}/{topicId}")
    public ResponseResult<List<Object>> findTodoBvidList(@PathVariable String begin, @PathVariable Integer topicId) {
        List<Object> list = markService.findTodoBvidList(begin, topicId);
        return new ResponseResult<>(200, "操作成功", list);
    }

    @GetMapping("/isMarked/{begin}/{end}")
    public ResponseResult<Integer> isMarked(@PathVariable String begin, @PathVariable String end) {
        Integer res = markService.isMarked(begin, end);
        return new ResponseResult<>(200, "操作成功", res);
    }

    @PostMapping("/insertMark")
    public ResponseResult<Map<String, Integer>> insertMark(@RequestBody Mark mark) {
        if (Objects.equals(mark.getMark(), MARK_EMUM.UNDEFINED.getValue()))
            return new ResponseResult<>(400, "无效的标注类型");
        int res = markService.insertMark(mark);
        Map<String, Integer> map = new HashMap<>();
        map.put("影响行数", res);
        return new ResponseResult<>(res > 0 ? 200 : 400, res > 0 ? "操作成功" : "插入失败，两视频重复或视频已标注", map);
    }

}
