package com.example.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mark {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String begin;
    private String end;
    private Integer mark;
    private int topicId;
}
