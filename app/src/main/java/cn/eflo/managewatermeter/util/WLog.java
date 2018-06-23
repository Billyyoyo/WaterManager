package cn.e_flo.managewatermeter.util;

import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;

/**
 * Created by billyyoyo on 16-1-27.
 */
public class WLog {

    public static final int LOGCAT = 0;
    public static final int LOGFILE = 1;

    private static int OUTPUT = LOGCAT;

    private static boolean DEBUG = true;

    private static boolean CRASH = true;

    private static File logfile;

    public static void CRASH(boolean b) {
        CRASH = b;
        createLogFile();
    }

    public static void DEBUG(boolean d) {
        DEBUG = d;
    }

    public static void OUTPUT(int o) {
        OUTPUT = o;
        if (o == LOGFILE && logfile == null) {
            createLogFile();
        }
    }

    public static void createLogFile() {
        File dir = FileUtil.getDirectory(FileUtil.LOG_DIR);
        logfile = new File(dir, DateUtil.getToday() + ".log");
        String path = logfile.getAbsolutePath();
        FileUtil.createFile(path);
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            if (OUTPUT == LOGCAT) {
                Log.i(tag, message);
            } else {
                writeLog("info", tag, message);
            }
        }
    }

    public static void w(String tag, String message) {
        if (OUTPUT == LOGCAT) {
            Log.w(tag, message);
        } else {
            writeLog("warn", tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (OUTPUT == LOGCAT) {
            Log.e(tag, message);
        } else {
            writeLog("error", tag, message);
        }
    }

    public static void c(String message) {
        if (!CRASH) {
            Log.e("Crash", message);
        } else {
            writeLog("error", "Crash", message);
        }
    }

    public static void writeLog(String level, String tag, String message) {
        FileUtil.writeStringToFile(level + "  " +
                DateFormat.format("yyyy-MM-dd HH:mm:ss.SSS", System.currentTimeMillis()) + "  " + tag + ": " + message + "\n", logfile, true);
    }

}
