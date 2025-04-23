package com.zbycorp.user.saml;

import com.zbycorp.common.SamlSpClientEnum;
import com.zbycorp.user.domain.saml.SamlResponse;
import com.zbycorp.user.domain.saml.SamlService;
import com.zbycorp.user.domain.user.User;
import com.zbycorp.user.saml.aliyun.AliyunSamlSupport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author xuyonghong
 * @date 2025-04-09 17:21
 **/
@Service
@AllArgsConstructor
public class AliyunOpenSamlService implements SamlService {

    private final AliyunSamlSupport aliyunSamlSupport;

    /**
     * saml response
     *
     * @param user
     * @return
     */
    @Override
    public SamlResponse generateResponse(User user) {
        String nodeString = aliyunSamlSupport.generateSaml2Response();
        String aliyunMainAccountId = "1850496108086839";
        String audienceUrl = "https://signin.aliyun.com/" + aliyunMainAccountId + "/saml/SSO";
        return SamlResponse.builder()
                .responseXml(nodeString)
                .audienceUrl(audienceUrl)
                .build();
    }

    /**
     * 生成SAML idp
     *
     * @param identify
     * @return
     */
    @Override
    public String generateIdpXml(String identify) {
        return aliyunSamlSupport.generateIdpMetadata(identify);
    }

    /**
     * SAML sp
     *
     * @return
     */
    @Override
    public SamlSpClientEnum client() {
        return SamlSpClientEnum.ALIYUN;
    }
}
