package com.zbycorp.user.saml;

import cn.hutool.core.collection.CollUtil;
import com.zbycorp.exception.saml.SamlIdpGenerateException;
import lombok.extern.slf4j.Slf4j;
import org.opensaml.Configuration;
import org.opensaml.saml2.metadata.*;
import org.opensaml.saml2.metadata.impl.EntityDescriptorMarshaller;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Base64;
import java.util.List;

/**
 * @author xuyonghong
 * @date 2023-01-15 13:52
 **/
@Component
@Slf4j
public class SamlIdpBuilder extends SamlXmlBuilder {



    /**
     * 生成idp描述文件
     *
     * @param identify
     * @return
     */
    public String buildIdpMetaData(String identify) {
        try {
            BasicX509Credential credential = samlCertificateCenter.obtainBasicX509Certificate(identify, keyPair -> {
                String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                log.info("private key: " + privateKey);
                log.info("-----------");
                log.info("public key: " + publicKey);
//                saveAliyunConfig(tenantId, identify, publicKey, privateKey);
            });
            EntityDescriptor entityDescriptor = buildEntityDescriptor();
            Signature signature = buildSignature();
            signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
            signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
            signature.setSigningCredential(credential);
            SecurityConfiguration securityConfiguration = Configuration.getGlobalSecurityConfiguration();
            SecurityHelper.prepareSignatureParams(signature, credential, securityConfiguration, null);
            entityDescriptor.setSignature(signature);
            Configuration.getMarshallerFactory()
                    .getMarshaller(entityDescriptor)
                    .marshall(entityDescriptor);
            Signer.signObject(signature);
            Element element = signature.getDOM();
            Node keyInfoNode = element.getLastChild();
            Element entityDescriptorDom = entityDescriptor.getDOM();
            if (entityDescriptorDom != null) {
                entityDescriptorDom.removeChild(signature.getDOM());
            }
            appendKeyInfo(entityDescriptor, keyInfoNode);
            EntityDescriptorMarshaller marshaller = new EntityDescriptorMarshaller();
            Element plain = marshaller.marshall(entityDescriptor);
            return XMLHelper.nodeToString(plain);
        } catch (Exception e) {
            log.error("生成idp-metadata.xml失败：", e);
            throw SamlIdpGenerateException.failed();
        }
    }

    /**
     * 创建idp描述文件
     *
     * @return
     * @throws ConfigurationException
     */
    private EntityDescriptor buildEntityDescriptor() throws ConfigurationException {
        EntityDescriptor entityDescriptor = buildElement(EntityDescriptor.DEFAULT_ELEMENT_NAME);
        entityDescriptor.setEntityID("https://idaas.shuzhi-inc.com/saml2/meta");
        //IdpSSODescriptor
        IDPSSODescriptor idpssoDescriptor = buildElement(IDPSSODescriptor.DEFAULT_ELEMENT_NAME);
        idpssoDescriptor.setWantAuthnRequestsSigned(false);
        idpssoDescriptor.addSupportedProtocol("urn:oasis:names:tc:SAML:2.0:protocol");

        KeyDescriptor keyDescriptor = buildElement(KeyDescriptor.DEFAULT_ELEMENT_NAME);
        keyDescriptor.setUse(UsageType.SIGNING);

        NameIDFormat nameIdEmailFormat = buildElement(NameIDFormat.DEFAULT_ELEMENT_NAME);
        nameIdEmailFormat.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
        NameIDFormat nameIdPersistentFormat = buildElement(NameIDFormat.DEFAULT_ELEMENT_NAME);
        nameIdPersistentFormat.setFormat("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");


        SingleSignOnService ssoPostService = buildElement(SingleSignOnService.DEFAULT_ELEMENT_NAME);
        ssoPostService.setBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
        ssoPostService.setLocation("https://idaas.shuzhi-inc.com/saml2/sso");

        SingleSignOnService ssoRedirectService = buildElement(SingleSignOnService.DEFAULT_ELEMENT_NAME);
        ssoRedirectService.setBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect");
        ssoRedirectService.setLocation("https://idaas.shuzhi-inc.com/saml2/sso");

        idpssoDescriptor.getKeyDescriptors().add(keyDescriptor);

        idpssoDescriptor.getNameIDFormats().add(nameIdEmailFormat);
        idpssoDescriptor.getNameIDFormats().add(nameIdPersistentFormat);

        idpssoDescriptor.getSingleSignOnServices().add(ssoPostService);
        idpssoDescriptor.getSingleSignOnServices().add(ssoRedirectService);

        entityDescriptor.getRoleDescriptors().add(idpssoDescriptor);
        return entityDescriptor;
    }

    /**
     * 追加keyInfo信息
     *
     * @param entityDescriptor
     * @param keyInfoNode
     */
    private void appendKeyInfo(EntityDescriptor entityDescriptor, Node keyInfoNode) {
        IDPSSODescriptor idpssoDescriptor = entityDescriptor.getIDPSSODescriptor("urn:oasis:names:tc:SAML:2.0:protocol");
        if (idpssoDescriptor == null) {
            return;
        }
        List<KeyDescriptor> keyDescriptors = idpssoDescriptor.getKeyDescriptors();
        if (CollUtil.isEmpty(keyDescriptors)) {
            return;
        }
        KeyDescriptor keyDescriptor = keyDescriptors.get(0);
        if (keyDescriptor == null) {
            return;
        }
        Element keyDescriptorDom = keyDescriptor.getDOM();
        if (keyDescriptorDom == null) {
            return;
        }
        keyDescriptorDom.appendChild(keyInfoNode);
    }

}
