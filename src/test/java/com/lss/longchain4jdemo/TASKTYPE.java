package com.lss.longchain4jdemo;

import lombok.Getter;

public enum TASKTYPE {


    MODIFY_TICKET("修改机票"),
    QUERY_TICKET("查寻机票"),
    CANCEL_TICKET("取消机票"),
    OTHER("其他");

    @Getter
    private String name;

    TASKTYPE(String name) {
        this.name = name;
    }
}
