package cn.eflo.managewatermeter.util;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.text.TextUtils;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by billyyoyo on 16-2-4.
 */
public class Util {

    static String[] unitSizes = new String[]{"B", "K", "M", "G", "T"};

    public static boolean isInteger(String num) {
        if (num == null || num.length() == 0) return false;
        if (num.startsWith("-")) num = num.substring(1);
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        if (b && Integer.MAX_VALUE > Long.parseLong(num))
            return b;
        else
            return false;
    }

    public static boolean isLong(String num) {
        if (num == null || num.length() == 0) return false;
        if (num.startsWith("-")) num = num.substring(1);
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        if (b && Long.MAX_VALUE > Long.parseLong(num))
            return b;
        else
            return false;
    }

    public static String parseFileName(String url) {
        Matcher m = Pattern.compile("(/file)(.*?)($)").matcher(url);
        if (m.find()) {
            String str = m.group();
            if (str.contains("?")) {
                str = str.substring(0, str.indexOf("?"));
            }
            str = str.substring(str.lastIndexOf("/") + 1);
            return str;
        } else if (url.contains("/")) {
            String str = url.substring(url.lastIndexOf("/") + 1);
            return str;
        }
        return null;
    }

    public static int getRandomColor() {
        Random random = new Random();
        int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        return color;
    }

    public static String joinString(Object[] arr, String split) {
        String result = "";
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                if (i > 0)
                    result += split;
                if (arr[i] != null && arr[i].toString().length() > 0)
                    result += arr[i].toString();
            }
        }
        return result;
    }

    public static String joinString(long[] arr, String split) {
        String result = "";
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                if (i > 0)
                    result += split;
                result += arr[i];
            }
        }
        return result;
    }

    public static String joinString(List<String> arr, String split) {
        String result = "";
        if (arr != null && arr.size() > 0) {
            for (int i = 0; i < arr.size(); i++) {
                if (i > 0)
                    result += split;
                if (arr.get(i) != null && arr.get(i).toString().length() > 0)
                    result += arr.get(i).toString();
            }
        }
        return result;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static boolean validatePhoneNumber(String num) {
        if (num == null) return false;
        num = num.replace("(", "");
        num = num.replace(")", "");
        num = num.replace("-", "");
        num = num.replace(" ", "");
        if (num.length() == 10 || num.length() == 11) {
            return validateNumber(num);
        } else {
            return false;
        }
    }

    public static boolean validateIntArray(String str) {
        if (TextUtils.isEmpty(str)) return false;
        if (!str.endsWith(",")) str += ",";
        return Pattern.compile("([0-9]+,)*").matcher(str).matches();
    }

    public static boolean validateNumber(String num) {
        if (num == null || num.length() == 0) return false;
        if (num.startsWith("-")) num = num.substring(1);
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        return b;
    }

    public static boolean validateFloat(String num) {
        if (num == null || num.length() == 0) return false;
        Pattern p = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        return b;
    }

    public static boolean validateEthAddress(String address) {
        if (address == null || address.length() == 0) return false;
        Pattern p = Pattern.compile("[0-9abcdefx]+");
        Matcher m = p.matcher(address);
        boolean b = m.matches();
        return b;
    }

    public static boolean validateUrl(String str) {
        return (str != null && (str.startsWith("http://") || str.startsWith("https://")));
    }

    public static String findUrl(String str) {
        String pattern = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
        Matcher m = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(str);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    public static boolean validateLocation(Double lt, Double lg) {
        if (lt == null || lg == null) return false;
        if (lt < -90 || lt > 90
                || lg < -180 || lg > 180
                || lt.doubleValue() == 0d || lg.doubleValue() == 0d) {
            return false;
        } else {
            return true;
        }
    }

    public static double distance(double lat1, double log1, double lat2, double log2) {
        if (lat1 < -90 || lat1 > 90) return -1;
        if (log1 < -180 || log1 > 180) return -1;
        if (lat1 == 0 || log1 == 0) return -1;
        if (lat2 < -90 || lat2 > 90) return -1;
        if (log2 < -180 || log2 > 180) return -1;
        if (lat2 == 0 || log2 == 0) return -1;
        float[] results = new float[1];
        Location.distanceBetween(lat1, log1, lat2, log2, results);
        return results[0];
    }

    public static String parseDouble(double num, String fmt) {
        DecimalFormat df = new DecimalFormat(fmt);
        return df.format(num);
    }

    public static String parseInt(int num, String fmt) {
        DecimalFormat df = new DecimalFormat(fmt);
        return df.format(num);
    }

    public static double formatDouble(double num, String fmt) {
        DecimalFormat df = new DecimalFormat(fmt);
        String temp = df.format(num);
        return Double.parseDouble(temp);
    }

    public static String getUnique() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String convertDistance(Context context, Double distance) {
        return convertKm(distance);
    }

    public static String getFileSize(long size) {
        return getFileSize(0, size);
    }

    private static String getFileSize(int i, long size) {
        if (size > 1000) {
            return getFileSize(++i, size / 1000);
        } else {
            Double d = new Double(size);
            DecimalFormat df = new DecimalFormat("0.##");
            String fmt = df.format(d);
            return fmt + unitSizes[i];
        }
    }

    public static String convertMiles(Double mile) {
        if (mile < 0) return "";
        mile = mile / 1609.344;
        String kilo = mile > 1000 ? "k" : "";
        mile = mile > 1000 ? mile / 1000 : mile;
        DecimalFormat df = new DecimalFormat(mile > 10 ? "0" : "0.#");

        String fmt = df.format(mile);
        return fmt + kilo + " miles";
    }

    public static String convertKm(Double km) {
        if (km < 0) return "";
        km = km / 1000;
        String kilo = km > 1000 ? "千" : "";
        km = km > 1000 ? km / 1000 : km;
        DecimalFormat df = new DecimalFormat(km > 10 ? "0" : "0.#");
        String fmt = df.format(km);
        return fmt + kilo + " 公里";
    }

    public static String floatString(Integer num) {
        if (num > 1000) {
            float no = new Float(num) / 1000;
            DecimalFormat df = new DecimalFormat(no > 10 ? "0" : "0.#");
            return df.format(no) + "k";
        } else {
            return num + "";
        }
    }

    public static int parseColor(String clr) {
        if (clr.startsWith("#")) {
            clr = clr.substring(1);
        }
        if (clr.length() < 7) {
            return Color.rgb(
                    Integer.valueOf(clr.substring(0, 2), 16),
                    Integer.valueOf(clr.substring(2, 4), 16),
                    Integer.valueOf(clr.substring(4, 6), 16));
        } else {
            return Color.argb(
                    Integer.valueOf(clr.substring(0, 2), 16),
                    Integer.valueOf(clr.substring(2, 4), 16),
                    Integer.valueOf(clr.substring(4, 6), 16),
                    Integer.valueOf(clr.substring(6, 8), 16));

        }
    }

    public static int parseAlphaColor(String clr, int alpha) {
        if (clr.startsWith("#")) {
            clr = clr.substring(1);
        }
        if (clr.length() < 7) {
            return Color.argb(
                    alpha,
                    Integer.valueOf(clr.substring(0, 2), 16),
                    Integer.valueOf(clr.substring(2, 4), 16),
                    Integer.valueOf(clr.substring(4, 6), 16));
        } else {
            return Color.argb(
                    Integer.valueOf(clr.substring(0, 2), 16),
                    Integer.valueOf(clr.substring(2, 4), 16),
                    Integer.valueOf(clr.substring(4, 6), 16),
                    Integer.valueOf(clr.substring(6, 8), 16));

        }
    }

    public static long[] toPrimitives(Long... objects) {
        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];
        return primitives;
    }

    public static Map<String, String> parseUrlParas(String paraText) {
        Map<String, String> paras = new HashMap<>();
        String[] array = paraText.split("&");
        for (String section : array) {
            String[] kv = section.split("=");
            paras.put(kv[0].toLowerCase(), kv[1]);
        }
        return paras;
    }

    public static String toGBK(String input){
        return new String(input.getBytes(Charset.forName("latin1")), Charset.forName("GBK"));
    }

}
