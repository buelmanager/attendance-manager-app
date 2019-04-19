package com.buel.holyhelper.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;

/**
 * Created by blue7 on 2018-07-19.
 */

public class MakePieChartView {

    private String[] XaxisValues;
    private Typeface mTf;
    private ChartData<?> cd;
    private View convertView = null;

    public MakePieChartView(
            ChartData<?> cd,
            Context c,
            String title,
            String ment,
            String ctitle) {

        this.cd = cd;
        this.XaxisValues = XaxisValues;
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");

        PieChart chart = null;
        convertView = LayoutInflater.from(c).inflate(
                R.layout.list_item_piechart, null);
        chart = (PieChart) convertView.findViewById(R.id.chart);

        TextView textView = convertView.findViewById(R.id.list_item_piechat_tv_title);
        TextView textDescView = convertView.findViewById(R.id.list_item_piechat_tv_desc);
        textView.setText(Html.fromHtml(title));
        textDescView.setText(Html.fromHtml(ment));

        // apply styling
        chart.getDescription().setEnabled(false);
        chart.setHoleRadius(52f);
        chart.setTransparentCircleRadius(57f);
        chart.setCenterText(Html.fromHtml(ctitle));
        chart.setCenterTextTypeface(mTf);
        chart.setCenterTextSize(9f);
        chart.setUsePercentValues(true);
        chart.setExtraOffsets(5, 10, 50, 10);

        cd.setValueFormatter(new PercentFormatter());
        cd.setValueTypeface(mTf);
        cd.setValueTextSize(11f);
        cd.setValueTextColor(Color.WHITE);
        // set data
        chart.setData((PieData) cd);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateY(900);

    }

    public View getConvertView() {
        return convertView;
    }

    public void setConvertView(View convertView) {
        this.convertView = convertView;
    }
}
