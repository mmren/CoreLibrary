package com.android.netactivity.app;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author renmingming
 * @param <T>
 */
public abstract class AdapterBase<T> extends BaseAdapter {

    public final List<T> mList = new LinkedList<T>();
    public Handler mHandler;
    public Context mContext;
    public LayoutInflater mInflater;
    public BaseHolder mHolder;

    public AdapterBase() {}

    public AdapterBase(Context baseContext) {
        this.mContext = baseContext;
        mInflater = LayoutInflater.from(baseContext);
    }

    public List<T> getList() {
        return mList;
    }

    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendToListOnly(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
    }

    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void appendToTopListOnly(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void clearOnly() {
        mList.clear();
    }
    public void setCurrentLayoutIDAtten() {

    }

    public void setCurrentLayoutIDFans() {}

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        if (position > mList.size() - 1) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            mHolder = getHolder(position, convertView, parent);
            convertView = mHolder.rootView;
            if (convertView != null) {
                convertView.setTag(mHolder);
            }
        } else {
            mHolder = (BaseHolder) convertView.getTag();
        }
        if (mList != null && mList.size() > 0 && mHolder != null) {
            initView(position,mList.get(position), mHolder);
        }
        return convertView;
    }

    protected abstract void initView(int position,T data, BaseHolder mBaseHolder);

    protected abstract BaseHolder getHolder(int position, View convertView, ViewGroup parent);

    public class BaseHolder {
        public View rootView;
    }

}
