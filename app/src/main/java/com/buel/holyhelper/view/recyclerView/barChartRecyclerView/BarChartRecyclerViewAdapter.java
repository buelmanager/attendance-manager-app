package com.buel.holyhelper.view.recyclerView.barChartRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.model.BarChartModel;
import com.chartView.ChartUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by blue7 on 2018-06-07.
 */

public class BarChartRecyclerViewAdapter
        extends RecyclerView.Adapter<BarChartRecyclerViewHolder> {

    List<BarChartModel> itemArrayList;
    View.OnClickListener onClickListener;

    public void setItemArrayList(List<BarChartModel> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public BarChartRecyclerViewAdapter(ArrayList<BarChartModel> itemArrayList, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public BarChartRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclervivew_item_barchart, parent, false);
        return new BarChartRecyclerViewHolder(view);
    }

    private final ArrayList<Integer> selected = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(
            @NonNull final BarChartRecyclerViewHolder holder,
            final int position) {

        final BarChartModel barChartModel = itemArrayList.get(position);

        try {
            Context c = holder.barChart.getContext();
            Typeface mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
            BarChart chart = holder.barChart;
            chart.setPinchZoom(false);
            chart.setScaleEnabled(false);
            TextView textView = holder.tvDesc;
            textView.setText(Html.fromHtml(barChartModel.ment));

            TextView textViewDiff = holder.tvDiff;
            TextView textViewyDiff = holder.tvyDiff;
            LinearLayout llDiff = holder.llDiff;

            String headOperType = "";
            String diff = "";
            String Ydiff = "";
            String headOperTipe2 = "";

            String strColor = "";
            textViewDiff.setText("");
            textViewyDiff.setText("");
            llDiff.setVisibility(View.GONE);

            if (barChartModel.prevDiff != null && barChartModel.prevYearDiff != null) {
                llDiff.setVisibility(View.VISIBLE);

                diff = barChartModel.prevDiff;
                Ydiff = barChartModel.prevYearDiff;

                if (Double.parseDouble(barChartModel.prevDiff) > 0) {
                    headOperType = "▲ ";
                    strColor = "#1565C0";
                } else if (Double.parseDouble(barChartModel.prevDiff) == 0) {
                    headOperType = "─";
                    diff = "";
                    strColor = "#898989";
                } else if (Double.parseDouble(barChartModel.prevDiff) < 0) {
                    headOperType = "▼ ";
                    strColor = "#FF8C9D";
                }

                textViewDiff.setTextColor(Color.parseColor(strColor));
                textViewDiff.setText(Html.fromHtml("전월 대비 " + headOperType + diff));

                if (Double.parseDouble(barChartModel.prevYearDiff) > 0) {
                    headOperTipe2 = "▲ ";
                    strColor = "#1565C0";
                } else if (Double.parseDouble(barChartModel.prevYearDiff) == 0) {
                    headOperTipe2 = "─";
                    Ydiff = "";
                    strColor = "#898989";
                } else if (Double.parseDouble(barChartModel.prevYearDiff) < 0) {
                    headOperTipe2 = "▼ ";
                    strColor = "#FF8C9D";
                }

                textViewyDiff.setTextColor(Color.parseColor(strColor));
                textViewyDiff.setText(Html.fromHtml((CommonData.getSelectedYear() - 1) + "년 동월 대비 " + headOperTipe2 + Ydiff));
            }
            TextView textViewTitle = holder.tvTitle;
            textViewTitle.setText(Html.fromHtml(barChartModel.title  ));

            // apply styling
            chart.getDescription().setEnabled(false);
            chart.setDrawGridBackground(false);
            chart.setDrawBarShadow(false);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(mTf);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setTypeface(mTf);
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(20f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setTypeface(mTf);
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(20f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            BarData cd = barChartModel.bardata;
            cd.setValueTypeface(mTf);

            // set data
            chart.setData((BarData) cd);
            chart.setFitBars(true);

            // do not forget to refresh the chart
            // holder.chart.invalidate();
            chart.animateY(700);
            String[] XaxisValues = barChartModel.XaxisValues;

            if (XaxisValues != null) {
                XAxis dXAxis = chart.getXAxis();
                dXAxis.setValueFormatter(new ChartUtils.XAxisValueFormatter(XaxisValues));
            }
        } catch (Exception e) {
            LoggerHelper.d(e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
