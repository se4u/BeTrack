package com.app.uni.betrack;

/**
 * Created by cevincent on 7/1/16.
 */
import android.content.Context;
import java.util.UUID;

/**
 * Generates unique device id
 */
public class UtilsDeviceIdGenerator {

    //private static final String EMULATOR_ANDROID_ID = "9774d56d682e549c";
    //private static final String[] BAD_SERIAL_PATTERNS = {"1234567", "abcdef", "dead00beef"};

    public static String readDeviceId(Context context) {
        String deviceId;
        //String androidSerialId = null;
        //try {
            // try to get device serial number
            //androidSerialId = Build.SERIAL;
        //} catch (NoSuchFieldError ignored) {
        //}

        //if (!TextUtils.isEmpty(androidSerialId) && !Build.UNKNOWN.equals(androidSerialId) && !isBadSerial(androidSerialId)) {
        //    deviceId = androidSerialId;
        //} else {
            // try to use Settings.Secure.ANDROID_ID
            // could be different after factory reset
            //String androidSecureId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            /*if (!TextUtils.isEmpty(androidSecureId) && !EMULATOR_ANDROID_ID.equals(androidSecureId) && !isBadDeviceId(androidSecureId)
                    && androidSecureId.length() == EMULATOR_ANDROID_ID.length()) {
                deviceId = androidSecureId;
            } else {*/
                deviceId = UUID.randomUUID().toString();
            //}
        //}
        return UUID.nameUUIDFromBytes(deviceId.getBytes()).toString();
    }
/*
    private static boolean isBadDeviceId(String id) {
        // empty or contains only spaces or 0
        return TextUtils.isEmpty(id) || TextUtils.isEmpty(id.replace('0', ' ').replace('-', ' ').trim());
    }

    private static boolean isBadSerial(String id) {
        if (!TextUtils.isEmpty(id)) {
            id = id.toLowerCase();
            for (String pattern : BAD_SERIAL_PATTERNS) {
                if (id.contains(pattern)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }
*/
}