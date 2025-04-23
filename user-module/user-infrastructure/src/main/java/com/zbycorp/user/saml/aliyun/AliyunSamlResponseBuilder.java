package com.zbycorp.user.saml.aliyun;

import com.zbycorp.exception.AssertUtil;
import com.zbycorp.exception.saml.SamlBuildAssertionException;
import com.zbycorp.user.saml.SamlParams;
import com.zbycorp.user.saml.SamlResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Optional;

/**
 * 阿里云SAML response
 *
 * @author xuyonghong
 * @date 2022-11-15 16:36
 **/
@Slf4j
@Component
public class AliyunSamlResponseBuilder extends SamlResponseBuilder {

    @Override
    protected Assertion buildAssertion(SamlParams input) {
        try {
            // 创建SubjectConfirmationData
            SubjectConfirmationData confirmationMethod = buildElement(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
            DateTime now = getDateTime();
            confirmationMethod.setNotOnOrAfter(now.plusHours(1));
            confirmationMethod.setRecipient(input.getRecipient());

            SubjectConfirmation subjectConfirmation = buildElement(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
            subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);
            subjectConfirmation.setSubjectConfirmationData(confirmationMethod);

            // 创建Subject
            Subject subject = buildElement(Subject.DEFAULT_ELEMENT_NAME);

            //创建nameID
            NameID nameId = buildElement(NameID.DEFAULT_ELEMENT_NAME);
            nameId.setValue(input.getNameId());
            nameId.setFormat(NameID.PERSISTENT);

            subject.setNameID(nameId);
            subject.getSubjectConfirmations().add(subjectConfirmation);

            // 创建 Authentication Statement
            AuthnStatement authnStatement = buildElement(AuthnStatement.DEFAULT_ELEMENT_NAME);
            DateTime now2 = getDateTime();
            authnStatement.setAuthnInstant(now2);
            authnStatement.setSessionIndex(generateId());

            AuthnContext authnContext = buildElement(AuthnContext.DEFAULT_ELEMENT_NAME);

            AuthnContextClassRef authnContextClassRef = buildElement(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
            authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:Password");

            authnContext.setAuthnContextClassRef(authnContextClassRef);
            authnStatement.setAuthnContext(authnContext);

            //创建AudienceRestriction
            AudienceRestriction audienceRestriction = buildElement(AudienceRestriction.DEFAULT_ELEMENT_NAME);
            Audience audience = buildElement(Audience.DEFAULT_ELEMENT_NAME);
            audience.setAudienceURI(input.getAudienceUri());
            audienceRestriction.getAudiences().add(audience);

            // 创建 condition
            Condition condition = buildElement(OneTimeUse.DEFAULT_ELEMENT_NAME);

            Conditions conditions = buildElement(Conditions.DEFAULT_ELEMENT_NAME);
            conditions.getConditions().add(condition);
            conditions.getAudienceRestrictions().add(audienceRestriction);

            // 创建 Issuer
            Issuer issuer = buildElement(Issuer.DEFAULT_ELEMENT_NAME);
            issuer.setValue(input.getIdpClientId());

            // Create the assertion
            Assertion assertion = buildElement(Assertion.DEFAULT_ELEMENT_NAME);
            assertion.setIssuer(issuer);
            assertion.setIssueInstant(now);
            assertion.setVersion(SAMLVersion.VERSION_20);
            assertion.setSubject(subject);
            assertion.getAuthnStatements().add(authnStatement);
            assertion.setConditions(conditions);
            return assertion;
        } catch (Exception e) {
            log.error("SAML断言构建失败：" , e);
            throw SamlBuildAssertionException.failed();
        }
    }

    /**
     * marshall
     *
     * @return
     */
    @Override
    protected String marshall(SamlParams params) throws Exception {
        String identify = params.getIdentify();
        AssertUtil.notBlank(identify, "识别码为不能为空");
        BasicX509Credential credential = samlCertificateCenter.getBasicX509CredentialByIdentify(identify);
        AssertUtil.notNull(credential, "saml response marshall failed, credential is null");
        Response response = buildResponse(params);
        AssertUtil.notNull(response, "saml response is invalid, response is null");
        List<Assertion> assertions = response.getAssertions();
        AssertUtil.notEmpty(assertions, "saml response is invalid, assertions is empty");
        Assertion assertion = assertions.get(0);
        Signature signature = buildSignature();
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        signature.setSigningCredential(credential);
        assertion.setSignature(signature);
        Configuration.getMarshallerFactory()
                .getMarshaller(response)
                .marshall(response);
        Signer.signObject(signature);
        ResponseMarshaller marshaller = new ResponseMarshaller();
        Element plain = marshaller.marshall(response);
        return XMLHelper.nodeToString(plain);
    }


    /**
     * 生成SAML Response
     *
     * @param params
     * @return
     */
    @Override
    protected Response buildResponse(SamlParams params) throws ConfigurationException {
        Response response = buildElement(Response.DEFAULT_ELEMENT_NAME);
        response.setID(generateId());
        response.setIssueInstant(getDateTime());
        Assertion assertion = buildAssertion(params);
        Optional.ofNullable(assertion).ifPresent(as -> as.setID(generateId()));
        response.getAssertions().add(assertion);
        response.setIssuer(buildIssuer(params.getIssuer()));
        StatusCode statusCode = buildElement(StatusCode.DEFAULT_ELEMENT_NAME);
        statusCode.setValue(StatusCode.SUCCESS_URI);
        Status status = buildElement(Status.DEFAULT_ELEMENT_NAME);
        status.setStatusCode(statusCode);
        response.setStatus(status);
        return response;
    }
}
