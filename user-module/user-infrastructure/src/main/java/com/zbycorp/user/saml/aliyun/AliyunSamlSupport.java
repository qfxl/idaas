package com.zbycorp.user.saml.aliyun;

import com.zbycorp.exception.AssertUtil;
import com.zbycorp.user.saml.SamlSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xuyonghong
 * @date 2022-12-22 11:05
 **/
@Slf4j
@Component
public class AliyunSamlSupport extends SamlSupport<AliyunSamlParams> {

    @Resource
    private AliyunSamlResponseBuilder aliyunSamlResponseBuilder;

    /**
     * 构建Saml2 Node String
     *
     * @return
     */
    @Override
    public String buildSaml2NodeString() {
        // 阿里云朱账号id
        String aliyunMainAccountId = "1850496108086839";
        AssertUtil.notBlank(aliyunMainAccountId, "租户阿里云id未设置，无法发起SAML登录");

        String userRamId = "xuyonghong";
        AssertUtil.notBlank(userRamId, "用户ram账号不能为空");

        AliyunSamlParams params = new AliyunSamlParams();
        //登录标识，对应保存idp加密信息的key SamlCertificateCenter#x509CredentialMap
        params.setIdentify("weinian");
        params.setAliyunAccount(userRamId + "@" + aliyunMainAccountId + ".onaliyun.com");
        params.setIdpClientId("https://idaas.shuzhi-inc.com/saml2/meta");
        params.setRecipient("https://signin.aliyun.com/saml/SSO");
        params.setAliyunId(aliyunMainAccountId);
        params.setIssuer("https://idaas.shuzhi-inc.com/saml2/meta");
        params.setNameId(params.getAliyunAccount());
        params.setAudienceUri("https://signin.aliyun.com/" + aliyunMainAccountId + "/saml/SSO");
        params.setSamlLoginUrl(params.getAudienceUri());

        return aliyunSamlResponseBuilder.generateSamlResponse(params);
    }


}
