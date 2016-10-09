package com.app.uni.betrack;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by cevincent on 09/10/2016.
 */

public class UtilsCipher {
    static final String TAG = "UtilsCipher";
    private final static String ALGORITHM = "AES";
    private String mySecret;

    public UtilsCipher(String mySecret){
        this.mySecret = mySecret;
    }

    public String encryptUTF8(String data){
        try{
            int Align64Bits = data.length() % 4;
            if (null == data) data = "0000";
            for (int i = 0; i < Align64Bits; i++) {
                data = " " + data;
            }
            byte[] bytes = data.toString().getBytes("utf-8");
            byte[] bytesBase64 = android.util.Base64.encode(bytes, Base64.DEFAULT);
            return encrypt(bytesBase64);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //AES
    private String encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] data = cipher.doFinal(clear);
        byte[] encodedBytes = android.util.Base64.decode(data, Base64.DEFAULT);
        String encrypted_data = new String(encodedBytes, Charset.forName("UTF8"));

        return encrypted_data;
    }

    private byte[] getKey() throws Exception{
        byte[] key = this.mySecret.getBytes("utf-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32); // 256 bits key
        return key;
    }
    ////////////////////////////////////////////////////////////
    private String encrypt(byte[] data) throws Exception{
        return encrypt(getKey(),data);
    }

}
