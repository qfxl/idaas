package com.zbycorp.user.saml;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.signature.Signature;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import java.util.UUID;

/**
 * @author xuyonghong
 * @date 2022-11-15 16:36
 **/
public class SamlXmlBuilder {

    @Resource
    protected SamlCertificateCenter samlCertificateCenter;

    protected XMLObjectBuilderFactory builderFactory;

    protected XMLObjectBuilderFactory getSamlBuilder() throws ConfigurationException {
        if (builderFactory == null) {
            System.setProperty("org.opensaml.httpclient.https.disableHostnameVerification", "true");
            DefaultBootstrap.bootstrap();
            builderFactory = Configuration.getBuilderFactory();
        }
        return builderFactory;
    }

    /**
     * build element
     *
     * @param qName
     * @return
     * @throws ConfigurationException
     */
    protected <T> T buildElement(QName qName) throws ConfigurationException {
        return (T) getSamlBuilder().getBuilder(qName).buildObject(qName);
    }

    /**
     * 创建Issuer
     *
     * @param issuerValue
     * @return
     * @throws ConfigurationException
     */
    protected Issuer buildIssuer(String issuerValue) throws ConfigurationException {
        //创建Issuer
        Issuer issuer = buildElement(Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue(issuerValue);
        return issuer;
    }

    /**
     * 创建signature
     *
     * @return
     * @throws ConfigurationException
     */
    protected Signature buildSignature() throws ConfigurationException {
        return buildElement(Signature.DEFAULT_ELEMENT_NAME);
    }

    protected DateTime getDateTime() {
        DateTimeZone zone = DateTimeZone.forID("Asia/Tokyo");
        return new DateTime(zone);
    }

    protected String generateId() {
        return "_" + UUID.randomUUID();
    }
}
