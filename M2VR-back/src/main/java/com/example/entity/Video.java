package com.example.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @TableId
    private String bvid;
    private String title;
    private String author;
    private Timestamp updateTime;
    private String duration;
    private int danmaku;
    private int likes;
    private int play;
    private int collect;
    private String area;
    private String tag;
    private String description;
    private String cover;
    private int topicId;
    private Integer mark;
    private Integer label;
}
