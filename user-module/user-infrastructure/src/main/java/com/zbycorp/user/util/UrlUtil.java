package com.zbycorp.user.util;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author xuyonghong
 * @date 2025-04-09 11:41
 **/
public class UrlUtil {

    private UrlUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * URL编码
     *
     * @param url
     * @return
     */
    public static String encode(String url) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(url)) {
            return StringUtils.EMPTY;
        }
        return URLEncoder.encode(url, CharEncoding.UTF_8);
    }

    /**
     * URL解码
     *
     * @param url
     * @return
     */
    public static String decode(String url) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(url)) {
            return StringUtils.EMPTY;
        }
        return URLDecoder.decode(url, CharEncoding.UTF_8);
    }

}
