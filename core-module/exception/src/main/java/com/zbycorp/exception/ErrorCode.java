package com.zbycorp.exception;

/**
 * @author xuyonghong
 * @date 2022-06-10 09:45
 **/
public enum ErrorCode {
    /**
     * 通用异常
     */
    COMMON_ERROR("-1", "处理失败");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
