package com.zbycorp.user.domain.saml;

import com.zbycorp.common.SamlSpClientEnum;
import com.zbycorp.user.domain.user.User;

/**
 * @author xuyonghong
 * @date 2025-04-09 16:41
 **/
public interface SamlService {

    /**
     * 生成SAML登录应答
     *
     * @param user
     * @return
     */
    SamlResponse generateResponse(User user);

    /**
     * 生成SAML idp
     * @param identify
     * @return
     */
    String generateIdpXml(String identify);

    /**
     * SAML sp
     *
     * @return
     */
    SamlSpClientEnum client();
}
