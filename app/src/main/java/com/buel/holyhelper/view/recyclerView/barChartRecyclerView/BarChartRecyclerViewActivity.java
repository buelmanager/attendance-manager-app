package com.buel.holyhelper.view.recyclerView.barChartRecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.view.activity.BaseActivity;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BarChartRecyclerViewActivity extends BaseActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter<BarChartRecyclerViewHolder> holderAdapter;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recycler_view_main);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /*if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();*/
               /* if (dy < 0 && !fab.isShown())
                    fab.show();
                else if (dy > 0 && fab.isShown())
                    fab.hide();*/
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        recyclerView.setAdapter(holderAdapter);
        setDataAndTime();

    }

    private void setDataAndTime() {

        if (CommonData.getViewMode() != ViewMode.BRIEFING) return;

        MaterialDailogUtil.datePickerDialog(
                BarChartRecyclerViewActivity.this,
                new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {

                        LoggerHelper.d(CommonData.getSelectedYear() + "/" +
                                CommonData.getSelectedMonth() + "/" +
                                CommonData.getSelectedDay() + "/" +
                                CommonData.getSelectedDayOfWeek());

                        setTitle(CommonData.getSelectedYear() + "/" +
                                CommonData.getSelectedMonth() + "/" +
                                CommonData.getSelectedDay() + "/" +
                                CommonData.getSelectedDayOfWeek());
                        MaterialDailogUtil.showSingleChoice(
                                BarChartRecyclerViewActivity.this,
                                R.array.days_option,

                                new MaterialDailogUtil.OnDialogSelectListner() {
                                    @Override
                                    public void onSelect(String s) {
                                        CommonData.setSelectedDays(Integer.parseInt(s));

                                        LoggerHelper.d(CommonData.getSelectedDays());
                                        setTitle(CommonData.getSelectedYear() + "/" +
                                                CommonData.getSelectedMonth() + "/" +
                                                CommonData.getSelectedDay() + "/" +
                                                CommonData.getSelectedDayOfWeek() + "/" + CommonData.getSelectedDays());

                                        getAttandData();
                                    }
                                });
                    }
                }
        );
    }

    private void setTitle(String str) {
        super.setTopTitleDesc(str);
    }

    private void getAttandData() {
        FDDatabaseHelper.INSTANCE.getAttend(() -> recyclerView.setAdapter(holderAdapter));
    }
}
