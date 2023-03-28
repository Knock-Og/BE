package com.project.comgle.dto.common;

public class SchemaDescriptionUtils {

    public static final String ID = "고유번호";
    public static final String EMAIL = "이메일";
    public static final String CREATE_AT = "생성일자";
    public static final String MODIFIED_AT = "수정일자";
    public static final String STATUS_CODE = "상태코드";
    public static final String MESSAGE = "상태메세지";

    public static class Company {
        public static final String NAME = "회원명";
        public static final String ADDRESS = "주소";
        public static final String TEL = "연락처";
        public static final String PRESIDENT = "대표명";
        public static final String BUSINESS_NUM = "사업자번호";
    }

    public static class Member {
        public static final String NAME = "회원명";
        public static final String PASSWORD = "패스워드";
        public static final String TEL = "연락처";
        public static final String POSITION = "직책(MEMBER|MANAGER|OWNER)";
    }

    public static class Post{
        public static final String TITLE = "제목";
        public static final String CONTENT = "내용";

        public static final String MEDIFY_PERMISSION = "수정권한";
        public static final String READABLE_POSITION = "읽기권한";
    }

    public static class Keyword{
        public static final String NAME = "댓글 내용";
    }

    public static class Comment{
        public static final String COMMENT = "댓글 내용";
    }

    public static class Category{
        public static final String NAME = "카테고리명";
    }

    public static class BookMarkForder{
        public static final String NAME = "카테고리명";
    }

    public static class SMS{
        public static final String AUTHENTICATION_CODE = "SMS 인증코드(6자리)";
    }
}
