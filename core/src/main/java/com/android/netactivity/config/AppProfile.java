package com.android.netactivity.config;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.netactivity.R;
import com.rt.sc.tools.ACache;
import com.rt.sc.tools.DataCleanUtils;
import com.rt.sc.tools.PackageUtils;


/**
 * Created by renmingming on 15/9/21.
 *
 */
public class AppProfile {

    private static final boolean DEBUG = true;

    private static final String TAG = "AppProfile";

    private static Context mContext;

    private static AppProfile appProfile;

    public static final String Preferences_Boolean_Current_Verison_Is_Init = "AppProfile.Preferences_Boolean_Current_Verison_Is_Init";

    public static final String Preferences_Int_Current_Verison_Code = "Preferences_Int_Current_Verison_Code";

    private static final String ADDRESSVERSION = "addressVersion";

    private String addressVersion;

    private static ACache aCache;

    public static boolean isAsyn = true;// 是否需要后台同步账户信息

    public static boolean isLoginPage = false;// 是否停留在登录页

    private static SharedPreferences mPreferences;


    public static final String Preferences_Boolean_IsFirst_Guide = "com.zd.maiya.Preferences_Boolean_IsFirst_Guide";//第一次打开，显示引导页


    public AppProfile(Context context) {
        mContext = context;
        if (mPreferences == null) {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        if (aCache == null) {
            aCache = ACache.get(context);
        }
    }

    public static AppProfile getInstance(Context context) {
        if (DEBUG) Log.v(TAG, "getProfile(context)");
        if (appProfile == null) {
            appProfile = new AppProfile(context);
        }
        return appProfile;
    }

    public SharedPreferences getCoreConfig() {
        return mPreferences;
    }

    /**
     * 初始化程序.
     */
    public void init() {
        Log.v(TAG, "init()");
        int newVersion = PackageUtils.getLocalVersionCode(mContext);
        String isInitKey = Preferences_Boolean_Current_Verison_Is_Init + "_" + newVersion;
        boolean mIsInit = mPreferences.getBoolean(isInitKey, false);
        if (!mIsInit && mContext != null) {
            int oldVersion = mPreferences.getInt(Preferences_Int_Current_Verison_Code, -1);
            if (oldVersion == -1) {
                BasePackageInfoCompat IMPL;
                IMPL = new GBPackageInfoCompat(mContext);
                if (IMPL.isUpgrade()) {
                    onUpgrade(mContext, oldVersion, newVersion);
                }
                mPreferences.edit().putInt(Preferences_Int_Current_Verison_Code, newVersion).commit();
            } else if (newVersion > oldVersion) {
                onUpgrade(mContext, oldVersion, newVersion);
                mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                mPreferences.edit().putInt(Preferences_Int_Current_Verison_Code, newVersion).commit();
            }
            mPreferences.edit().putBoolean(isInitKey, true).commit();
        }
    }

    public static class BasePackageInfoCompat {
        protected Context mContext;

        public BasePackageInfoCompat(Context context) {
            this.mContext = context;
        }

        public boolean isUpgrade() {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static class GBPackageInfoCompat extends BasePackageInfoCompat {
        public GBPackageInfoCompat(Context context) {
            super(context);
        }

        public boolean isUpgrade() {
            if (Build.VERSION.SDK_INT >= 9) {
                PackageManager packageManager = mContext.getPackageManager();
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                    long firstInstallTime = packageInfo.firstInstallTime;
                    long lastUpdateTime = packageInfo.lastUpdateTime;
                    if (lastUpdateTime > firstInstallTime) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return false;
            }

            return false;
        }
    }

    /**
     * 清理之前版本的存留数据,任何与当前版本不兼容的数据都应在次方中中清除或者升级.
     *
     * @param context
     */
    public void onUpgrade(Context context, int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgrade(context, oldVersion=" + oldVersion + ", newVersion=" + newVersion + ")");
        try {
            // 清理之前版本的数据.
            DataCleanUtils.cleanFiles(context);
            DataCleanUtils.cleanDatabases(context);
            DataCleanUtils.cleanInternalCache(context);
            DataCleanUtils.cleanSharedPreference(context);
        } catch (Exception e) {
            // 清除数据,不希望有错!
        }
    }


    public String getAddressVersion() {
        if (addressVersion != null) {
            return addressVersion;

        } else {
            addressVersion = ((String) aCache.getAsObject(ADDRESSVERSION));
            return addressVersion;
        }
    }

    public void setAddressVersion(@NonNull String addressVersion) {
        this.addressVersion = addressVersion;

        aCache.put(ADDRESSVERSION, addressVersion);
    }


    /**
     * 获取渠道包
     *
     * @return
     */
    public String getChannelCode() {
        String code = getMetaData("TD_CHANNEL_ID");
        if (code != null) {
            return code;
        }
        return mContext.getResources().getString(R.string.test);

    }

    public String getMetaData(String key) {
        try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {

        }
        return null;
    }


    //设置是否是第一次打开，显示引导页
    public void setGuideFirst() {
        if (DEBUG) Log.v(TAG, "setGuideFirst()");
        mPreferences.edit().putBoolean(Preferences_Boolean_IsFirst_Guide, false).commit();
    }

    public void setGuideFirst(boolean isGuideFirst) {
        if (DEBUG) Log.v(TAG, "setGuideFirst()");
        mPreferences.edit().putBoolean(Preferences_Boolean_IsFirst_Guide, isGuideFirst).commit();
    }

    public boolean isGuideFirst() {
        if (DEBUG) Log.v(TAG, "isGuideFirst()");
        return mPreferences.getBoolean(Preferences_Boolean_IsFirst_Guide, true);
    }


}
