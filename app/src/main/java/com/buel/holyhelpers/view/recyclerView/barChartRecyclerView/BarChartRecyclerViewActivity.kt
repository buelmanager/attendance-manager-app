package com.buel.holyhelpers.view.recyclerView.barChartRecyclerView

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.selectedDay
import com.buel.holyhelpers.data.CommonData.selectedDayOfWeek
import com.buel.holyhelpers.data.CommonData.selectedDays
import com.buel.holyhelpers.data.CommonData.selectedYear
import com.buel.holyhelpers.data.CommonData.viewMode
import com.buel.holyhelpers.data.FDDatabaseHelper.getAttend
import com.buel.holyhelpers.data.ViewMode
import com.buel.holyhelpers.view.SimpleListener
import com.buel.holyhelpers.view.activity.BaseActivity
import com.commonLib.MaterialDailogUtil.Companion.datePickerDialog
import com.commonLib.MaterialDailogUtil.Companion.showSingleChoice
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.orhanobut.logger.LoggerHelper

class BarChartRecyclerViewActivity : BaseActivity() {
    lateinit var recyclerView: RecyclerView
    var holderAdapter: RecyclerView.Adapter<BarChartRecyclerViewHolder>? = null
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        recyclerView = findViewById(R.id.recycler_view_main)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) { /*if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();*/
/* if (dy < 0 && !fab.isShown())
                    fab.show();
                else if (dy > 0 && fab.isShown())
                    fab.hide();*/
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) { /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        recyclerView.setAdapter(holderAdapter)
        setDataAndTime()
    }

    private fun setDataAndTime() {
        if (viewMode != ViewMode.BRIEFING) return
        datePickerDialog(
                this@BarChartRecyclerViewActivity,
                object : OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        LoggerHelper.d("" + selectedYear + "/" +
                                CommonData.selectedMonth + "/" +
                                selectedDay + "/" +
                                selectedDayOfWeek)
                        setTitle("" + selectedYear + "/" +
                                CommonData.selectedMonth + "/" +
                                selectedDay + "/" +
                                selectedDayOfWeek)
                        showSingleChoice(
                                this@BarChartRecyclerViewActivity,
                                R.array.days_option,
                                object : OnDialogSelectListner {
                                    override fun onSelect(s: String) {
                                        selectedDays = s.toInt()
                                        LoggerHelper.d(selectedDays)
                                        setTitle("" +  selectedYear + "/" +
                                                CommonData.selectedMonth + "/" +
                                                selectedDay + "/" +
                                                selectedDayOfWeek + "/" + selectedDays)
                                        attandData
                                    }
                                })
                    }
                }
        )
    }

    private fun setTitle(str: String) {
        super.setTopTitleDesc(str)
    }

    private val attandData: Unit
        private get() {
            getAttend(SimpleListener.OnCompleteListener { recyclerView!!.adapter = holderAdapter })
        }
}