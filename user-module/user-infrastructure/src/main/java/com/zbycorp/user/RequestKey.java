package com.zbycorp.user;

/**
 * @author xuyonghong
 * @date 2025-04-16 16:45
 **/
public interface RequestKey {

    String REDIRECT_URI = "redirect_uri";
    String AUTH_CODE = "authCode";
    String STATE = "state";
    String FROM = "from";
    String TARGET = "target";
    String CORP_ID = "corpId";

    String USER_NAME = "username";
    String PASSWORD = "password";

    String USER_ID = "userId";
    String TENANT_ID = "tenantId";
    String GROUP_ID = "groupId";

    String TENANT_CODE = "tenantCode";

    String SAML_RELAY_STATE = "RelayState";
    String OAUTH_CLIENT_ID = "client_id";

    String LOGIN_DO_URI = "/login/do";

    String CLIENT_ID = "client_id";

    String LOGIN_TYPE = "login_type";

    String FEI_SHU_AUTH_CODE = "code";

    String WECHAT_AUTH_CODE = "code";

    String STATE_SPLIT = "-";

    String OAUTH_AUTHORIZE_PATH = "/oauth/authorize";
}
