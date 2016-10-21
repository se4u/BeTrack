package com.app.uni.betrack;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by cevincent on 15/10/2016.
 */

public class UtilsCryptoRSA {
    static final String TAG = "UtilsCryptoRSA";

    private SecretKeySpec skeySpec;
    private Cipher cipher;


    public static String encryptSessionKeyWithPublicKey(String pemString, byte[] sessionKey, Context context) {
        try {
            PublicKey publicKey = getPublicKeyFromPemFormat(pemString, context);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherData = cipher.doFinal(sessionKey);
            Log.e(TAG, new String(Base64.encode(cipherData, Base64.CRLF)));
            return new String(Base64.encode(cipherData, Base64.URL_SAFE | Base64.CRLF));
        } catch (IOException ioException) {
            Log.e(TAG, "ioException");
        } catch (NoSuchAlgorithmException exNoSuchAlg) {
            Log.e(TAG, "NoSuchAlgorithmException");
        } catch (javax.crypto.NoSuchPaddingException exNoSuchPadding) {
            Log.e(TAG, "NoSuchPaddingException");
        } catch (java.security.InvalidKeyException exInvalidKey) {
            Log.e(TAG, "InvalidKeyException");
        } catch (javax.crypto.IllegalBlockSizeException exIllBlockSize) {
            Log.e(TAG, "IllegalBlockSizeException");
        } catch (javax.crypto.BadPaddingException exBadPadding) {
            Log.e(TAG, "BadPaddingException");
        }
        return null;
    }

    private static PublicKey getPublicKeyFromPemFormat(String pemString, Context context)
            throws IOException, NoSuchAlgorithmException
    {
        PublicKey key = null;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(pemString);
        BufferedReader pemReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = pemReader.readLine()) != null) {
            if (line.indexOf("-----BEGIN PUBLIC KEY-----") != -1) {
                while ((line = pemReader.readLine()) != null) {
                    if (line.indexOf("-----END PUBLIC KEY") != -1) {
                        break;
                    }
                    content.append(line.trim());
                }
                break;
            }
        }
        if (line == null) {
            throw new IOException("PUBLIC KEY not found");
        }

        Log.e(TAG, "PUBLIC KEY: " + content.toString());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        try {
            key = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(content.toString(), Base64.CRLF)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return key;
    }
}
