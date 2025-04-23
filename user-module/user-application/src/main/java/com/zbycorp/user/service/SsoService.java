package com.zbycorp.user.service;

import com.zbycorp.common.SamlSpClientEnum;
import com.zbycorp.exception.AppException;
import com.zbycorp.exception.saml.SamlException;
import com.zbycorp.user.domain.saml.SamlResponse;
import com.zbycorp.user.domain.saml.SamlService;
import com.zbycorp.user.service.saml.SamlServiceFactory;
import com.zbycorp.user.util.UrlUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

import static com.zbycorp.user.RequestKey.REDIRECT_URI;

/**
 * @author xuyonghong
 * @date 2025-04-09 16:36
 **/
@Service
@AllArgsConstructor
public class SsoService {
    private final SamlServiceFactory samlServiceFactory;

    /**
     * 登录阿里云
     *
     * @param request
     * @param modelMap
     * @return
     */
    public String samlSso(HttpServletRequest request, ModelMap modelMap) {
        try {
            SamlService samlService = samlServiceFactory.getInstance(SamlSpClientEnum.ALIYUN);
            String redirectUri = request.getParameter(REDIRECT_URI);
            SamlResponse response = samlService.generateResponse(null);
            String action = response.getAudienceUrl() + "?relayState=" + UrlUtil.encode(redirectUri);
            modelMap.put("action", action);
            String saml2Response = response.getResponseXml();
            modelMap.put("value", saml2Response);
            return "login_saml2";
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw SamlException.failed();
        }
    }

    /**
     * 生成saml idp文件
     *
     * @param identify
     * @return
     */
    public String generateSamlIdpMetadata(String identify) {
        SamlService samlService = samlServiceFactory.getInstance(SamlSpClientEnum.ALIYUN);
        return samlService.generateIdpXml(identify);
    }
}
