package com.android.netactivity.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.android.netactivity.R;
import com.rt.sc.tools.DensityUtil;
import com.rt.sc.tools.ScreenUtil;

/**
 * Created by renmingming on 15/12/10.
 *
 */
public class ImmerseHelper
{
    private static SystemBarTintManager tintManager;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setSystemBarTransparent(Activity paramActivity, int color_title_dark)
    {
//        Window window = paramActivity.getWindow();
//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            //api 21 解决方案
//            View systemdecor = window.getDecorView();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            systemdecor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(paramActivity.getResources().getColor(color_title_dark));
//
//        } else
//        {
//            //api 19 解决方案
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            SystemBarTintManager tintManager = new SystemBarTintManager(paramActivity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(color_title_dark);
//        }
//        window.setAttributes(layoutParams);


//        hackStatusBarTransparent(paramActivity);
//        setContentPadding(paramActivity);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = paramActivity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(paramActivity.getResources().getColor(color_title_dark));
//            window.setNavigationBarColor(Color.TRANSPARENT);

        } else
        {
            paramActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            tintManager = new SystemBarTintManager(paramActivity);
            tintManager.setStatusBarTintColor(paramActivity.getResources().getColor(color_title_dark));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    public static void hackStatusBarTransparent(Activity paramActivity)
    {
        ViewGroup localViewGroup = (ViewGroup) paramActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        View colorview = new View(paramActivity);
        colorview.setBackgroundResource(R.color.color_title);
        localViewGroup.addView(colorview, ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getStatusHeight(paramActivity));
    }

    public static void setContentPadding(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            int statusH = ScreenUtil.getStatusHeight(activity);
            int actionbarH = ImmerseHelper.getActionBarHeight(activity);
            ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
            View view = rootView.getChildAt(0);
            int top = rootView.getTop();
            int top2 = view.getPaddingTop();
            view.setPadding(0, statusH + actionbarH + DensityUtil.dip2px(activity, 5), 0, 0);
        }

    }


    /**
     * 获取actionbar高度, 单位px
     *
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context)
    {
        TypedValue localTypedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, localTypedValue, true))
        {
            return TypedValue.complexToDimensionPixelSize(localTypedValue.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }
}
