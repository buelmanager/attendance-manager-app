package com.buel.holyhelpers.view.recyclerView.briefingRecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.data.FDDatabaseHelper;
import com.buel.holyhelpers.data.ViewMode;
import com.buel.holyhelpers.model.AttendModel;
import com.buel.holyhelpers.model.DateModel;
import com.buel.holyhelpers.view.SimpleListener;
import com.buel.holyhelpers.view.activity.BaseActivity;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BriefingRecyclerViewActivity extends BaseActivity {

    private static final String TAG = "MemberRecyclerViewActivity";
    RecyclerView recyclerView;
    RecyclerView.Adapter<BriefingRecyclerViewHolder> holderAdapter;
    TextView tvDesc;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        tvDesc = findViewById(R.id.recycler_view_main_tv_desc);
        recyclerView = findViewById(R.id.recycler_view_main);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        setDataAndTime();

    }

    private void setDataAndTime() {
        if (CommonData.getViewMode() != ViewMode.BRIEFING) return;

        MaterialDailogUtil.Companion.datePickerDialog(
                BriefingRecyclerViewActivity.this,
                s -> {
                    LoggerHelper.d(CommonData.getSelectedYear() + "/" +
                            CommonData.getSelectedMonth() + "/" +
                            CommonData.getSelectedDay() + "/" +
                            CommonData.getSelectedDayOfWeek());

                    tvDesc.setText(CommonData.getSelectedYear() + "/" +
                            CommonData.getSelectedMonth() + "/" +
                            CommonData.getSelectedDay() + "/" +
                            CommonData.getSelectedDayOfWeek());
                    MaterialDailogUtil.Companion.showSingleChoice(
                            BriefingRecyclerViewActivity.this,
                            R.array.days_option,
                            s1 -> {
                                CommonData.setSelectedDays(Integer.parseInt(s1));

                                LoggerHelper.d(CommonData.getSelectedDays());
                                tvDesc.setText(CommonData.getSelectedYear() + "/" +
                                        CommonData.getSelectedMonth() + "/" +
                                        CommonData.getSelectedDay() + "/" +
                                        CommonData.getSelectedDayOfWeek() + "/" + CommonData.getSelectedDays());

                                getAttandData();
                            });
                }
        );
    }

    private void getAttandData() {
        ArrayList<AttendModel> attendModels = new ArrayList<>();
        HashMap<String, String> attendMap = new HashMap<>();

        FDDatabaseHelper.INSTANCE.getAttend(new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {

                LoggerHelper.d("onFromDataComplete");

                HashMap<String, DateModel> dateMaps = CommonData.getAttendDateMaps();
                for (Map.Entry<String, DateModel> dateElem : dateMaps.entrySet()) {
                    DateModel itemModel = dateElem.getValue();
                    HashMap<String, AttendModel> itemModelmap = itemModel.member;
                    for (Map.Entry<String, AttendModel> elem : itemModelmap.entrySet()) {
                        //System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
                        attendModels.add(elem.getValue());
                    }
                }

                holderAdapter = new BriefingRecyclerViewAdapter(attendModels, v -> {
                    if (v.getId() == R.id.recycler_view_item_btn_delete) {
                    } else if (v.getId() == R.id.recycler_view_item_btn_select) {
                    }
                });

                holderAdapter.notifyDataSetChanged();
                ((BriefingRecyclerViewAdapter) holderAdapter).setItemArrayList(attendModels);
                recyclerView.refreshDrawableState();

                holderAdapter = new BriefingRecyclerViewAdapter(attendModels, v -> {

                });
                recyclerView.setAdapter(holderAdapter);

            }
        });

    }
}
