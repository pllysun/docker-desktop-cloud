package com.cloud.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * Json响应实体
 *
 * @author zhou kai
 * 2023-12-01
 **/
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> R<T> success(String msg, T data) {
        return new R<T>(0, msg, data);
    }

    public static <T> R<T> success(T data) {
        return new R<T>(0, "success", data);
    }

    public static <T> R<T> success(String msg) {
        return new R<T>(0, msg);
    }

    public static <T> R<T> success() {
        return new R<T>(0, "success");
    }

    public static <T> R<T> fail(int code, String msg, T data) {
        return new R<T>(code, msg, data);
    }

    public static <T> R<T> fail(String msg, T data) {
        return new R<T>(1, msg, data);
    }

    public static <T> R<T> fail(T data) {
        return new R<T>(1, "fail", data);
    }

    public static <T> R<T> fail(String msg) {
        return new R<T>(1, msg);
    }

    public static <T> R<T> fail() {
        return new R<T>(1, "fail");
    }
}
