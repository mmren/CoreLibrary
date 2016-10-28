package com.android.netactivity.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.netactivity.R;
import com.android.netactivity.net.GsonRequest;
import com.android.netactivity.net.NetBean;
import com.rt.sc.tools.NetLoadingDialog;
import com.rt.sc.tools.StringUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by renmingming on 16/4/11.
 * 网络框架类
 */
public abstract class NetManagerActivity extends AppCompatActivity implements NetInterFace {


    public Handler mHandel;

    protected NetInterFace mNet;

    protected boolean isDialogShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNet = NetImpl.regist(this, this);
        mHandel = new MyHandler(this, mNet);

    }


    private void registmNetImpl(NetInterFace netImpl) {
        mNet = netImpl;
    }

    public abstract <D extends NetBean> boolean doAfterErrorImpl(String urlTag, D data);

    public abstract <D extends NetBean> void doAfterSuccessImpl(String urlTag, D data);


    public <D extends NetBean> GsonRequest inStanceGsonRequest(String urlstr, String tag, Class<D> data, Map<String, String> params, Map<String, String> headers, boolean isNeedShowDialog, boolean canDialogBackClose) {
        return mNet.inStanceGsonRequest(urlstr, tag, data, params, headers, isNeedShowDialog, canDialogBackClose);
    }


    public <D extends NetBean> boolean doAfterError(String urlTag, D data) {
        mNet.doAfterError(urlTag, data);
        return doAfterErrorImpl(urlTag, data);
    }

    public <D extends NetBean> void doAfterSuccess(String urlTag, D data) {
        mNet.doAfterSuccess(urlTag, data);
        doAfterSuccessImpl(urlTag, data);
    }

    public synchronized void showProcessDialog(String tag, boolean canBackDismiss) {
        if (!isDialogShowing) {
            isDialogShowing = true;
            mNet.setCurrentUrlTag(tag);
            mNet.showProcessDialog(tag, canBackDismiss);
        }
    }

    public void dismissProcessDialog(String tag) {
        if (isDialogShowing) {
            isDialogShowing = false;
            mNet.dismissProcessDialog(tag);
        }
    }

    static class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;
        private NetInterFace mNet;

        MyHandler(Activity activity, NetInterFace mNet) {
            mActivityReference = new WeakReference<>(activity);
            this.mNet = mNet;
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                if (mNet == null) {
                    return;
                }
                try {
                    switch (NetStatusEnum.toLable(msg.what + "")) {

                        case startandcancancle:
                            mNet.showProcessDialog((String) msg.obj, true);
                            break;

                        case startandcannotcancle:
                            mNet.showProcessDialog((String) msg.obj, false);
                            break;

                        case errorandfinish:
                            mNet.dismissProcessDialog((String) msg.obj);
                            activity.finish();
                            break;

                        case error:
                            mNet.dismissProcessDialog((String) msg.obj);
                            break;

                        case errorandToast:
                            mNet.dismissProcessDialog("");
                            if (msg.obj != null && msg.obj instanceof String) {
                                String content = (String) msg.obj;
                                if (!StringUtils.isEmpty(content)) {
                                    Toast.makeText(activity, content, Toast.LENGTH_LONG).show();
                                }
                            }
                            break;

                        case errorandshowDialog:
                            mNet.dismissProcessDialog("");
                            if (msg.obj != null && msg.obj instanceof String) {
                                String content = (String) msg.obj;
                                if (!StringUtils.isEmpty(content)) {
                                    mNet.showLoginConfirm((String) msg.obj, 0);
                                }
                            }

                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mNet.cancelRequests();
        super.onDestroy();

    }



}
