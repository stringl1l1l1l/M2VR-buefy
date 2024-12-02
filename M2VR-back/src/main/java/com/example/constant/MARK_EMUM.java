package com.example.constant;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MARK_EMUM implements IEnum<Integer> {
    UNDEFINED(0, "UNDEFINED"),
    COPY(1, "COPY"),
    EVENT(2, "EVENT"),
    INSTANCE(4, "INSTANCE"),
    REASONING(8, "REASONING"),
    DEPENDENT(16, "DEPENDENT"),
    INDEPENDENT(32, "INDEPENDENT");

    private final int value;
    private final String disc;

    @Override
    public Integer getValue() {
        return this.value;
    }


}