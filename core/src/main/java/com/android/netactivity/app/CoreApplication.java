package com.android.netactivity.app;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import com.android.netactivity.config.AppProfile;
import com.rt.sc.config.CorePreferences;
import com.android.netactivity.net.RequestManager;
import com.android.netactivity.net.VolleyUtil;

import java.util.ArrayList;

/**
 * Created by renmingming on 15/9/10.
 *
 */
public class CoreApplication extends Application {

    private static final String TAG = CoreApplication.class.getSimpleName();

    public static Handler mHandler;

    private static CoreApplication coreApplication;

    private ArrayList<WindowUtils> windowUtils = new ArrayList<WindowUtils>();

    private ArrayList<Activity> activities = new ArrayList<Activity>();


    public static synchronized CoreApplication getInstance() {

        return coreApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        coreApplication = this;

    }


    private void init() {

        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(this);

        VolleyUtil.getInstance(this);
        mHandler = new Handler();
        RequestManager.init(this);

        CorePreferences.getInstance(this).initConfig(this);
        // 初始化应用信息
        AppProfile.getInstance(this).init();


    }


    public void registerWindowUtil(WindowUtils windowUtil) {
        windowUtils.add(windowUtil);
    }

    public void unregisterWindowUtil() {
        for (WindowUtils windowUtil : windowUtils) {
            windowUtil.hidePopupWindow(true);
        }
    }

    public static Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void finishAll(){
        for(Activity activity : activities){
            activity.finish();
        }
    }


}
