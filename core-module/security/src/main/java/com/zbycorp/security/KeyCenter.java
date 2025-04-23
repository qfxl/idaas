package com.zbycorp.security;

import cn.hutool.core.date.DateUtil;
import com.zbycorp.exception.saml.SamlException;
import org.apache.commons.codec.binary.Base64;
import sun.security.x509.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xuyonghong
 * @date 2023-01-15 09:02
 **/
public class KeyCenter {

    static {
        Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
    }

    public static KeyCenter getInstance() {
        return KeyCenterHolder.getInstance();
    }

    private static class KeyCenterHolder {
        public static final KeyCenter INSTANCE = new KeyCenter();

        private KeyCenterHolder() {
        }

        public static KeyCenter getInstance() {
            return INSTANCE;
        }
    }

    /**
     * 获取X509证书
     *
     * @param keyPair
     * @param identify
     * @return
     * @throws Exception
     */
    public X509Certificate getX509Certificate(KeyPair keyPair, String identify) throws Exception {
        X500Name owner = new X500Name("CN=" + identify);
        PrivateKey privateKey = keyPair.getPrivate();
        X509CertInfo info = new X509CertInfo();

        Date since = DateUtil.parse("2025-01-01", "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(since);
        //60年有效期
        calendar.add(Calendar.YEAR, 60);
        CertificateValidity interval = new CertificateValidity(since, calendar.getTime());
        BigInteger sn = BigInteger.valueOf(12L);
        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        info.set(X509CertInfo.SUBJECT, owner);
        info.set(X509CertInfo.ISSUER, owner);
        info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));

        AlgorithmId algo = new AlgorithmId(AlgorithmId.sha256WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

        // 证书签名
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privateKey, "SHA256withRSA");

        // 更新算法并重新签名
        algo = (AlgorithmId) cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);

        cert = new X509CertImpl(info);
        cert.sign(privateKey, "SHA256withRSA");
        return cert;
    }

    /**
     * 生成KeyPair
     *
     * @return
     */
    public KeyPair generateKeyPair() {
        return generateKeyPair("RSA", 4096);
    }

    /**
     * 通过publicKey跟privateKey获取KeyPair
     *
     * @param privateKeyBase64
     * @param publicKeyBase64
     * @return
     */
    public KeyPair generateKeyPairFromSpec(String privateKeyBase64, String publicKeyBase64) {
        return generateKeyPairFromSpec("RSA", privateKeyBase64, publicKeyBase64);
    }

    /**
     * 通过publicKey跟privateKey获取KeyPair
     *
     * @param algorithm
     * @param privateKeyBase64
     * @param publicKeyBase64
     * @return
     */
    public KeyPair generateKeyPairFromSpec(String algorithm, String privateKeyBase64, String publicKeyBase64) {
        KeyPair keyPair;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            X509EncodedKeySpec publicKeySpec = getX509EncodedKeySpec(publicKeyBase64);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            PKCS8EncodedKeySpec privateKeySpec = getPKCS8EncodeKeySpec(privateKeyBase64);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            keyPair = new KeyPair(publicKey, privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw SamlException.failed();
        }
        return keyPair;
    }

    /**
     * 生成KeyPair
     *
     * @param algorithm
     * @param keySize
     * @return
     */
    public KeyPair generateKeyPair(String algorithm, int keySize) {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(keySize);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw SamlException.failed();
        }
        return keyPair;
    }

    /**
     * 获取X509EncodeKeySpec
     *
     * @param base64
     * @return
     */
    private X509EncodedKeySpec getX509EncodedKeySpec(String base64) {
        return new X509EncodedKeySpec(Base64.decodeBase64(base64.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 获取PKCS8EncodedKeySpec
     *
     * @param base64
     * @return
     */
    private PKCS8EncodedKeySpec getPKCS8EncodeKeySpec(String base64) {
        return new PKCS8EncodedKeySpec(Base64.decodeBase64(base64.getBytes(StandardCharsets.UTF_8)));
    }
}
