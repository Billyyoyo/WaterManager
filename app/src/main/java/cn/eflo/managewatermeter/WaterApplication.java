package cn.eflo.managewatermeter;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;

import java.util.LinkedList;

import cn.eflo.managewatermeter.util.CrashHandler;
import cn.eflo.managewatermeter.util.FileUtil;
import cn.eflo.managewatermeter.util.SystemUtil;
import cn.eflo.managewatermeter.util.WLog;

public class WaterApplication extends Application {

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        FileUtil.SDCARD_DIR = context.getFilesDir().getAbsolutePath();
        FileUtil.ROOT_DIR = "WaterManager";
        WLog.DEBUG(SystemUtil.isDebuggable(context));
        WLog.OUTPUT(WLog.LOGCAT);
        WLog.CRASH(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!SystemUtil.isDebuggable(this)) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        } else {
            ActiveAndroid.setLoggingEnabled(true);
        }
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }


}
