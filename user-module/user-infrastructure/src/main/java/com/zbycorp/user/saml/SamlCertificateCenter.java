package com.zbycorp.user.saml;

import com.zbycorp.exception.AssertUtil;
import com.zbycorp.exception.saml.SamlX509Exception;
import com.zbycorp.security.KeyCenter;
import lombok.extern.slf4j.Slf4j;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author xuyonghong
 * @date 2022-11-16 14:57
 **/

@Slf4j
@Component
public class SamlCertificateCenter {

    private final KeyCenter keyCenter = KeyCenter.getInstance();
    /**
     * X509证书Map，key为各个平台的标识
     */
    private Map<String, BasicX509Credential> x509CredentialMap;

    @PostConstruct
    public void init() {
        x509CredentialMap = new HashMap<>(10);
        initX509CredentialMap();
    }

    /**
     * 获取SAMLResponse加密需要的x509证书
     *
     * @param privateKey
     * @param publicKey
     * @return
     */
    public BasicX509Credential getBasicX509Certificate(String privateKey, String publicKey, String identify) {
        BasicX509Credential basicX509Credential;
        try {
            KeyPair keyPair = keyCenter.generateKeyPairFromSpec(privateKey, publicKey);
            X509Certificate certificate = keyCenter.getX509Certificate(keyPair, identify);
            basicX509Credential = new BasicX509Credential();
            basicX509Credential.setEntityCertificate(certificate);
            basicX509Credential.setPrivateKey(keyPair.getPrivate());
            basicX509Credential.setPublicKey(keyPair.getPublic());
        } catch (Exception e) {
            log.error("获取X509证书失败：", e);
            throw SamlX509Exception.failed();
        }
        return basicX509Credential;
    }

    /**
     * 生成新的X509证书
     *
     * @param identify
     * @param generateConsumer
     * @return
     */
    public BasicX509Credential obtainBasicX509Certificate(String identify, Consumer<KeyPair> generateConsumer) {
        BasicX509Credential basicX509Credential = getBasicX509CredentialByIdentify(identify);
        if (basicX509Credential == null) {
            try {
                KeyPair keyPair = keyCenter.generateKeyPair();
                X509Certificate certificate = keyCenter.getX509Certificate(keyPair, identify);
                basicX509Credential = new BasicX509Credential();
                basicX509Credential.setEntityCertificate(certificate);
                basicX509Credential.setPrivateKey(keyPair.getPrivate());
                basicX509Credential.setPublicKey(keyPair.getPublic());
                generateConsumer.accept(keyPair);
                x509CredentialMap.put(identify, basicX509Credential);
            } catch (Exception e) {
                log.error("复用X509证书失败：", e);
                throw SamlX509Exception.failed();
            }
        }
        return basicX509Credential;
    }

    /**
     * 获取对应X509证书信息
     *
     * @param identify
     * @return
     */
    public BasicX509Credential getBasicX509CredentialByIdentify(String identify) {
        AssertUtil.notBlank(identify, "识别码为不能为空");
        return x509CredentialMap.get(identify);
    }

    /**
     * 初始化各个平台X509证书信息
     */
    private void initX509CredentialMap() {
//        String privateKey = "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";
//        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAirD9T3BoqpqNlCQ0w0NO+69Vnu+0tS1VfK1gS6nJeJmJcToIXVJ0LZjGTUngShfYQvS3o58bqdjnla82qpj+VEaeGMqrGvFMbuGxEnc/F7jXpGJdgFc7RU90PTOCWuVvPmEZEQL2/esaFr1KFoZc6JpOD9MzfHgDNFXOnq0wHVtmBprRQKFrTMalcQddNmZyi6PvyaC5TVom6KhIPu47mgK1U5dDQAbPuXCu8EuN1cEFglaKsfgxgKkQRQo4OhoCKLFxp0gjXuXJ7hZLumVI9J2jhRYRbOoCnL1U8TseKKWkrLdL8UGvLhWN/ZmTvJuAWlRLOzHu6QOxG5smKx+25O9N6GNVPOKg7omOmmvwsVSTUCJay//Xxn4st+I3eFeefDdlsjQKQ6lU/dYvoMFru5yy9hAUOedF7nZoIJTLGSggFvZv1mhESZna20FRrs8WenvTcJxi9sFsPZM7nkNtGuMcDaU2DmX9zWzqq4g27bCqzJ4aAh5dYF6xPTWAnCfJLKzh9oxoF0GlYgaHRU2VsrEeTZIqvyXiqKGSp9/zZnL+vNuaFBhrrkBNG0KKp/Lh0w43QA1LgBdkIdseSfHURF85NA0hxZ/RlHPrvK7quf4fepzpkbCxXl19/5A5S8A0IdA+pkmu8K3KJXAWra0BEmLE0DFdQLTj0KuxIGg7kTkCAwEAAQ==";

        String privateKey = "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";
        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmfexMJSeNLxc9Zii9Q4uTil+Fm9e73dwYqIoWQ+HOlakQ3Ee2EMn3M3f1oqSUKW9mLUY7XgAFaLM0FsmIG9DFZCpPYSL+mIEkg+FCmMJ8wTsx/etqunQiQt26faOTDH5Quf93QlE8qvVv7g3MgwjDJJ8/rCVhQ3cMMR7+K9oAHfs49UMa9V4SDyN70qQTcZaPC2kd7NkvBKbZ/s2fDCb+NToLco25FtOIaeoDmhVnDKCrJr4NTbW0KJCcIvkptv9xSFNG25LFSJAKQf3dm2c5mZH5ow9FZ7EDKYmeYMhWBhOe+wx7JtxGFSZX8sBcq2N1u4zihFMJOQE2qNmN7dEmT71zvKa5qDk15VANrHMYb0j+atJmQ29+qJsJWvFscc01aDpUmSUzsk7Nk07dmuyyu/Fn8Ip1e7ntIRnIEC59CUk0TvQDynKMRA78vE9w958+U5f+TvL2uQMG0i5SnP5xWqC54e9CBxPjb0+fW+f0YDD5a487KL/aIGwIJQxQZHkf9hVXUeOZQCw6PZlXWdz8DBrzdM6F9tYlrnfIOxKNmxvK2R5MNKS/UlPwW8fYPZwHewapxJk+vYSvMIa6oNYXOtlmrSkHM90h8MsoKmwQYJKpQMMkNCHh11qvT1KYu+yEiXFC4d/wtAeNmVQI6K607IphQNzF4Clxgw1W04/NjkCAwEAAQ==";

        BasicX509Credential x509Credential = getBasicX509Certificate(privateKey, publicKey, "weinian");
        x509CredentialMap.put("weinian", x509Credential);
    }
}
