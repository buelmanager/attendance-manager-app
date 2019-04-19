package com.orhanobut.logger;

import android.util.Log;

import com.commonLib.Common;

/**
 * Created by blue7 on 2018-05-09.
 */

public class LoggerHelper {

    public static String TAG_2019_04_07 = "2019_04_07";
    public LoggerHelper() {
        super();
    }

    private static FormatStrategy formatStrategy;
    private static Boolean isDebug = false;

    public static void setLogger(String tag, boolean bool) {
        if (formatStrategy != null) return;
        formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(7)
                .showThreadInfo(false)      // (Optional) Whether to show thread info or not. Default true
                .tag(tag)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        isDebug = bool;
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }


    public static void i(String title, String s) {
        Log.i(Common.PACKAGE_NAME, "");
        Log.i(Common.PACKAGE_NAME, "┌────── [ " + title + " ] ──────┐");
        Logger.i(s);
        Log.i(Common.PACKAGE_NAME, "");
    }

    public static void s(String title, String s) {
        if (isDebug)
            Logger.i("[ " + title + " ]- " + s);
    }

    public static void i(String s) {
        Logger.i(s);
    }

    public static void v(String title, String s) {
        if (isDebug) {
            Log.v(Common.PACKAGE_NAME, "");
            Log.v(Common.PACKAGE_NAME, "┌────── [ " + title + " ] ──────┐");
            Logger.v(s);
            Log.v(Common.PACKAGE_NAME, "");
        }
    }

    public static void v(String s) {
        if (isDebug)
            Logger.v(s);
    }

    public static void d(String title, Object obj) {
        Log.d(Common.PACKAGE_NAME, "");
        Log.d(Common.PACKAGE_NAME, "┌────── [ " + title + " ] ──────┐");
        Logger.d(obj);
        Log.d(Common.PACKAGE_NAME, "");
    }

    public static void d(Object obj) {
        Logger.d(obj);
    }

    public static void e(String title, String str) {
        Log.e(Common.PACKAGE_NAME, "");
        Log.e(Common.PACKAGE_NAME, "┌────── [ " + title + " ] ──────┐");
        Logger.e(str);
        Log.e(Common.PACKAGE_NAME, "");
    }

    public static void e(String str) {
        Logger.e(str);
    }

    public static void setIsDebug(Boolean debug) {
        isDebug = debug;
    }
}
