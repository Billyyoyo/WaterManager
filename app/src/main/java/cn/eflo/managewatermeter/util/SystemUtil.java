package cn.eflo.managewatermeter.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by billyyoyo on 16-1-27.
 */
public class SystemUtil {


    public static boolean requestInitPermission(Activity context) {
        List<String> permissions = new ArrayList<String>();
        boolean result = true;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            result = false;
        }
        if (permissions.size() > 0) {
            String[] permissionArrays = new String[permissions.size()];
            permissionArrays = permissions.toArray(permissionArrays);
            ActivityCompat.requestPermissions(context,
                    permissionArrays,
                    Constant.INTENT_REQUEST_INIT_PERMISSION);
        }
        return result;
    }

    public static boolean isLandscape(Context context) {
        Configuration cf = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = cf.orientation; //获取屏幕方向
        if (ori == cf.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException nnfe) {
            return 0;
        }
    }

    public static String getApplicationMetaString(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getApplicationMetaInt(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getInt(name)+"";
        } catch (Exception e) {
            e.printStackTrace();
            return "3306";
        }
    }

    public static String getActivityMetaData(Activity activity, String name) {
        try {
            ActivityInfo actInfo = activity.getPackageManager()
                    .getActivityInfo(activity.getComponentName(),
                            PackageManager.GET_META_DATA);
            return actInfo.metaData.getString(name);
        } catch (Exception e) {
//            e.printStackTrace();
            return "";
        }
    }

    public static boolean isSimReady(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        boolean sim = (telephonyManager != null && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY);
        boolean airplane = (Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1);
        return (sim && !airplane);
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static double getScreenInch(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double width = (double) (dm.widthPixels) / (double) (dm.densityDpi);
        double height = (double) (dm.heightPixels) / (double) (dm.densityDpi);
        double size = Math.sqrt(width * width + height * height);
        return size;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

//    public static int getToolbarHeight(Context context) {
//        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
//                new int[]{R.attr.actionBarSize});
//        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
//        styledAttributes.recycle();
//
//        return toolbarHeight;
//    }

    public static void hideSoftKeyboard(TextView textView) {
        if (textView == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(textView.getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(SearchView textView) {
        if (textView == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(textView.getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(TextView textView) {
        if (textView == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(textView.getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(textView, 0);
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null
                && networkinfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;

    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static boolean isScreenOpen(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (powerManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                return powerManager.isInteractive();
            } else {
                return powerManager.isScreenOn();
            }
        }
        return false;
    }

    public static void showSystemStatusBar(Activity activity) {
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getAppCompActivity(activity).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void hideSystemStatusBar(Activity activity) {
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getAppCompActivity(activity).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void hideSystemNavBar(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //       设置屏幕始终在前面，不然点击鼠标，重新出现虚拟按键
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                            // bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
            );
        }
    }

    public static void showSystemNavBar(Context context, int systemUiVisibility) {
        ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    public static boolean checkGPS(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void goSetGPS(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    private static String getLauncherPkgName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            String pkgName = info.processName;
            if (pkgName.contains("launcher") && pkgName.contains("android")) {
                return pkgName;
            }

        }
        return null;
    }

    public static boolean hasShortcut(Context context, String lableName) {
        String launcherPkgName = getLauncherPkgName(context);
        String url = "content://" + launcherPkgName + ".settings/favorites?notify=true";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
                new String[]{lableName}, null);
        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public static void addShortcut(Context context, int appNameRes, int launcherIconRes) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(appNameRes));
        shortcut.putExtra("duplicate", false); //不允许重复创建
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(context, context.getClass().getName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, launcherIconRes);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        context.sendBroadcast(shortcut);

    }

    public static boolean isDebuggable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static String generateUDID(Context context) {
        String udid = "" + System.currentTimeMillis();
//        return CodeUtil.encrypt(udid + "cybeye");
        return CodeUtil.getEncodedMD5(udid + "cybeye");
    }

    public static void saveUDID(Context context, String udid) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            File idFile = new File(FileUtil.getRoot(), "IDENTIFY_TAG");
            if (!idFile.exists()) {
                File parentFile = idFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                try {
                    idFile.createNewFile();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                FileUtil.writeStringToFile(udid, idFile, false);
            }
        }
    }

    public static String loadUDID(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            File idFile = new File(FileUtil.getRoot(), "IDENTIFY_TAG");
            if (idFile.exists()) {
                return FileUtil.readText(idFile);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void copy2Clipboard(Context context, int tipId, String data) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple_text", data);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, tipId, Toast.LENGTH_SHORT).show();
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static void callPhone(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static void downloadFile(Context context, String name, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        File downloadDir = FileUtil.getDirectory(FileUtil.DOWNLOADS);
        downloadDir.mkdirs();
        File destFile = new File(downloadDir, (!TextUtils.isEmpty(name)) ? name : Util.getUnique() + FileUtil.getEXT(url));
        request.setDestinationUri(Uri.fromFile(destFile));
        request.setTitle(TextUtils.isEmpty(name) ? url.substring(url.lastIndexOf("/")) : name);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }

    public static boolean canFloatWindow(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkCpuArchInfo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.getName().contains("CPU_ABI")) {
                    Object obj = field.get(null);
                    if (obj != null && "x86".equals(obj.toString())) {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static String IP2String(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

}
