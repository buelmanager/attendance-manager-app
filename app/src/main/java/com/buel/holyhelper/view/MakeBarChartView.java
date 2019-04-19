package com.buel.holyhelper.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.model.ChartListenerModel;
import com.chartView.ChartUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.LoggerHelper;

/**
 * Created by blue7 on 2018-07-19.
 */

public class MakeBarChartView {

    private String[] XaxisValues;
    private Typeface mTf;
    private ChartData<?> cd;
    private View convertView = null;

    public MakeBarChartView(
            ChartData<?> cd,
            Context c,
            String[] XaxisValues,
            String title,
            DataTypeListener.OnCompleteListener<ChartListenerModel> onCompleteListener) {

        this.cd = cd;
        this.XaxisValues = XaxisValues;
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");

        BarChart chart = null;
        convertView = LayoutInflater.from(c).inflate(
                R.layout.list_item_barchart, null);
        chart = (BarChart) convertView.findViewById(R.id.chart);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                String date = XaxisValues[(int) e.getX()];
                int cnt = (int) e.getY();

                LoggerHelper.d("Activity", "e.getX : " + e.getX() + ", date : " + date + ", cnt: " + cnt);

                ChartListenerModel chartListenerModel = new ChartListenerModel();
                chartListenerModel.xIndex = (int) e.getX();
                chartListenerModel.xData = date;
                chartListenerModel.yData = cnt;

                if (onCompleteListener != null) onCompleteListener.onComplete(chartListenerModel);
            }

            @Override
            public void onNothingSelected() {
                LoggerHelper.d("onNothingSelected");
            }
        });

        TextView textView = convertView.findViewById(R.id.list_item_barchat_tv_title);
        textView.setText(Html.fromHtml(title));
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

        cd.setValueTypeface(mTf);

        // set data
        chart.setData((BarData) cd);
        chart.setFitBars(true);

        // do not forget to refresh the chart
//        holder.chart.invalidate();
        chart.animateY(700);

        if (XaxisValues != null) {
            XAxis dXAxis = chart.getXAxis();
            dXAxis.setValueFormatter(new ChartUtils.XAxisValueFormatter(XaxisValues));
        }
    }

    public View getConvertView() {
        return convertView;
    }

    public void setConvertView(View convertView) {
        this.convertView = convertView;
    }
}

