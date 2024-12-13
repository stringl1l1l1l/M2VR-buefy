package com.example.service;

import com.example.entity.Mark;

import java.util.List;

public interface MarkService {
    List<Mark> findAllMarks();

    List<Mark> findMarksByTopicId(int topicId);

    List<Mark> findMarksByBegin(String begin);

    List<Object> findTodoBvidList(String begin, int topicId);
    
    int insertMark(Mark mark);

    int isMarked(String begin, String end);
}
