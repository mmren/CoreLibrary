package com.android.netactivity.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.netactivity.R;
import com.android.netactivity.net.RequestManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by renmingming on 15/9/16.
 * 基础fragment
 */
public abstract class CoreFragment extends NetManagerFragment {

    protected String TAG;

    Bundle savedState;

    private String uuid_f;

    protected static final String ARG_PARAM1 = "param1";

    protected static final String ARG_PARAM2 = "param2";

    protected String mParam1;

    protected String mParam2;

    View rootView = null;

    protected CoreActivity activity;

    private OnFragmentInteractionListener mListener;


    private final List<Request> requests = new ArrayList<>();

    protected CoreFragment() {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onFragmentInteraction(Uri uri, Object object);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG = this.getClass().getName();
        super.onCreate(savedInstanceState);
        activity = (CoreActivity) getActivity();

        if (getArguments() != null) {
            Bundle bundle = getArguments();

            if (bundle != null) {
                if (bundle.containsKey(ARG_PARAM1))
                    mParam1 = getArguments().getString(ARG_PARAM1);
                if (bundle.containsKey(ARG_PARAM2))
                    mParam2 = getArguments().getString(ARG_PARAM2);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = initView(inflater, container, savedInstanceState);
        }
        getData();
        return rootView;
    }


    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void getData();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
    }

    protected void onFirstTimeLaunched() {
        uuid_f = UUID.randomUUID().toString();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onButtonPressed(Uri uri, Object object) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri, object);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save State Here
        saveStateToArguments();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        // Save State Here
        saveStateToArguments();
        super.onDestroyView();
    }


    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b != null)
                b.putBundle(uuid_f, savedState);
        }
    }


    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null && b.containsKey(uuid_f)) {
            savedState = b.getBundle(uuid_f);
            if (savedState != null) {
                restoreState();
                return true;
            }
        }
        return false;
    }


    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }

    protected void onRestoreState(Bundle savedInstanceState) {

    }


    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    protected void onSaveState(Bundle outState) {

    }

    protected void executeRequest(@NonNull Request<?> request) {
        RequestManager.addRequest(request, this);
    }


    @Override
    public void onResume() {
        if (getView() != null) {
            getView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        super.onResume();
    }



    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEventBean event) {

        onEventIMpl(event);



    }

    public void onEventIMpl(BaseEventBean event) {

    }


    @Override
    public void onDestroy() {
        for (Request request : requests) {
            request.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_alpha_out);
    }
    @Override
    public String getCurrentUrlTag() {
        return null;
    }

    @Override
    public void setCurrentUrlTag(String tag) {

    }

}
