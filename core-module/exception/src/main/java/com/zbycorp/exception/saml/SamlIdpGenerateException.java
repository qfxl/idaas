package com.zbycorp.exception.saml;


import com.zbycorp.exception.ErrorCode;
import com.zbycorp.exception.AppException;

/**
 * SAML登录异常
 *
 * @author xuyonghong
 * @date 2023-08-23 19:41
 **/
public class SamlIdpGenerateException extends AppException {

    private SamlIdpGenerateException(String code, String message) {
        super(code, message);
    }

    public static SamlIdpGenerateException failed() {
        return new SamlIdpGenerateException(ErrorCode.COMMON_ERROR.getCode(), "SAML登录失败");
    }

    public static SamlIdpGenerateException failed(String code, String message) {
        return new SamlIdpGenerateException(code, message);
    }

}
