package com.project.comgle.post.entity;

import lombok.Getter;

@Getter
public enum WeightEnum {
    BOOKMARK(5),
    POSTVIEWS(3),
    COMMENTCOUNT(1);

    private int num;

    WeightEnum(int num) {
        this.num = num;
    }
}
