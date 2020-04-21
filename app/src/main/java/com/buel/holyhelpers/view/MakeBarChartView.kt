package com.buel.holyhelpers.view

import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.model.ChartListenerModel
import com.chartView.ChartUtils.XAxisValueFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.orhanobut.logger.LoggerHelper

/**
 * Created by blue7 on 2018-07-19.
 */
class MakeBarChartView(
        private val cd: ChartData<*>,
        c: Context,
        private val XaxisValues: Array<String>?,
        title: String?,
        onCompleteListener: DataTypeListener.OnCompleteListener<ChartListenerModel?>?) {
    private val mTf: Typeface
    lateinit var convertView: View
    init {
        mTf = Typeface.createFromAsset(c.assets, "OpenSans-Regular.ttf")
        var chart: BarChart? = null
        convertView = LayoutInflater.from(c).inflate(
                R.layout.list_item_barchart, null)

        LoggerHelper.d("XaxisValues", XaxisValues.toString())

        chart = convertView.findViewById<View>(R.id.chart) as BarChart
        chart.setPinchZoom(false)
        chart!!.setScaleEnabled(false)
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                val date = XaxisValues!![e.x.toInt()]
                val cnt = e.y.toInt()
                LoggerHelper.d("Activity", "e.getX : " + e.x + ", date : " + date + ", cnt: " + cnt)
                val chartListenerModel = ChartListenerModel()
                chartListenerModel.xIndex = e.x.toInt()
                chartListenerModel.xData = date
                chartListenerModel.yData = cnt
                onCompleteListener?.onComplete(chartListenerModel)
            }

            override fun onNothingSelected() {
                LoggerHelper.d("onNothingSelected")
            }
        })
        val textView = convertView.findViewById<TextView>(R.id.list_item_barchat_tv_title)
        textView.text = Html.fromHtml(title)
        // apply styling
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = mTf
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        val leftAxis = chart.axisLeft
        leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)
        leftAxis.spaceTop = 20f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        val rightAxis = chart.axisRight
        rightAxis.typeface = mTf
        rightAxis.setLabelCount(5, false)
        rightAxis.spaceTop = 20f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        cd.setValueTypeface(mTf)
        // set data
        chart.data = cd as BarData
        chart.setFitBars(true)
        // do not forget to refresh the chart
//        holder.chart.invalidate();
        chart.animateY(700)
        if (XaxisValues != null) {
            val dXAxis = chart.xAxis
            dXAxis.valueFormatter = XAxisValueFormatter(XaxisValues)
        }
    }
}