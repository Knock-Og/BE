package com.project.comgle.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {

    WRONG_EMAIL(400,"Invalid email"),
    WORNG_PASSWORD(400,"Invalid password"),
    INVALID_EMAIL_REG(400,"Invalid email format"),
    INVALID_PASSWD_REG(400,"Passwords must be 8-32 letters long, including upper and lower cases or special characters."),
    INVALID_VALUE(400,"Invalid request"),
    INVALID_AUTHENTICATION_CODE(400,"Invalid authentication code"),
    SEND_SMS_CODE_ERR(400,"Failed to send sms code."),
    SEND_EMAIL_CODE_ERR(400,"Failed to send email code"),

    DUPLICATE_COMPANY(400, "Duplicate company exists."),
    DUPLICATE_EMAIL(400, "Duplicate email exists."),
    DUPLICATE_MEMBER(400, "Duplicate member exists."),
    DUPLICATE_PHONE_NUMBER(400, "Duplicate phone number exists."),
    DUPLICATE_POST(400, "Duplicate post exists."),
    DUPLICATE_FOLDER(400, "Duplicate folder exists."),
    DUPLICATE_CATEGORY(400,"Duplicate category exists."),
    DUPLICATE_PASSWORD(400, "Duplicate password exists."),

    EXCEED_FOLDER_NUM(400,"The maximum number of folders has been exceeded."),


    INVALID_TOKEN(401,"Invalid token"),


    REQUIRED_ADMIN_POSITION(403, "ADMIN permission is required."),
    IMMULATABLE_TO_ADMIN(403,"You cannot change your position to an ADMIN account."),
    IMMULATABLE_ADMIN_POSITION(403, "ADMIN accounts cannot change its position."),
    NOT_DELETE_ADMIN_POSITION(403, "ADMIN accounts cannot be deleted."),
    INVALID_PERMISSION_TO_MODIFY(403,"You are not authorized to edit this post."),
    INVALID_PERMISSION_TO_READ(403,"You are not authorized to read this post."),
    INVALID_PERMISSION_TO_DELETE_COMMENT(403,"You are not authorized to delete this comment."),


    NOT_EXIST_POSITION(404,"The position does not exist."),
    NOT_EXIST_MEMBER(404,"That account(member) does not exist."),
    NOT_EXIST_COMPANY(404, "The company does not exist."),
    NOT_EXIST_FOLDER(404,"The folder does not exist."),
    NOT_EXIST_USERS_FOLDER(404,"The user's folder does not exist."),
    NOT_EXIST_POST_IN_COMPANY(404, "The post does not exist in the company."),
    NOT_EXIST_POST(404,"The post does not exist."),
    NOT_EXIST_CATEGORY(404,"The category does not exist."),
    NOT_EXIST_COMMENT(404,"The comment does not exist."),
    NOT_EXIST_AUTHENTICATION_CODE(404,"Authentication code does not exist.");


    private final int code;
    private final String msg;

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
