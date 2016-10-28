package com.android.netactivity.app;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.netactivity.R;
import com.android.netactivity.config.AppProfile;
import com.android.netactivity.net.RequestManager;
import com.android.volley.Request;
import com.rt.sc.tools.AddTitleBar;
import com.rt.sc.tools.DensityUtil;
import com.rt.sc.tools.NetLoadingDialog;
import com.rt.sc.tools.ScreenUtil;
import com.rt.sc.tools.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by renmingming on 15/9/10.
 * 核心activity
 */

public abstract class CoreActivity extends NetManagerActivity implements com.android.netactivity.app.CoreFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {

    public static String TAG;

    public AddTitleBar addTitleBar;

    protected CoreActivity activity;

    protected FragmentManager mFragmentManager;

    protected ViewGroup rootView;

    private int statusBarH = 0;

    private WindowUtils mWindowUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (mWindowUtils == null) {
            mWindowUtils = WindowUtils.getInstance(activity);
        }
        TAG = this.getClass().getName();
        statusBarH = ScreenUtil.getStatusHeight(this);
        activity = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);

        rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        addTitleBar = new AddTitleBar(activity);
        addTitleBar.setBackUPListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addTitleBar.setTitle("");
        setContentAfterTitleBar();

        ((CoreApplication) getApplication()).addActivity(this);
    }


    protected void setContentAfterTitleBar() {
        if (AppProfile.getInstance(getApplicationContext()).isGuideFirst()) {
            com.android.netactivity.app.ImmerseHelper.setSystemBarTransparent(this, R.color.transparent_bankcards);
        } else {
            ImmerseHelper.setSystemBarTransparent(this, R.color.color_title_dark);
        }
        addTitleBar.getActionbarLayout().setPadding(0, statusBarH, 0, 0);
        int titleH = DensityUtil.dip2px(activity, 48);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rootView.setPadding(0, titleH + statusBarH, 0, 0);

        } else {
            rootView.setPadding(0, titleH, 0, 0);
        }
    }

    protected void setContentAfterStatusBar() {

        ImmerseHelper.setSystemBarTransparent(this, R.color.color_title_dark);
        addTitleBar.getActionbarLayout().setPadding(0, statusBarH, 0, 0);
        addTitleBar.setBackGroundRes(R.color.transparent_all);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rootView.setPadding(0, statusBarH, 0, 0);

        } else {
            rootView.setPadding(0, 0, 0, 0);
        }
    }


    protected void setContentTop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rootView.setPadding(0, 0, 0, 0);
        }


    }

    protected void setStatusBarColor(int color) {
        ImmerseHelper.setSystemBarTransparent(this, color);

    }


    /**
     * 设置actionbar标题字体颜色
     */
    public void setActionBarTitleColor(int color) {
        if (addTitleBar != null) {
            addTitleBar.setTitleTextColor(color);
        }
    }

    /**
     * 设置actionbar背景
     *
     * @param resource
     */
    public void setActionBarBackGround(final int resource) {

        if (addTitleBar != null) {
            addTitleBar.setBackGroundRes(resource);
        }

    }


    @Override
    protected void onTitleChanged(final CharSequence title, int color) {


        if (addTitleBar != null) {
            addTitleBar.setTitle(title.toString());
        }

        super.onTitleChanged(title, color);
    }


    /**
     * 设置左键点击事件
     */
    protected void setLeftBtnClickListener(final View.OnClickListener clickListener) {

        if (addTitleBar != null) {
            addTitleBar.setLeftBtnClickListener(clickListener);
        }

    }

    /**
     * 设置右键是否可以点击
     */
    protected void setRightBtnEnable(final boolean isEnable) {


        if (addTitleBar != null) {
            addTitleBar.setRightBtnEnable(isEnable);
        }

    }

    /**
     * @param clickListener 设置右键点击事件
     */
    protected void setRightBtnClickListener(final View.OnClickListener clickListener) {

        if (addTitleBar != null) {
            addTitleBar.setRightBtnClickListener(clickListener);
        }

    }

    /**
     * 设置右边按钮
     *
     * @param drawable ico
     * @param type     1,left 2 top 3 right 4 buttom
     * @param text     标题
     */
    protected void setRightBtnDrawable(final Drawable drawable, final int type, final String text) {

        if (addTitleBar != null) {
            addTitleBar.setTitleBtnDrawable(drawable, type, text, 1);
        }

    }

    /**
     * 设置左边按钮
     *
     * @param drawable ico
     * @param type     1,left 2 top 3 right 4 buttom
     * @param text     标题
     */
    protected void setLeftBtnDrawable(final Drawable drawable, final int type, final String text) {


        if (addTitleBar != null) {
            addTitleBar.setTitleBtnDrawable(drawable, type, text, 0);
        }

    }

    /**
     * 隐藏显示右边按钮
     *
     * @param isShow 是否显示
     */
    protected void setHidOrShowRightBtn(final boolean isShow) {

        if (addTitleBar != null) {
            addTitleBar.setHidOrShowRightBtn(isShow);
        }

    }

    /**
     * 隐藏显示返回键
     *
     * @param isShow 是否显示
     */
    protected void setHidOrShowbackBtn(final boolean isShow) {

        if (addTitleBar != null) {
            addTitleBar.setHidOrShowbackBtn(isShow);
        }

    }

    /**
     * 设置返回键监听
     */
    protected void setBackUPListener(final View.OnClickListener listener) {

        if (addTitleBar != null) {
            addTitleBar.setBackUPListener(listener);
        }

    }


    protected void executeRequest(@NonNull Request<?> request) {
        RequestManager.addRequest(request, this);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        onFragmentInteractionImpl(uri);
    }

    @Override
    public void onFragmentInteraction(Uri uri, Object object) {
        onFragmentInteractionImpl(uri, object);
    }

    /**
     * fragment 回调activity
     *
     * @param uri 区分fragment
     */
    protected abstract void onFragmentInteractionImpl(Uri uri);

    protected void onFragmentInteractionImpl(Uri uri, Object object) {
    }

    protected CoreFragment addFragment(Class fragmentclass, int fragmentid, String tag, int animalType, int addType) {
        try {
            CoreFragment baseFragment = (CoreFragment) fragmentclass.newInstance();
            addFragment(baseFragment, fragmentid, tag, animalType, addType);
            return baseFragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void addFragment(Fragment fragment, int fragmentid, String tag, int animalType) {
        addFragment(fragment, fragmentid, tag, animalType, 0);
    }

    /**
     * @param fragment   fragment
     * @param fragmentid fid
     * @param tag        fragment tag
     * @param animalType 0 up-down 1 left-right
     * @param addType    0 add 1 replace
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void addFragment(Fragment fragment, int fragmentid, String tag, int animalType, int addType) {
        if (null == mFragmentManager) {
            mFragmentManager = getSupportFragmentManager();
        }
        mFragmentManager.addOnBackStackChangedListener(this);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            if (animalType == 0) {
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in_bottom,
                        R.anim.slide_out_top);
            } else if (animalType == 1) {
                fragmentTransaction.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_right_out);
            }
        } else {
            if (animalType == 0) {
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in_bottom,
                        R.anim.slide_out_bottom,
                        R.anim.slide_in_top,
                        R.anim.slide_out_top);
            } else if (animalType == 1) {
                fragmentTransaction.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        }
        if (!fragment.isAdded()) {
            if (0 == addType) {
                fragmentTransaction.add(fragmentid, fragment, tag);
            } else if (1 == addType) {
                fragmentTransaction.replace(fragmentid, fragment, tag);
            }
            fragmentTransaction.addToBackStack(null);

        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        int i = intent.getIntExtra("tag", 0);
        if (i == 1) {
            overridePendingTransition(R.anim.push_alpha_out, R.anim.push_alpha_out);
        } else {
            overridePendingTransition(R.anim.push_left_in, R.anim.push_alpha_out);
        }
    }


    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        super.startActivityFromFragment(fragment, intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_alpha_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_alpha_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        } else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        if (mWindowUtils != null) {
            mWindowUtils.hidePopupWindow(true);
        }
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEventBean event) {
        if (onEventIMpl(event)) {
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    public boolean onEventIMpl(BaseEventBean event) {
        return false;
    }

    @Override
    public void onBackStackChanged() {

    }



    /**
     * @param content
     * @param backtohome 0 不需要返回主页，1需要
     */
    public void showLoginConfirm(String content, final int backtohome) {
        View view = View.inflate(getApplicationContext(), R.layout.dialog_cancel_comfrim, null);
        Dialog dialog = NetLoadingDialog.getInstance().loading(this, view);
        TextView titleTV = (TextView) view.findViewById(R.id.title);
        titleTV.setText(content);
        view.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
                NetLoadingDialog.getInstance().dismissDialog();
            }
        });
        view.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
                NetLoadingDialog.getInstance().dismissDialog();
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    goToHome();
                }
                NetLoadingDialog.getInstance().dismissDialog();
                return false;
            }
        });

    }

}
