/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.android.netactivity.net;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class RequestManager {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	private static Context mContext;
	private static BitmapLruCache bitmapLruCache;
	private RequestManager() {
		// no instances
	}

	public static void init(Context context) {
		mContext = context;
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		bitmapLruCache = new BitmapLruCache(cacheSize);
		mImageLoader = new ImageLoader(mRequestQueue, bitmapLruCache);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	/**
	 * 网络连接是否可用
	 */
	public static boolean isConnnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivityManager) {
			NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

			if (null != networkInfo) {
				for (NetworkInfo info : networkInfo) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static void addRequest(Request<?> request, Object tag) {
		if(isConnnected(mContext))
		{
			if (tag != null)
			{
				request.setTag(tag);
			}
			mRequestQueue.add(request);
		}
    }
	
	public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {

			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
//	public static String getSDPaht(String key) {
//		if (mRequestQueue!=null){
//			return mRequestQueue.getPath(key);
//		}else {
//			return null;
//		}
//	}
//	public static void remove(String key,int w, int h) {
//		if (mRequestQueue!=null && mImageLoader !=null){
//			 mRequestQueue.remove(key);
//			bitmapLruCache.remove(mImageLoader.getCacheKey(key,w,h));
//
//		}
//	}
}
