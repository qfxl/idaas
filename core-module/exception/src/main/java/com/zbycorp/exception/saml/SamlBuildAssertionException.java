package com.zbycorp.exception.saml;

import com.zbycorp.exception.ErrorCode;
import com.zbycorp.exception.AppException;

/**
 * @author xuyonghong
 * @date 2025-04-08 18:46
 **/
public class SamlBuildAssertionException extends AppException {

    private SamlBuildAssertionException(String code, String message) {
        super(code, message);
    }

    public static SamlBuildAssertionException failed() {
        return new SamlBuildAssertionException(ErrorCode.COMMON_ERROR.getCode(), "SAML应答构建失败");
    }

    public static SamlBuildAssertionException failed(String code, String message) {
        return new SamlBuildAssertionException(code, message);
    }

}
