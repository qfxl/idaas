package com.zbycorp.exception;

/**
 * @author xuyonghong
 * @date 2025-04-08 17:59
 **/
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private Integer isJsonp;

    public static AppException failed(String code, String message) {
        return new AppException(code, message);
    }

    public AppException() {
        super();
    }

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AppException(Integer isJsonp, String code, String message) {
        super(message);
        this.code = code;
        this.isJsonp = isJsonp;
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }


    public Integer getIsJsonp() {
        return isJsonp;
    }

    public void setIsJsonp(Integer isJsonp) {
        this.isJsonp = isJsonp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
