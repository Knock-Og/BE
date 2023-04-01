package com.project.comgle.member.entity;

import lombok.Getter;

@Getter
public enum PositionEnum {

    ADMIN(Authority.ADMIN_NUM,Authority.ADMIN),
    OWNER(Authority.OWNER_NUM,Authority.OWNER),
    MANAGER(Authority.MANAGER_NUM,Authority.MANAGER),
    MEMBER(Authority.MEMBER_NUM,Authority.MEMBER);

    private final int num;
    private final String authority;

    PositionEnum(int num, String authority) {
        this.num = num;
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String OWNER = "ROLE_OWNER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MEMBER = "ROLE_MEMBER";

        public static final int ADMIN_NUM = 3;
        public static final int OWNER_NUM = 2;
        public static final int MANAGER_NUM = 1;
        public static final int MEMBER_NUM = 0;
    }

}
