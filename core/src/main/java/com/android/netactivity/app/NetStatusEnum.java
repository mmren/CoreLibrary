package com.android.netactivity.app;

/**
 * Created by renmingming on 2016/10/18.
 */

public enum NetStatusEnum {

    startandcancancle(0),startandcannotcancle(1),error(2), errorandfinish(3), errorandToast(4), errorandshowDialog(5);

    private int value = 0;

    NetStatusEnum(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static NetStatusEnum toLable(String str) throws Exception {
        try {
            return valueOf(str);
        } catch (Exception ex) {
            throw new Exception("NetStatusEnum enum is illegality");
        }
    }

}
