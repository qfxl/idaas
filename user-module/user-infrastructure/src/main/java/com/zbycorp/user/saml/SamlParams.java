package com.zbycorp.user.saml;

import lombok.Data;

/**
 * @author xuyonghong
 * @date 2022-11-15 16:36
 **/
@Data
public class SamlParams {
    private String issuer;
    private String nameId;
    private String idpClientId;
    /**
     * acs url
     */
    private String recipient;
    private String audienceUri;
    private String samlLoginUrl;
    /**
     * 身份标识，aliyunId或者其他平台的标识
     */
    private String identify;
}
