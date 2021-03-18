package ru.pack.csps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pack.csps.crypt.Encryptor;
import ru.pack.csps.util.IEncrypted;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class EncryptorService {

    @Autowired
    private Encryptor encryptor;

    public void encrypt(IEncrypted IEncrypted) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        IEncrypted.encrypt(encryptor);
    }

    public void decrypt(IEncrypted IEncrypted) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        IEncrypted.decrypt(encryptor);
    }

    public void encrypt(List<IEncrypted> IEncrypteds) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        for (IEncrypted e: IEncrypteds) {
            e.encrypt(encryptor);
        }
    }

    public void decrypt(List<IEncrypted> IEncrypteds) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        for (IEncrypted e: IEncrypteds) {
            e.decrypt(encryptor);
        }
    }

    public String encrypt(String str) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return encryptor.encrypt(str);
    }

    public String decrypt(String string) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
         return encryptor.decrypt(string);
    }
}
