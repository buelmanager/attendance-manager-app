package com.chartView;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by blue7 on 2018-07-15.
 */

public class ChartUtils {
    public static class XAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValue;

        public XAxisValueFormatter(String[] mValue) {
            this.mValue = mValue;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValue[(int)value];
        }
    }
}
