package com.zbycorp.user.domain.saml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuyonghong
 * @date 2025-04-09 16:32
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SamlResponse {

    private String responseXml;

    private String audienceUrl;
}
