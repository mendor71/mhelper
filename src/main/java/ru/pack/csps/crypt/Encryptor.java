package ru.pack.csps.crypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    private static final String secretKeyKey = "M1ySe!(s*jp^s771";

    private IvParameterSpec iv;
    private SecretKeySpec skeySpec;
    private Cipher cipher;

    private String secretKey;
    private String initVector;

    private boolean init = false;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getInitVector() {
        return initVector;
    }

    public void setInitVector(String initVector) {
        this.initVector = initVector;
    }

    private void init() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec sKeyKeySpec = new SecretKeySpec(secretKeyKey.getBytes("UTF-8"), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

        cipher.init(Cipher.DECRYPT_MODE, sKeyKeySpec, iv);
        byte[] original = cipher.doFinal(Base64.decodeBase64(secretKey));

        secretKey = new String(original);
        skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

        init = true;
    }

    public String encrypt(String source) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (!init) {
            init();
        }

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encoded = cipher.doFinal(source.getBytes());

        return Base64.encodeBase64String(encoded);
    }

    public String decrypt(String encodedSource) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (!init) {
            init();
        }

        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.decodeBase64(encodedSource));
        return new String(original);
    }
}
