package com.android.netactivity.net;

import android.content.Context;

import com.android.netactivity.config.AppProfile;
import com.android.netactivity.R;
import com.rt.sc.tools.AppUtils;
import com.rt.sc.tools.PackageUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by renmingming on 15/9/18.
 * 网络管理工具类
 */
public class VolleyUtil
{
    public static String TESTURL ="https://mobile.maiyabank.com/myLoan-mobile/action/api/";

    public static String REALURL;

    public String baseUrl;

    public static String httpUrl;

    private static final String client = "2";

    private static Context mCtx;

    public static final String CHARACTER_ENCODING = "UTF-8";

    private static VolleyUtil mInstance;


    private VolleyUtil(Context context)
    {
        mCtx = context;
    }

    public static synchronized VolleyUtil getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new VolleyUtil(context);
            String test = context.getResources().getString(R.string.test);
            String channel = AppProfile.getInstance(context).getChannelCode();
            REALURL = AppProfile.getInstance(context).getMetaData("BASEURL");
            if (channel == null || (test != null && test.equals(channel)))
            {
                mInstance.baseUrl = TESTURL;
            } else
            {
                mInstance.baseUrl = REALURL;
            }
        }
        return mInstance;
    }


    public Map<String, String> getHeaders()
    {
        Map<String, String> oList = new HashMap<>();
        oList.put("client", client);
        oList.put("deviceNo", AppUtils.getDeviceId(mCtx));
        oList.put("channel", AppProfile.getInstance(mCtx).getChannelCode());
        oList.put("version", getVersion());
        oList.put("build", PackageUtils.getLocalVersionCode(mCtx) + "");
        oList.put("ip", AppUtils.getIp(mCtx) + "");
        return oList;
    }

    public static Map<String, String> getHeadersStatic()
    {
        if (mInstance != null && mCtx != null)
        {
            return mInstance.getHeaders();
        }
        return null;
    }

    public String getVersion()
    {
        if (mCtx != null)
        {
            String version = PackageUtils.getLocalVersionName(mCtx);
            if (version != null)
            {
                return version;
            }
        }

        return "";
    }





}
