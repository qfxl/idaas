package com.zbycorp.exception.saml;


import com.zbycorp.exception.ErrorCode;
import com.zbycorp.exception.AppException;

/**
 * SAML登录异常
 *
 * @author xuyonghong
 * @date 2023-08-23 19:41
 **/
public class SamlException extends AppException {

    private SamlException(String code, String message) {
        super(code, message);
    }

    public static SamlException failed() {
        return new SamlException(ErrorCode.COMMON_ERROR.getCode(), "SAML登录失败");
    }

    public static SamlException failed(String code, String message) {
        return new SamlException(code, message);
    }

}
