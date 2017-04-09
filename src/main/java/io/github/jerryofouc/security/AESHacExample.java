package io.github.jerryofouc.security;

import com.cisco.cca.algorithm.crypto.AESWithHmacSHA256Crypto;
import com.cisco.cca.exception.CryptoException;
import com.cisco.cca.key.Key;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by xiaojiez on 4/8/17.
 */
public class AESHacExample {
    public static void main(String[] args) throws NoSuchAlgorithmException, CryptoException {
        byte[] keyBytes = new byte[32];
        for(int i=0;i<keyBytes.length;i++){
            keyBytes[i] = (byte) i;
        }

        Key key = new Key("SessionKey","SK".getBytes(),keyBytes,0);

        AESWithHmacSHA256Crypto aesWithHmacSHA256Crypto = new AESWithHmacSHA256Crypto();
        byte[] encrypted = aesWithHmacSHA256Crypto.encrypt("aaa".getBytes(),key);

        byte[] encriptData = Base64.getDecoder().decode("QUhTSwAAAABiNS9xPRT5NiBALKHGUMprsB7RHUJQxVHAU3s2LihKB2pHwWFMRU7q8YvtjdaIYi0rVnAmeISqs0JecS0+h5BU");
        System.out.println(new String(aesWithHmacSHA256Crypto.decrypt(encriptData,key)));

    }
}
