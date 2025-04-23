package com.zbycorp.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.zbycorp.exception.AppException;
import com.zbycorp.exception.ErrorCode;

import java.util.List;
import java.util.Objects;

/**
 * @author xuyonghong
 * @date 2025-04-08 18:28
 **/
public class AssertUtil {

    public static void notBlank(String str, String message) {
        if (CharSequenceUtil.isBlank(str)) {
            throw AppException.failed(ErrorCode.COMMON_ERROR.getCode(), message);
        }
    }

    public static void notNull(Object obj, String message) {
        if (Objects.isNull(obj)) {
            throw AppException.failed(ErrorCode.COMMON_ERROR.getCode(), message);
        }
    }

    public static void notEmpty(List<?> list, String message) {
        if (CollUtil.isEmpty(list)) {
            throw AppException.failed(ErrorCode.COMMON_ERROR.getCode(), message);
        }
    }
}
