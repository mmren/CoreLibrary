package com.android.netactivity.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.netactivity.R;
import com.android.netactivity.net.GsonRequest;
import com.android.netactivity.net.NetBean;
import com.android.netactivity.net.VolleyUtil;
import com.rt.sc.tools.HttpUtil;
import com.rt.sc.tools.LogUtil;
import com.rt.sc.tools.NetLoadingDialog;

import java.util.HashMap;
import java.util.Map;


public class NetImpl implements NetInterFace {

    private NetManagerActivity mActivity;

    private String mCurrentUrlTag;

    private NetInterFace mNetInterFace;

    private final Map<String, Request> requests = new HashMap<>();


    private NetImpl(NetManagerActivity mActivity, NetInterFace mNetInterFace) {

        this.mActivity = mActivity;
        this.mNetInterFace = mNetInterFace;
    }

    public static synchronized NetImpl regist(NetManagerActivity activity, NetInterFace mNetInterFace) {

        return new NetImpl(activity, mNetInterFace);

    }


    public <D extends NetBean> GsonRequest inStanceGsonRequest(String urlstr, final String tag, final Class<D> data, Map<String, String> params, Map<String, String> headers, boolean isNeedShowDialog
            , boolean canDialogBackClose) {


        if (HttpUtil.isConnnected(mActivity)) {

            final boolean isNeedShowDialog_f = isNeedShowDialog;
            if (isNeedShowDialog_f) {

                mNetInterFace.showProcessDialog(tag, canDialogBackClose);
            }
            final Map<String, String> params_fix = new HashMap<>();
            for (String key : params.keySet()) {

                if (params.get(key) != null) {
                    params_fix.put(key, params.get(key));
                }
            }
            VolleyUtil vu = VolleyUtil.getInstance(mActivity);

            String url = vu.baseUrl + urlstr;


            GsonRequest<D> gsonRequest = new GsonRequest<D>(Request.Method.POST, url, data, headers,
                    new Response.Listener<D>() {

                        boolean doresult = false;

                        @Override
                        public void onResponse(D response) {


                            LogUtil.v("", "response::====" + response.toString());

                            if (isNeedShowDialog_f && tag.equals(mCurrentUrlTag)) {
                                mNetInterFace.dismissProcessDialog(tag);
                            }
                            if (response != null && data.isInstance(response)) {

                                response.saveToke();

                                if (response.isJumpLogin()) {
                                    showLoginConfirm(response.getRetinfo(), 1);
                                } else if (response.isExpire()) {
                                    goToLogin();
                                }
                                if (response.isSuccess()) {
                                    mNetInterFace.doAfterSuccess(tag, response);
                                } else {
                                    doresult = mNetInterFace.doAfterError(tag, response);
                                    if (!doresult) {
                                        doresult = mNetInterFace.doAfterError(tag);
                                    }
                                }
                                if (isNeedShowDialog_f && !doresult && response.isFailed()) {
                                    Toast.makeText(mActivity, response.getRetinfo(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                LogUtil.v("", "response::====" + response);
                            }

                        }
                    },
                    errorListener(tag, isNeedShowDialog_f)) {
                protected Map<String, String> getParams() {
                    return params_fix;
                }
            };
            gsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 0));
            requests.put(tag, gsonRequest);
            LogUtil.v("", "Request::====" + gsonRequest.getUrl());
            return gsonRequest;
        } else {
            mNetInterFace.doAfterError(tag);
            if (isNeedShowDialog) {
                Toast.makeText(mActivity, R.string.net_erro, Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }


    public void showProcessDialog(final String tag, final boolean canBackDismiss) {
        LogUtil.i(this.getClass().getName(), tag);
        final Dialog dialog = NetLoadingDialog.getInstance().loading(mActivity, null, canBackDismiss);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (canBackDismiss) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (requests.get(tag) != null) {
                            requests.get(tag).cancel();
                        }

                    }
                }
                return true;
            }
        });
    }


    public void dismissProcessDialog(String tag) {
        LogUtil.i(this.getClass().getName(), tag + "");
        NetLoadingDialog.getInstance().dismissDialog();
    }

    @Override
    public void showLoginConfirm(String content, int backtohome) {

    }


    @Override
    public Response.ErrorListener errorListener(final String urlTag, final boolean isNeedShowDialog) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("", "response::===error=" + error != null ? error.toString() : "");

                if (isNeedShowDialog && urlTag.equals(mCurrentUrlTag)) {
                    mNetInterFace.dismissProcessDialog(urlTag);
                }
                if (error instanceof TimeoutError && isNeedShowDialog) {
                    Toast.makeText(mActivity, "网络延迟", Toast.LENGTH_SHORT).show();
                } else if (isNeedShowDialog) {
                    Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    @Override
    public void cancelRequests() {
        for (Request request : requests.values()) {
            request.cancel();
        }

    }

    @Override
    public void goToLogin() {

    }

    @Override
    public void goToHome() {

    }

    @Override
    public String getCurrentUrlTag() {
        return mCurrentUrlTag;
    }

    @Override
    public void setCurrentUrlTag(String tag) {
        this.mCurrentUrlTag = tag;
    }

    @Override
    public <D extends NetBean> void doAfterSuccess(String urlTag, D data) {

    }


    @Override
    public boolean doAfterError(String urlTag) {
        return false;
    }

    @Override
    public <D extends NetBean> boolean doAfterError(String urlTag, D data) {

        return false;
    }


}
