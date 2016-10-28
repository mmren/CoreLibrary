package netactivity.android.com.corelibrary.base;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.netactivity.app.CoreActivity;
import com.android.netactivity.app.WindowUtils;
import com.android.netactivity.net.NetBean;
import com.android.volley.Response;
import com.rt.sc.config.Config;
import com.rt.sc.tools.DensityUtil;
import com.rt.sc.tools.GeneralUtils;
import com.rt.sc.tools.StringUtils;

import netactivity.android.com.corelibrary.R;

/**
 * Created by renmingming on 2016/10/24.
 * 基础activity类， goToLogin，goToHome
 */

public class BaseActivity extends CoreActivity {

    protected WindowUtils mWindowUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mWindowUtils == null) {
            mWindowUtils = WindowUtils.getInstance(activity);
        }
        setLeftBtnDrawable(getResources().getDrawable(R.drawable.selector_white_return),1,"");
        setTitleTextSize(DensityUtil.px2dip(this,getResources().getDimension(R.dimen.text_size_18)));
        setActionbarColor();
    }

    @Override
    protected void onFragmentInteractionImpl(Uri uri) {

    }

    @Override
    public Response.ErrorListener errorListener(String urlTag, boolean isNeedShowDialog) {
        return null;
    }

    @Override
    public boolean doAfterError(String urlTag) {
        return false;
    }

    @Override
    public void showLoginConfirm(String content, int backtohome) {

    }

    @Override
    public void cancelRequests() {

    }

    @Override
    public void goToLogin() {

    }

    @Override
    public void goToHome() {

    }

    @Override
    public String getCurrentUrlTag() {
        return null;
    }

    @Override
    public void setCurrentUrlTag(String tag) {

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWindowUtils != null) {
            mWindowUtils.hidePopupWindow(true);
        }
    }

    /**
     * 不需要动画
     * @param fragment   fragment
     * @param fragmentid fid
     * @param tag        fragment tag
     * @param addType    0 add 1 replace
     */
    @Override
    protected void addFragment(Fragment fragment, int fragmentid, String tag, int addType)
    {
        if (null == mFragmentManager) {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            if (0 == addType) {
                fragmentTransaction.add(fragmentid, fragment, tag);
            } else if (1 == addType) {
                fragmentTransaction.replace(fragmentid, fragment, tag);
            }
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public <D extends NetBean> boolean doAfterErrorImpl(String urlTag, D data) {
        return false;
    }

    @Override
    public <D extends NetBean> void doAfterSuccessImpl(String urlTag, D data) {

    }

    public void setNoActionbar()
    {
        addTitleBar.hide();
        setContentTop();
    }

    public void sethaveActionbar()
    {
        addTitleBar.show();
        setContentAfterTitleBar();
    }
    public void setOnlyTitle(String title)
    {
        setHidOrShowbackBtn(false);
        setTitle(title);
        setActionbarColor();
    }
    public void setActionbarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addTitleBar.getActionbarLayout().setBackgroundColor(getResources().getColor(R.color.color_1b86da, getTheme()));
        }else {
            addTitleBar.getActionbarLayout().setBackgroundColor(getResources().getColor(R.color.color_1b86da));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setActionBarTitleColor(getResources().getColor(R.color.white, getTheme()));
        }else {
            setActionBarTitleColor(getResources().getColor(R.color.white));
        }
    }

    private void setTitleTextSize(float size)
    {
        ((TextView)addTitleBar.getActionbarLayout().findViewById(R.id.title)).setTextSize(size);
    }

    public void showPhoneDialog()
    {
        mWindowUtils.showPopupWindow(R.layout.dialog_cancel_comfrim, R.style.loading_dialog, R.style.anim_menu_bottombar, true, Gravity.CENTER);
        View rootView = mWindowUtils.getmView();


        TextView titleTV = (TextView) rootView.findViewById(R.id.title);
        titleTV.setText(getString(R.string.service_tel_content, Config.CONSTACT_US2));
        TextView confirm = (TextView) rootView.findViewById(R.id.dialog_confirm);
        confirm.setText(getString(R.string.call));
        mWindowUtils.registhideListener(R.id.dialog_cancel);
        mWindowUtils.registhideListener(R.id.dialog_confirm);
        mWindowUtils.registClickEvent(R.id.dialog_confirm, new WindowUtils.ClickCallback() {
            @Override
            public void onClick(View v) {
                GeneralUtils.setTel(getApplicationContext(), Config.CONSTACT_US);
            }
        });
    }

    public void showPromptPopWindow(String content) {
        showPromptPopWindow(content, null, false);
    }

    public void showPromptPopWindow(String content, WindowUtils.ClickCallback callback, boolean isShowCancel) {
        showPromptPopWindow(content, callback, isShowCancel, "", true);
    }

    public void showPromptPopWindow(String content, WindowUtils.ClickCallback callback, boolean isShowCancel, boolean isHide) {
        showPromptPopWindow(content, callback, isShowCancel, "", isHide);
    }

    public void showPromptPopWindow(String content, WindowUtils.ClickCallback callback, boolean isShowCancel, String confirmStr) {
        showPromptPopWindow(content, callback, isShowCancel, confirmStr, true);
    }

    public void showPromptPopWindow(String content, WindowUtils.ClickCallback callback, boolean isShowCancel, String confirmStr, boolean isHide) {
        if (!activity.isFinishing()) {
            if (mWindowUtils == null) {
                mWindowUtils = WindowUtils.getInstance(activity);
            }
            if (isHide) {
                mWindowUtils.hidePopupWindow(false);
            }
            mWindowUtils.showPopupWindow(com.android.netactivity.R.layout.dialog_cancel_comfrim, com.android.netactivity.R.style.loading_dialog, com.android.netactivity.R.style.dialog_bg_style, isShowCancel, true, Gravity.CENTER);
            View rootView = mWindowUtils.getmView();

            if (!StringUtils.isEmpty(confirmStr)) {
                ((TextView) rootView.findViewById(com.android.netactivity.R.id.dialog_confirm)).setText(confirmStr);
            }
            TextView titleTV = (TextView) rootView.findViewById(com.android.netactivity.R.id.title);
            titleTV.setText(content);
            mWindowUtils.registhideListener(com.android.netactivity.R.id.dialog_confirm);
            mWindowUtils.registhideListener(com.android.netactivity.R.id.dialog_cancel);
            if (callback != null) {
                mWindowUtils.registClickEvent(com.android.netactivity.R.id.dialog_confirm, callback);
            }
        }
    }
}
