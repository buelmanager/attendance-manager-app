package com.buel.holyhelper.utils;

import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.LoggerHelper;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;

public class PageMoveManager {

    private static ArrayList<Class<T>> historyList = new ArrayList<>();

    public static void delLastHistory() {
        if (historyList != null) {
            historyList.remove(historyList.size() - 1);
            LoggerHelper.d("PageMoveManager", "마지막 historyList 를 삭제합니다.");
        }
    }

    public static void addHistory(Context context) {
        LoggerHelper.d("PageMoveManager", "히스토리를 추가합니다. " + context.getClass());

        if (historyList == null) {
            historyList = new ArrayList<>();
            LoggerHelper.d("PageMoveManager", "historyList 를 생성합니다.");
        }

        Intent intent = new Intent(context, context.getClass());
        intent.getClass();
        historyList.add((Class)context.getClass());
    }

    public static Class<T> getLastHistory() {
        Class<T> tempContext = null;
        if (historyList != null) {
            tempContext = historyList.get(historyList.size() - 1);
        }
        LoggerHelper.d("PageMoveManager", "마지막 historyList 를 가지고옵니다. ");
        return tempContext;
    }
}
