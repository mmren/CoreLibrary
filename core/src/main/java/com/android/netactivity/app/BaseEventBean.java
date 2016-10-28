package com.android.netactivity.app;

/**
 * Created by renmingming on 16/3/31.
 */
public class BaseEventBean {
    private String mMsg;
    private String mTag;


    public BaseEventBean(String tag, String msg) {
        mMsg = msg;
        mTag = tag;
    }
    public String getMsg(){
        return mMsg;
    }

    public String getmTag() {
        return mTag;
    }
}
