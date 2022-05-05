package com.frxs.flutter_icod_print.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by crf on 2020/5/30.
 */
public class DataUtils {
    private static Context context;
    private static DataUtils dataUtils;

    public static DataUtils getInstance(Context context) {
        if (null == dataUtils) {
            synchronized (DataUtils.class) {
                if (null == dataUtils) {
                    dataUtils = new DataUtils(context.getApplicationContext());
                }
            }
        }
        return dataUtils;
    }

    public DataUtils(Context context) {
        this.context = context;
    }

    /**
     * 得到string.xml中字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId) {
        return context.getResources().getStringArray(resId);
    }

    /**
     * 得到string.xml中整形数组
     *
     * @param resId
     * @return
     */
    public static int[] getIntegerArr(int resId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(resId);

        int indexCount = typedArray.length();

        int arr[] = new int[indexCount];
        for (int i = 0; i < indexCount; i++) {
            arr[i] = typedArray.getInteger(i, 9600);
        }
        return arr;
    }
}
