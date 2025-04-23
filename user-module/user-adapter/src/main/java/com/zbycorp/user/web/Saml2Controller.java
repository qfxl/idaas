package com.zbycorp.user.web;

import com.zbycorp.exception.AssertUtil;
import com.zbycorp.user.service.SsoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xuyonghong
 * @date 2025-04-09 15:07
 **/
@Controller
@RequestMapping("saml2")
@AllArgsConstructor
public class Saml2Controller {

    private final SsoService ssoService;

    @GetMapping("aliyun_sso")
    public String loginSaml(HttpServletRequest request, ModelMap modelMap) {
        return ssoService.samlSso(request, modelMap);
    }


    /**
     * 生成idp文件
     *
     * @param identify
     * @param response
     * @throws IOException
     */
    @GetMapping("idp_metadata")
    public void generateIdpMetadata(String identify, HttpServletResponse response) throws IOException {
        AssertUtil.notBlank(identify, "generate IdpMetadata.xml failed，identify is null or blank");
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(ssoService.generateSamlIdpMetadata(identify));
    }
}
