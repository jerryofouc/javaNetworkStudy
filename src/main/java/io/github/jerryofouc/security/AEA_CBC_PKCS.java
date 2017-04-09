package io.github.jerryofouc.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by xiaojiez on 4/8/17.
 */
public class AEA_CBC_PKCS {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        byte[] ivKey = new byte[16];
        for(int i=0;i<ivKey.length;i++){
            ivKey[i] = (byte) i;
        }
        byte[] plainText = "zhangxiaojie".getBytes();
        byte[] key = new byte[32];
        for(int i=0;i<key.length;i++){
            key[i] = (byte) i;
        }
        System.out.println(Base64.encodeBase64String(ivKey));
        System.out.println(Base64.encodeBase64String(key));
        System.out.println(Base64.encodeBase64String(encrypt(plainText,key,ivKey)));
    }


    public static byte[] encrypt(byte[] plainText,byte[] key,byte[] ivKey) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParam = new IvParameterSpec(ivKey);
        Cipher newByte = Cipher.getInstance("AES/CBC/PKCS5Padding");
        newByte.init(Cipher.ENCRYPT_MODE, sKeySpec, ivParam);
        return newByte.doFinal(plainText);
    }


}
