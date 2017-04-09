package io.github.jerryofouc.security;

import com.cisco.webex.security.crypto.exceptions.WbxSecException;
import com.cisco.webex.security.crypto.symmetric.AESEncryptionEngine;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by xiaojiez on 4/8/17.
 */
public class DigestTest {
    public static void main(String[] args) throws WbxSecException {
        byte[] keyBytes = new byte[32];

        for(int i=0;i<keyBytes.length;i++){
            keyBytes[i] = (byte) i;
        }

        System.out.println(Base64.encodeBase64String(AESEncryptionEngine.createEncryptiongKey(keyBytes)));
        System.out.println(Base64.encodeBase64String(keyBytes));

    }
}
