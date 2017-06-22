package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Fresher on 22/06/2017.
 */

public class MD5Utils {

    public static String hashStringToMD5(String source) {
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] stringBytes = source.getBytes("UTF-8");
            byte[] resultBytes= MD5.digest(stringBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < resultBytes.length; ++i) {
                sb.append(Integer.toHexString((resultBytes[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
            //return null;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            exception.printStackTrace();
            return null;
        }
    }



}
