package com.project.comgle.entity.enumSet;

import lombok.Getter;

@Getter
public enum PositionEnum {
    ADMIN(3),
    OWNER(2),
    MANAGER(1),
    MEMBER(0);

    private int num;

    PositionEnum(int num) {
        this.num = num;
    }
}
