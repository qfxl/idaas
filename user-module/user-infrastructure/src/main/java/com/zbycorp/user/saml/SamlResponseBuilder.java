package com.zbycorp.user.saml;

import com.zbycorp.exception.saml.SamlResponseGenerateException;
import lombok.extern.slf4j.Slf4j;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.ConfigurationException;

/**
 * @author xuyonghong
 * @date 2023-01-15 13:47
 **/
@Slf4j
public abstract class SamlResponseBuilder extends SamlXmlBuilder {
    /**
     * 对SamlResponse签名
     *
     * @param params
     * @return
     */
    public String generateSamlResponse(SamlParams params) {
        try {
            return marshall(params);
        } catch (Exception e) {
            log.error("SAML 应答生成失败：", e);
            throw SamlResponseGenerateException.failed();
        }
    }

    /**
     * 构建SAML 应答
     *
     * @param params
     * @return
     * @throws ConfigurationException
     */
    protected abstract Response buildResponse(SamlParams params) throws ConfigurationException;

    /**
     * 构建SAML 断言
     *
     * @param params
     * @return
     * @throws ConfigurationException
     */
    protected abstract Assertion buildAssertion(SamlParams params) throws ConfigurationException;

    /**
     * marshall
     *
     * @param params
     * @return
     * @throws Exception
     */
    protected abstract String marshall(SamlParams params) throws Exception;
}
