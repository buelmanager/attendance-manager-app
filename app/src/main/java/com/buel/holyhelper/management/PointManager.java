package com.buel.holyhelper.management;

import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.view.SimpleListener;
import com.orhanobut.logger.LoggerHelper;

import java.util.HashMap;
import java.util.Map;

public class PointManager {

    private static int mPoint;

    public static int getmCumulativePoint() {
        return mCumulativePoint;
    }

    public static void setmCumulativePoint(int mCumulativePoint) {
        PointManager.mCumulativePoint = mCumulativePoint;
    }

    private static int mCumulativePoint;

    public static int getmPoint() {
        return mPoint;
    }

    public static void setmPoint(int mPoint) {
        PointManager.mPoint = mPoint;
    }

    public static void setMinusPoint(int value) {
        PointManager.getServerPoint(new Management.OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                LoggerHelper.d("이전 포인트 : " + getmPoint());
                int tempPoint = getmPoint() - value;

                if(tempPoint<0) tempPoint = 0;
                setmPoint(tempPoint);
                Map<String, Object> pMap  = new HashMap<>();
                pMap.put("point", getmPoint());
                modify(CommonData.getHolyModel(),
                        pMap,
                        new Management.OnCompleteListener() {
                            @Override
                            public void onComplete(Object data) {
                                LoggerHelper.d("현재 포인트 : " + getmPoint());
                            }
                        }
                );
            }
        });
    }
    public static void setPlusPoint(int value ) {
        PointManager.getServerPoint(new Management.OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                LoggerHelper.d("이전 포인트 : " + getmPoint());
                LoggerHelper.d("이전 누적 포인트 : " + getmCumulativePoint());
                int tempPoint = getmPoint() + value;
                int tempCPoint = getmCumulativePoint() + value;
                setmPoint(tempPoint);
                setmCumulativePoint(tempCPoint);
                Map<String, Object> pMap  = new HashMap<>();
                pMap.put("point", getmPoint());
                pMap.put("cumulativePoint", getmCumulativePoint());
                modify(CommonData.getHolyModel(),
                        pMap,
                        new Management.OnCompleteListener() {
                            @Override
                            public void onComplete(Object data) {
                                LoggerHelper.d("현재 포인트 : " + getmPoint());
                                LoggerHelper.d("현재 누적 포인트 : " + getmCumulativePoint());
                            }
                        }
                );
            }
        });
    }

    public static void setPlusPoint(int value , SimpleListener.OnCompleteListener onCompleteListener) {
        PointManager.getServerPoint(new Management.OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                LoggerHelper.d("이전 포인트 : " + getmPoint());
                LoggerHelper.d("이전 누적 포인트 : " + getmCumulativePoint());
                int tempPoint = getmPoint() + value;
                int tempCPoint = getmCumulativePoint() + value;
                setmPoint(tempPoint);
                setmCumulativePoint(tempCPoint);
                Map<String, Object> pMap  = new HashMap<>();
                pMap.put("point", getmPoint());
                pMap.put("cumulativePoint", getmCumulativePoint());
                modify(CommonData.getHolyModel(),
                        pMap,
                        new Management.OnCompleteListener() {
                            @Override
                            public void onComplete(Object data) {
                                LoggerHelper.d("현재 포인트 : " + getmPoint());
                                LoggerHelper.d("현재 누적 포인트 : " + getmCumulativePoint());

                                FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
                                    @Override
                                    public void onComplete() {
                                        if(onCompleteListener !=null )onCompleteListener.onComplete();
                                    }
                                });
                            }
                        }
                );
            }
        });
    }

    /**
     * @param onCompleteListener
     */
    public static void getServerPoint(
            Management.OnCompleteListener onCompleteListener) {
        FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                if(CommonData.getHolyModel() == null) return;
                setmPoint(CommonData.getHolyModel().point);
                setmCumulativePoint(CommonData.getHolyModel().cumulativePoint);
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(null);
            }
        });
    }

    public static void modify(
            HolyModel dataModel,
            Map<String, Object> map,
            final Management.OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendCorpsModifyData(dataModel, map, listener);
    }
}
