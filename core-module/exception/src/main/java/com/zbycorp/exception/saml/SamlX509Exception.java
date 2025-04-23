package com.zbycorp.exception.saml;


import com.zbycorp.exception.ErrorCode;
import com.zbycorp.exception.AppException;

/**
 * SAML登录异常
 *
 * @author xuyonghong
 * @date 2023-08-23 19:41
 **/
public class SamlX509Exception extends AppException {

    private SamlX509Exception(String code, String message) {
        super(code, message);
    }

    public static SamlX509Exception failed() {
        return new SamlX509Exception(ErrorCode.COMMON_ERROR.getCode(), "SAML登录失败");
    }

    public static SamlX509Exception failed(String code, String message) {
        return new SamlX509Exception(code, message);
    }

}
