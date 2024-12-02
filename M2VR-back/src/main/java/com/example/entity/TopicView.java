package com.example.entity;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TopicView {

    private int topicId;
    private String topic;
    private int total;
    private int marked;
    private int labeled;
}
