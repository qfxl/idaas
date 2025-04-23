package com.zbycorp.user.saml.aliyun;

import com.zbycorp.user.saml.SamlParams;
import lombok.Data;

/**
 * @author xuyonghong
 * @date 2022-11-15 16:36
 **/
@Data
public class  AliyunSamlParams extends SamlParams {

    private String aliyunAccount;
    private String aliyunId;
}
