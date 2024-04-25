package com.miri.goodsservice.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESUtils {

    private static final int IV_LENGTH = 16;

    @Value("${miri.aes.secret-key}")
    private String secretKey; // 32byte

    private String iv = "";

    private static final String AES_METHOD = "AES/CBC/PKCS5Padding";

    private static final String CIPHER_FINAL_ENCODING = "UTF-8";

    private static final String ENCODING_METHOD = "AES";

    public String encodeUnique(String targetEncodeString) {
        try {
            iv = secretKey.substring(0, IV_LENGTH);

            byte[] keyData = secretKey.getBytes();

            SecretKey secureKey = new SecretKeySpec(keyData, ENCODING_METHOD);

            Cipher c = Cipher.getInstance(AES_METHOD);
            c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));

            byte[] encrypted = c.doFinal(targetEncodeString.getBytes(CIPHER_FINAL_ENCODING));
            String enStr = new String(Base64.getEncoder().encode(encrypted));

            return enStr;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decodeUnique(String targetDecodeString) {
        try {
            iv = secretKey.substring(0, IV_LENGTH);

            byte[] keyData = secretKey.getBytes();
            SecretKey secureKey = new SecretKeySpec(keyData, ENCODING_METHOD);
            Cipher c = Cipher.getInstance(AES_METHOD);
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes(CIPHER_FINAL_ENCODING)));

            byte[] byteStr = Base64.getDecoder().decode(targetDecodeString.getBytes());

            return new String(c.doFinal(byteStr), CIPHER_FINAL_ENCODING);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
