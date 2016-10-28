package com.android.netactivity.app;

import com.android.volley.Response;
import com.android.netactivity.net.GsonRequest;
import com.android.netactivity.net.NetBean;

import java.util.Map;

/**
 * Created by renmingming on 16/4/11.
 *
 */
public interface NetInterFace {

     <D extends NetBean> GsonRequest inStanceGsonRequest(final String urlstr, final String tag, final Class<D> data,
                                                         final Map<String, String> params, Map<String, String> headers,
                                                         final boolean isNeedShowDialog, boolean canDialogBackClose);


    Response.ErrorListener errorListener(final String urlTag, final boolean isNeedShowDialog);


    <D extends NetBean> void doAfterSuccess(String urlTag, D data);


    boolean doAfterError(String urlTag);
    <D extends NetBean> boolean doAfterError(String urlTag, D data);



    void showProcessDialog(String tag, boolean canBackDismiss);
    void dismissProcessDialog(String tag);


    void showLoginConfirm(String content, int backtohome);


    void cancelRequests();

    void goToLogin();

    void goToHome();

    String getCurrentUrlTag();

    void setCurrentUrlTag(String tag);
}
