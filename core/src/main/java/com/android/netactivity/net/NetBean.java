package com.android.netactivity.net;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Http响应基本包装类.
 *
 * @author renmingming
 */

public abstract class NetBean implements Serializable {


    public NetBean() {
    }

    public NetBean(String retcode, String retinfo, String token) {
        this.retcode = retcode;
        this.retinfo = retinfo;
        this.token = token;
    }

    @SerializedName("retcode")
    @Expose
    private String retcode;

    @SerializedName("retinfo")
    @Expose
    private String retinfo;

    @SerializedName("token")
    @Expose
    private String token;


    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(String retinfo) {
        this.retinfo = retinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    /**
     * The retinfo
     */
    public abstract void saveToke();

    /**
     * 设备被踢下线
     *
     * @return
     */
    public abstract boolean isJumpLogin();

    /**
     * toaken过期
     *
     * @return
     */
    public abstract boolean isExpire();

    public abstract String toString();

    public abstract boolean isSuccess();

    public abstract boolean isFailed();


    public static NetBean instance(){
        return new NetBean() {
            @Override
            public void saveToke() {

            }

            @Override
            public boolean isJumpLogin() {
                return false;
            }

            @Override
            public boolean isExpire() {
                return false;
            }

            @Override
            public String toString() {
                return null;
            }

            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public boolean isFailed() {
                return false;
            }
        };
    }



}
