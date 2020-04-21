package com.buel.holyhelpers.view.recyclerView.briefingRecyclerView

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.FDDatabaseHelper.getAttend
import com.buel.holyhelpers.data.ViewMode
import com.buel.holyhelpers.model.AttendModel
import com.buel.holyhelpers.model.DateModel
import com.buel.holyhelpers.view.SimpleListener
import com.buel.holyhelpers.view.activity.BaseActivity
import com.commonLib.MaterialDailogUtil.Companion.datePickerDialog
import com.commonLib.MaterialDailogUtil.Companion.showSingleChoice
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.orhanobut.logger.LoggerHelper
import java.util.*

class BriefingRecyclerViewActivity() : BaseActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var holderAdapter: RecyclerView.Adapter<BriefingRecyclerViewHolder>
    var tvDesc: TextView? = null
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        tvDesc = findViewById(R.id.recycler_view_main_tv_desc)
        recyclerView = findViewById(R.id.recycler_view_main)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        setDataAndTime()
    }

    private fun setDataAndTime() {
        if (CommonData.viewMode != ViewMode.BRIEFING) return
        /*datePickerDialog(
                this@BriefingRecyclerViewActivity,
                OnDialogSelectListner { s: String? ->
                    LoggerHelper.d((CommonData.getSelectedYear().toString() + "/" +
                            CommonData.getSelectedMonth() + "/" +
                            CommonData.getSelectedDay() + "/" +
                            CommonData.getSelectedDayOfWeek()))
                    tvDesc!!.setText((CommonData.getSelectedYear().toString() + "/" +
                            CommonData.getSelectedMonth() + "/" +
                            CommonData.getSelectedDay() + "/" +
                            CommonData.getSelectedDayOfWeek()))
                    showSingleChoice(
                            this@BriefingRecyclerViewActivity,
                            R.array.days_option,
                            OnDialogSelectListner { s1: String ->
                                CommonData.setSelectedDays(s1.toInt())
                                LoggerHelper.d(CommonData.getSelectedDays())
                                tvDesc!!.setText((CommonData.getSelectedYear().toString() + "/" +
                                        CommonData.getSelectedMonth() + "/" +
                                        CommonData.getSelectedDay() + "/" +
                                        CommonData.getSelectedDayOfWeek() + "/" + CommonData.getSelectedDays()))
                                attandData
                            })
                }
        )*/

        datePickerDialog(this, object : OnDialogSelectListner{
            override fun onSelect(s: String) {
                LoggerHelper.d((CommonData.selectedYear.toString() + "/" +
                        CommonData.selectedMonth + "/" +
                        CommonData.selectedDay + "/" +
                        CommonData.selectedDayOfWeek))
                tvDesc!!.setText((CommonData.selectedYear.toString() + "/" +
                        CommonData.selectedMonth + "/" +
                        CommonData.selectedDay + "/" +
                        CommonData.selectedDayOfWeek))

                showSingleChoice(
                        this@BriefingRecyclerViewActivity,
                        R.array.days_option,
                        object : OnDialogSelectListner{
                            override fun onSelect(s: String) {
                                LoggerHelper.d(CommonData.selectedDays)
                                tvDesc!!.setText((CommonData.selectedYear.toString() + "/" +
                                        CommonData.selectedMonth + "/" +
                                        CommonData.selectedDay + "/" +
                                        CommonData.selectedDayOfWeek + "/" + CommonData.selectedDays))
                                attandData
                            }
                        })
            }
        })
    }

    //System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
    private val attandData: Unit
        private get() {
            val attendModels = ArrayList<AttendModel>()
            val attendMap = HashMap<String, String>()
            getAttend(SimpleListener.OnCompleteListener {
                LoggerHelper.d("onFromDataComplete")
                val dateMaps = CommonData.attendDateMaps
                for (dateElem: Map.Entry<String?, DateModel> in dateMaps!!.entries) {
                    val itemModel = dateElem.value
                    val itemModelmap = itemModel.member
                    for (elem: Map.Entry<String?, AttendModel> in itemModelmap.entries) { //System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
                        attendModels.add(elem.value)
                    }
                }
                holderAdapter = BriefingRecyclerViewAdapter(attendModels, View.OnClickListener { v: View ->
                    if (v.getId() == R.id.recycler_view_item_btn_delete) {
                    } else if (v.getId() == R.id.recycler_view_item_btn_select) {
                    }
                })
                holderAdapter.notifyDataSetChanged()
                (holderAdapter as BriefingRecyclerViewAdapter).setItemArrayList(attendModels)
                recyclerView!!.refreshDrawableState()
                holderAdapter = BriefingRecyclerViewAdapter(attendModels, View.OnClickListener { v: View? -> })
                recyclerView!!.adapter = holderAdapter
            })
        }

    companion object {
        private val TAG = "MemberRecyclerViewActivity"
    }
}