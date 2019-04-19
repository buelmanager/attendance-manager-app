package com.buel.holyhelper.view.recyclerView.barChartRecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.github.mikephil.charting.charts.BarChart;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by blue7 on 2018-06-07.
 */

public class BarChartRecyclerViewHolder extends RecyclerView.ViewHolder {

    BarChart barChart;
    TextView tvTitle;
    TextView tvDesc;
    TextView tvDiff;
    TextView tvyDiff;
    LinearLayout llDiff;

    public BarChartRecyclerViewHolder(View itemView) {
        super(itemView);
        barChart = itemView.findViewById(R.id.recyclerview_item_barchart_chart);
        tvTitle = itemView.findViewById(R.id.recyclerview_item_barchart_tv_title);
        tvDesc = itemView.findViewById(R.id.recyclerview_item_barchart_tv_desc);
        tvDiff = itemView.findViewById(R.id.recyclerview_item_barchart_tv_diff);
        tvyDiff = itemView.findViewById(R.id.recyclerview_item_barchart_tv_ydiff);
        llDiff = itemView.findViewById(R.id.diff_ll);
    }


}
