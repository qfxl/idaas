package com.zbycorp.user.saml;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author xuyonghong
 * @date 2022-12-22 10:29
 **/
@Slf4j
public abstract class SamlSupport<Params extends SamlParams> {
    @Resource
    private SamlIdpBuilder samlIdpBuilder;


    /**
     * 构建Saml2 Node String
     *
     * @return
     */
    protected abstract String buildSaml2NodeString();

    /**
     * 生成Saml Response
     *
     * @return
     */
    public String generateSaml2Response() {
        String nodeString = buildSaml2NodeString();
        return Base64.getEncoder().encodeToString(nodeString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成idp文件
     *
     * @param identify
     * @return
     */
    public String generateIdpMetadata(String identify) {
        return samlIdpBuilder.buildIdpMetaData(identify);
    }
}
