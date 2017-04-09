package io.github.jerryofouc.security;

import com.cisco.cca.exception.CryptoException;
import com.cisco.cca.facade.SecureEncryption;

/**
 * Created by xiaojiez on 4/7/17.
 */
public class EncryptionExample {
    public static void main(String[] args) throws CryptoException {
        byte[] bytes = SecureEncryption.encrypt("SessionDefault","abc123".getBytes(),"SessionKey");
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X ", b));
            result.append(" "); // delimiter
        }
        System.out.println(result.toString());
    }
}
