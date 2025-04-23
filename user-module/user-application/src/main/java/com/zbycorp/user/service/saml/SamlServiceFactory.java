package com.zbycorp.user.service.saml;

import com.zbycorp.common.AbstractInstanceFactory;
import com.zbycorp.common.SamlSpClientEnum;
import com.zbycorp.user.domain.saml.SamlService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * @author xuyonghong
 * @date 2025-04-09 17:39
 **/
@Component
public class SamlServiceFactory extends AbstractInstanceFactory<SamlSpClientEnum, SamlService> {
    public SamlServiceFactory(List<SamlService> samlServices) {
        super(samlServices);
    }

    /**
     * 每个实例惟一映射的类型执行函数
     * 必须由实现类实现, 只有实现类知道实例如何映射实例类型
     *
     * @return 类型执行函数
     */
    @Override
    protected Function<? super SamlService, ? extends SamlSpClientEnum> keyFunction() {
        return SamlService::client;
    }
}
