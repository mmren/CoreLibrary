package netactivity.android.com.corelibrary.base;

import android.app.Activity;

import com.android.netactivity.app.CoreApplication;

import java.util.ArrayList;

/**
 * Created by hanwp on 2016/10/26.
 */

public class MaiYaPuHuiApplication extends CoreApplication
{
    private ArrayList<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void finishAll(){
        for(Activity activity : activities){
            activity.finish();
        }
    }
}
