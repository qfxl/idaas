package com.zbycorp.exception.saml;

import com.zbycorp.exception.ErrorCode;
import com.zbycorp.exception.AppException;

/**
 * @author xuyonghong
 * @date 2025-04-08 18:46
 **/
public class SamlResponseGenerateException extends AppException {

    private SamlResponseGenerateException(String code, String message) {
        super(code, message);
    }

    public static SamlResponseGenerateException failed() {
        return new SamlResponseGenerateException(ErrorCode.COMMON_ERROR.getCode(), "SAML应答构建失败");
    }

    public static SamlResponseGenerateException failed(String code, String message) {
        return new SamlResponseGenerateException(code, message);
    }

}
