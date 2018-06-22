package cn.e_flo.managewatermeter.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by billyyoyo on 16-1-27.
 */
public class CodeUtil {
//    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//    private static final String SECURITY_KEY = "z]o04/.diy,df*s-";
    private static final String SECURITY_KEY = "0123456789ABCDEF";

    public static String getEncodedMD5(String clearText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(clearText.getBytes("UTF-8"));
            return hexEncode(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
            return null;
        } catch (UnsupportedEncodingException ex1) {
            System.err.println(ex1);
            return null;
        }
    }

    public static String hexEncode(byte[] input) {
        return encodeHexString(input);
    }

    public static String encodeHexString(byte[] data) {
        return new String(encodeHex(data));
    }

    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, SECURITY_KEY.toCharArray());
    }

    public static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }


    public static void main(String[] args) {
        System.out.println(getEncodedMD5("管理员"));
//        String salt = "BARNEScom.cybeye.naccNOBLE";
//        String str = "7375627a2762515a5c4b521d53564a57165d040105777d277a76613332396434";
////        String salt = "BARNEScom.cybeye.liveboxNOBLE";
////        String str = "7272642f2667050e5419554b00004c524b0e0d1253565f48772a737e24326237";
//        String secret = decodeSecret(salt, str);
//        System.out.println(secret);

//        String str = "1404b1251e1d133283ebf92e63a329d4";
//        String encrypt = encodeSecret(salt, str);
//        System.out.println(encrypt);
    }

}
