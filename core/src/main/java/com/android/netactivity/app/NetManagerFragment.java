package com.android.netactivity.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.netactivity.net.GsonRequest;
import com.android.netactivity.net.NetBean;

import java.util.Map;

/**
 * Created by renmingming on 15/9/16.
 * 网络框架类
 */

public abstract class NetManagerFragment extends Fragment implements NetInterFace{

    protected NetInterFace mNet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNet = NetImpl.regist((NetManagerActivity)getActivity(),this);

    }
    private void registmNetImpl(NetInterFace netImpl){
        mNet = netImpl;
    }

    public abstract <D extends NetBean> boolean doAfterErrorImpl(String urlTag, D data);

    public abstract <D extends NetBean> void doAfterSuccessImpl(String urlTag, D data);

    public <D extends NetBean> GsonRequest inStanceGsonRequest(String urlstr, String tag, Class<D> data, Map<String, String> params, Map<String, String> headers, boolean isNeedShowDialog, boolean canDialogBackClose) {
        return  mNet.inStanceGsonRequest(urlstr, tag, data, params, headers, isNeedShowDialog,canDialogBackClose);
    }



    public <D extends NetBean> boolean doAfterError(String urlTag, D data) {
        mNet.doAfterError(urlTag, data);
        return doAfterErrorImpl(urlTag, data);
    }

    public <D extends NetBean> void doAfterSuccess(String urlTag, D data) {
        mNet.doAfterSuccess(urlTag, data);
        doAfterSuccessImpl(urlTag, data);
    }


    @Override
    public void showProcessDialog(String tag,boolean canBackDismiss) {
        if (!((NetManagerActivity)getActivity()).isDialogShowing) {
            ((NetManagerActivity)getActivity()).isDialogShowing = true;

            mNet.setCurrentUrlTag(tag);

            mNet.showProcessDialog(tag,canBackDismiss);
        }
    }

    @Override
    public void dismissProcessDialog(String tag) {
        if (((NetManagerActivity)getActivity()).isDialogShowing) {
            ((NetManagerActivity)getActivity()).isDialogShowing = false;
            mNet.dismissProcessDialog(tag);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mNet.cancelRequests();
        super.onDestroy();

    }


}
