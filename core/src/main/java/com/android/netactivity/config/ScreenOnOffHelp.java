package com.android.netactivity.config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.netactivity.app.CoreApplication;

/**
 * Created by renmingming on 16/2/14.
 * 锁屏监听类
 */
public class ScreenOnOffHelp
{

    private static Context mContext;

    private ScreenBroadcastReceiver mScreenReceiver;

    private static ScreenOnOffHelp mScreenOnOffHelp;
    private class ScreenBroadcastReceiver extends BroadcastReceiver
    {
        private String action = null;


        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // 开屏
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 锁屏
                CoreApplication.getInstance().unregisterWindowUtil();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                // 解锁
            }
        }
    }
    private void startScreenBroadcastReceiver() {
        mScreenReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver, filter);
    }

    private ScreenOnOffHelp(){

    }
    public static ScreenOnOffHelp ScreenOnOffHelpInstance(Context context){
        mContext = context;
        mScreenOnOffHelp = new ScreenOnOffHelp();
        mScreenOnOffHelp.startScreenBroadcastReceiver();
        return mScreenOnOffHelp;
}
    public void stopScreenBroadcastReceiver(){
        mContext.unregisterReceiver(mScreenReceiver);
    }
}
