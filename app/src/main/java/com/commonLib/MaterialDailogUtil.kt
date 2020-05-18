package com.commonLib

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.data.FDDatabaseHelper
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.utils.AppUtil
import com.buel.holyhelpers.view.DataTypeListener
import com.buel.holyhelpers.view.SimpleListener
import com.buel.holyhelpers.view.activity.CorpsManagerViewActivity
import com.buel.holyhelpers.view.activity.GroupManagerViewActivity
import com.orhanobut.logger.LoggerHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by 19001283 on 2018-07-09.
 */

class MaterialDailogUtil {

    interface OnDialogSelectListner {
        fun onSelect(s: String)
    }

    @Throws(Exception::class)
    fun getDateDay(date: String, dateType: String): String {

        var day = ""
        val dateFormat = SimpleDateFormat(dateType)
        val nDate = dateFormat.parse(date)
        val cal = Calendar.getInstance()
        cal.time = nDate
        val dayNum = cal.get(Calendar.DAY_OF_WEEK)
        when (dayNum) {
            1 -> day = "일"
            2 -> day = "월"
            3 -> day = "화"
            4 -> day = "수"
            5 -> day = "목"
            6 -> day = "금"
            7 -> day = "토"
        }
        return day
    }

    companion object {

        val isProgressSupport: Boolean
            get() = MaterialDailogUtil.progressDialog != null


        private val isCancelable = false

        fun getTeamDialog(context: Context, onCompleteListener: SimpleListener.OnCompleteListener): MaterialDialog? {

            if (CommonData.holyModel == null) {
                SuperToastUtil.toastE(context, CommonString.INFO_TITLE_CONTROL_CORP)
                return null
            }

            if (CommonData.selectedGroup == null) {
                SuperToastUtil.toastE(context, CommonString.INFO_TITLE_CONTROL_GROUP)
                return null
            }

            if (CommonData.selectedGroup.team == null || CommonData.selectedGroup.team.size == 0) {
                SuperToastUtil.toastE(context, CommonString.INFO_TITLE_CONTROL_TEAM)
                return null
            }

            val selGroup = CommonData.selectedGroup

            val names = CommonData.teamMap.filter { it.value!!.groupUid == selGroup.uid }.map { it.value!!.name }

            Collections.sort(names)
            return MaterialDialog.Builder(context)
                    .title(CommonString.INFO_TITLE_CONTROL_TEAM)
                    .iconRes(R.drawable.ic_info)
                    .items(names)
                    .cancelable(isCancelable)
                    .itemsCallback { dialog, view, which, text ->
                        val teams = CommonData.teamMap

                        var team: HolyModel.groupModel.teamModel? = teams.values.find {

                            LoggerHelper.d("CommonData.getSelectedGroup().name : " + CommonData.selectedGroup.uid + " / " + "it.groupUid : " + it!!.groupUid)
                            LoggerHelper.d("text.toString() : " + text.toString() + " / " + "it.name : " + it!!.name)
                            it.groupUid == CommonData.selectedGroup.uid && it.name == text.toString()
                        }

                        CommonData.selectedTeam = team!!
                        CommonData.setCurTeamModel(team)

                        onCompleteListener.onComplete()
                    }.show()
        }

        fun getGroupDialog(context: Context, onCompleteListener: SimpleListener.OnCompleteListener): MaterialDialog? {

            if (CommonData.holyModel == null) {
                Toast.makeText(context, CommonString.INFO_TITLE_CONTROL_CORP, Toast.LENGTH_SHORT).show()
                //CommonData.setHistoryClass(context.javaClass as Class<T>?)
                val intent = Intent(context.applicationContext, CorpsManagerViewActivity::class.java)
                context.startActivity(intent)
                return null
            }

            if (CommonData.holyModel.group == null || CommonData.holyModel.group.size == 0) {
                Toast.makeText(context, CommonString.GROUP_NICK + " 리스트가 없어 생성화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
                //CommonData.setHistoryClass(context.javaClass as Class<*>)
                val intent = Intent(context.applicationContext, GroupManagerViewActivity::class.java)
                context.startActivity(intent)
                return null
            }

            val names:ArrayList<String> = FDDatabaseHelper.getGroupNameList(CommonData.holyModel.group)

            return MaterialDialog.Builder(context)
                    .title(CommonString.GROUP_NICK + "를 선택하세요")
                    .iconRes(R.drawable.ic_info)
                    .items(names)
                    .cancelable(isCancelable)
                    .itemsCallback { dialog, view, which, text ->
                        val group = FDDatabaseHelper.getGroupModelNameFromMap(CommonData.holyModel.group, text.toString())
                        CommonData.setCurGroupModel(group)
                        CommonData.selectedGroup = group

                        FDDatabaseHelper.getTeamDataToStore(DataTypeListener.OnCompleteListener { onCompleteListener.onComplete() })
                    }
                    .show()

        }

        fun getGroupListDialog(
                context: Context,
                title: String,
                content: String,
                arrayList: ArrayList<String>,
                horizontal: Boolean, cancelabled: Boolean): MaterialDialog {
            return MaterialDialog.Builder(context)
                    .title(title)
                    .iconRes(R.drawable.ic_info)
                    .items(arrayList)
                    .cancelable(isCancelable)
                    .itemsCallback { dialog, view, which, text ->
                        LoggerHelper.d("which : $which")
                        LoggerHelper.d("text : $text")
                    }
                    .show()
        }

        fun getListDialog(
                context: Context,
                title: String,
                content: String,
                arrayList: ArrayList<String>,
                horizontal: Boolean, cancelabled: Boolean): MaterialDialog {
            return MaterialDialog.Builder(context)
                    .title(title)
                    .iconRes(R.drawable.ic_info)
                    .items(arrayList)
                    .cancelable(isCancelable)
                    .itemsCallback { dialog, view, which, text ->
                        LoggerHelper.d("which : $which")
                        LoggerHelper.d("text : $text")
                    }
                    .show()
        }

        private var progressDialog: MaterialDialog? = null

        fun showProgressDialog(value: Boolean?) {
            try {
                if (value!!)
                    MaterialDailogUtil.progressDialog!!.show()
                else
                    MaterialDailogUtil.progressDialog!!.dismiss()
            } catch (e: Exception) {
                LoggerHelper.e(e.message)
            }

        }

        fun deleteProgressDialog() {

            if (MaterialDailogUtil.progressDialog != null) {
                MaterialDailogUtil.progressDialog!!.dismiss()
                MaterialDailogUtil.progressDialog = null
            }
        }

        fun progressDialog(
                context: Context,
                title: String,
                content: String,
                horizontal: Boolean, cancelabled: Boolean) {

            MaterialDailogUtil.progressDialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.progress_dialog)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(horizontal)
                    .cancelable(isCancelable).build()
        }

        fun progressDialogShowCnt(
                context: Context,
                title: String,
                content: String,
                MaxCnt: Int,
                curCnt: Int,
                horizontal: Boolean, cancelabled: Boolean): MaterialDialog {

            return MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.progress_dialog)
                    .content(R.string.please_wait)
                    .progress(false, MaxCnt)
                    .progressIndeterminateStyle(horizontal)
                    .cancelable(isCancelable).build()
        }

        fun simpleYesNoDialog(context: Context, title: String,
                              selectListner: OnDialogSelectListner) {

            val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which -> selectListner.onSelect("ok") }
                    .show()
        }


        fun fileChooserDialog(context: Context) {


        }

        fun simpleDoneDialog(context: Context, title: String, content: String,
                             selectListner: OnDialogSelectListner?) {

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .content(content)
                    .positiveText(android.R.string.ok)
                    .cancelable(isCancelable)
                    .onPositive { dialog1, which -> selectListner?.onSelect("ok") }
                    .show()

        }

        fun simpleInputDoneDialog(context: Context, title: String, hint: String,
                                  selectListner: OnDialogSelectListner?) {
            @SuppressLint("ResourceType") val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .input(hint, null, true) { dialog, input ->
                        LoggerHelper.d("input : $input")
                        selectListner?.onSelect(input.toString())
                    }
                    .positiveText(android.R.string.ok)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which ->
                        //selectListner.onSelect("ok");
                    }
                    .show()
        }

        fun simpleDoneDialog(context: Context, title: String,
                             selectListner: OnDialogSelectListner?) {

            val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .positiveText(android.R.string.ok)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which -> selectListner?.onSelect("ok") }
                    .show()
        }

        fun simpleYesNoDialog(context: Context, selectListner: OnDialogSelectListner?) {

            val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.yesno)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which -> selectListner?.onSelect("ok") }
                    .show()
        }

        fun noticeDialog(context: Context, content: String, title: String, isYesNo: Boolean, selectListner: OnDialogSelectListner) {
            val layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null)
            val linearLayout = layout.findViewById<LinearLayout>(R.id.notice_layout)
            val textView = layout.findViewById<TextView>(R.id.content_tv)
            val textTitleView = layout.findViewById<TextView>(R.id.content_title)
            textView.text = Html.fromHtml(content)
            textTitleView.text = Html.fromHtml(title)

            if (isYesNo)
                customYesNoDialog(context, layout, selectListner)
            else
                customDialog(context, layout, selectListner)
        }

        fun noticeDialog(context: Context, content: String, title: String, selectListner: OnDialogSelectListner) {
            val layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null)
            val linearLayout = layout.findViewById<LinearLayout>(R.id.notice_layout)
            val textView = layout.findViewById<TextView>(R.id.content_tv)
            val textTitleView = layout.findViewById<TextView>(R.id.content_title)
            textView.text = Html.fromHtml(content)
            textTitleView.text = Html.fromHtml(title)
            customDialog(context, layout, selectListner)
        }

        fun noticeDialog(context: Context, content: String, selectListner: OnDialogSelectListner) {
            val layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null)
            val linearLayout = layout.findViewById<LinearLayout>(R.id.notice_layout)
            val textView = layout.findViewById<TextView>(R.id.content_tv)

            textView.text = Html.fromHtml(content)
            customDialog(context, layout, selectListner)
        }

        fun CustomDailogManager(context: Context, content: String, title: String, selectListner: OnDialogSelectListner) {
            val layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null)
            val linearLayout = layout.findViewById<LinearLayout>(R.id.notice_layout)
            val textView = layout.findViewById<TextView>(R.id.content_tv)
            val textTitleView = layout.findViewById<TextView>(R.id.content_title)
            textView.text = Html.fromHtml(content)
            textTitleView.text = Html.fromHtml(title)
            customDialog2(context, layout, selectListner)
        }

        fun shareKakaoMessage(
                context: Context,
                title: String,
                popupMsg: String,
                sendMsg: String) {
            MaterialDailogUtil.CustomDailogManager(
                    context,
                    popupMsg,
                    title,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            if (s == "share") {
                                AppUtil.sendSharedData(context, sendMsg)
                            }
                        }
                    })
        }

        fun customDialog2(context: Context, layout: View, selectListner: OnDialogSelectListner?) {
            @SuppressLint("ResourceAsColor") val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .checkBoxPrompt("don't again", false) { buttonView, isChecked -> Toast.makeText(context, "check : $isChecked", Toast.LENGTH_SHORT).show() }
                    .customView(layout, false)
                    .positiveText("[ 닫기 ] ")
                    .positiveColor(R.color.darkGray)
                    .negativeText("[ 앱으로 공유 ]")
                    .cancelable(isCancelable)
                    .onNegative { dialog, which ->
                        selectListner?.onSelect("share")
                    }
                    .onPositive { dialog, which ->
                        selectListner?.onSelect("ok")
                    }
                    .show()
        }

        fun customYesNoDialog(context: Context, layout: View, selectListner: OnDialogSelectListner?) {
            val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .checkBoxPrompt("don't again", false) { buttonView, isChecked -> Toast.makeText(context, "check : $isChecked", Toast.LENGTH_SHORT).show() }
                    .customView(layout, false)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.no)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which ->
                        selectListner?.onSelect("ok")
                    }
                    .show()
        }


        fun customDialog(context: Context, layout: View, selectListner: OnDialogSelectListner?) {
            val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .checkBoxPrompt("don't again", false) { buttonView, isChecked -> Toast.makeText(context, "check : $isChecked", Toast.LENGTH_SHORT).show() }
                    .customView(layout, false)
                    .positiveText(android.R.string.ok)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which ->
                        selectListner?.onSelect("ok")
                    }
                    .show()
        }

        fun datePickerDialog2(context: Context, selectListner: OnDialogSelectListner?) {
            val pickerView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_datepicker, null)

            val datePicker = pickerView.findViewById<DatePicker>(R.id.datePicker)
            val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.select_date)
                    .customView(datePicker, false)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which ->
                        val year = datePicker.year
                        val mon = datePicker.month
                        val day = datePicker.dayOfMonth

                        val calendar = Calendar.getInstance()
                        calendar.set(year, mon, day)

                        selectListner?.onSelect(year.toString() + "-" + (mon + 1) + "-" + day)
                        dialog.dismiss()
                    }
                    .show()
        }

        fun datePickerDialog(context: Context, selectListner: OnDialogSelectListner?) {
            val pickerView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_datepicker, null)

            val datePicker = pickerView.findViewById<DatePicker>(R.id.datePicker)

            if (CommonData.selectedYear != -1) {
                //datePicker.init(CommonData.getSelectedYear(),CommonData.getSelectedMonth(),CommonData.getSelectedDay());
                datePicker.init(CommonData.selectedYear, CommonData.selectedMonth - 1, CommonData.selectedDay, null)
            }

            val dialog = MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.select_date)
                    .customView(datePicker, false)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which ->
                        val year = datePicker.year
                        val mon = datePicker.month
                        val day = datePicker.dayOfMonth

                        val calendar = Calendar.getInstance()
                        calendar.set(year, mon, day)

                        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

                        CommonData.selectedYear = year
                        CommonData.selectedMonth = mon + 1
                        CommonData.selectedDay = day
                        CommonData.selectedDayOfWeek = dayOfWeek

                        selectListner?.onSelect("days : $dayOfWeek")
                        dialog.dismiss()
                    }
                    .show()
        }

        fun showSingleChoiceReturnStr(
                context: Context,
                title: String,
                list: List<*>,
                selectListner: OnDialogSelectListner?) {

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .items(list)
                    .itemsCallbackSingleChoice(
                            0
                    ) { dialog, view, which, text ->
                        //showToast(context , which + ": " + text);
                        selectListner?.onSelect(text.toString())
                        true // allow selection
                    }
                    //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .cancelable(isCancelable)
                    //.neutralText(R.string.clear_selection)
                    .show()
        }

        fun showSingleChoice(
                context: Context,
                title: String,
                list: List<*>,
                selectListner: OnDialogSelectListner?) {

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .items(list)
                    .itemsCallbackSingleChoice(
                            0
                    ) { dialog, view, which, text ->
                        //showToast(context , which + ": " + text);
                        selectListner?.onSelect(which.toString())
                        true // allow selection
                    }
                    //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .cancelable(isCancelable)
                    //.neutralText(R.string.clear_selection)
                    .show()
        }

        fun showSingleChoice(
                context: Context,
                title: String,
                itemsRes: Int,
                selectListner: OnDialogSelectListner?) {

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .items(itemsRes)
                    .itemsCallbackSingleChoice(
                            0
                    ) { dialog, view, which, text ->
                        //showToast(context , which + ": " + text);
                        selectListner?.onSelect(which.toString())
                        true // allow selection
                    }
                    //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .cancelable(isCancelable)
                    //.neutralText(R.string.clear_selection)
                    .show()
        }

        fun showSingleChoice(
                context: Context,
                itemsRes: Int,
                selectListner: OnDialogSelectListner?) {

            var selectIndex = 0
            if (CommonData.selectedDays != -1) {
                selectIndex = CommonData.selectedDays
            }

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.select_time)
                    .items(itemsRes)
                    .itemsCallbackSingleChoice(
                            selectIndex
                    ) { dialog, view, which, text ->
                        //showToast(context , which + ": " + text);
                        selectListner?.onSelect(which.toString())
                        true // allow selection
                    }
                    //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .cancelable(isCancelable)
                    //.neutralText(R.string.clear_selection)
                    .show()
        }

        /*public static void showSingleChoice(
            Context context,
            int itemsRes,
            final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(R.string.select_time)
                .items(R.array.days_option)
                .itemsCallbackSingleChoice(
                        1,
                        (dialog, view, which, text) -> {
                            //showToast(context , which + ": " + text);
                            if (selectListner != null)
                                selectListner.onSelect(String.valueOf(which));
                            return true; // allow selection
                        })
                //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                .onPositive((dialog, which) -> dialog.dismiss())
                .alwaysCallMultiChoiceCallback()
                .positiveText(R.string.md_choose_label)
                .autoDismiss(false)
                .cancelable(isCancelable)
                //.neutralText(R.string.clear_selection)
                .show();
    }*/

        fun simpleListDialog(
                context: Context,
                itemsRes: Int,
                title: Int,
                selectListner: OnDialogSelectListner?) {

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(title)
                    .items(itemsRes)
                    .itemsCallbackSingleChoice(
                            0
                    ) { dialog, view, which, text ->
                        //showToast(context , which + ": " + text);
                        selectListner?.onSelect(which.toString())
                        true // allow selection
                    }
                    //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .cancelable(isCancelable)
                    //.neutralText(R.string.clear_selection)
                    .show()
        }

        fun simpleListDialog(
                context: Context,
                strings: List<String>,
                selectListner: OnDialogSelectListner?) {

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.simple_list_title)
                    .items(strings)
                    .itemsCallbackSingleChoice(
                            0
                    ) { dialog, view, which, text ->
                        //showToast(context , which + ": " + text);
                        selectListner?.onSelect(which.toString())
                        true // allow selection
                    }
                    //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .cancelable(isCancelable)
                    //.neutralText(R.string.clear_selection)
                    .show()
        }

        fun simpleListDialog(
                context: Context,
                itemsRes: Int,
                selectListner: OnDialogSelectListner?) {

            MaterialDialog.Builder(context)
                    .iconRes(R.drawable.ic_info)
                    .title(R.string.simple_list_title)
                    .items(itemsRes)
                    .itemsCallbackSingleChoice(
                            0
                    ) { dialog, view, which, text ->
                        //showToast(context , which + ": " + text);
                        selectListner?.onSelect(which.toString())
                        true // allow selection
                    }
                    //.onNeutral((dialog, which) -> dialog.clearSelectedIndices())
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .cancelable(isCancelable)
                    //.neutralText(R.string.clear_selection)
                    .show()
        }

        @SuppressLint("NewApi")
        fun timePickerDialog(context: Context, selectListner: OnDialogSelectListner?) {
            val pickerView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_timepicker, null)

            val timePicker: TimePicker = pickerView.findViewById<TimePicker>(R.id.timePicker)


            val dialog = MaterialDialog.Builder(context)
                    .title("시간을 선택하세요.")
                    .customView(timePicker, false)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .iconRes(R.drawable.ic_info)
                    .cancelable(isCancelable)
                    .onPositive { dialog, which ->
                        val hour = timePicker.hour
                        val minute = timePicker.minute
                        val baseline = timePicker.baseline

                        selectListner?.onSelect("hour : $hour/ minute : $minute/ baseline : $baseline")
                        dialog.dismiss()
                    }
                    .show()

        }

        var toast: Toast? = null

        fun showToast(context: Context, message: String) {
            if (toast != null) {
                toast!!.cancel()
                toast = null
            }
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast!!.show()
        }

        fun getExcutive(positionNum: Int): String {
            var position = ""
            when (Integer.valueOf(positionNum)) {
                1 -> position = "성도/회원"
                0 -> position = "임원"

                else -> {
                }
            }
            return position
        }

        fun getNew(positionNum: Int): String {
            var position = ""
            when (Integer.valueOf(positionNum)) {
                1 -> position = "새신자"
                0 -> position = "기존"

                else -> {
                }
            }
            return position
        }


        fun getGender(positionNum: Int): String {
            var position = ""
            when (Integer.valueOf(positionNum)) {
                1 -> position = "여성"
                0 -> position = "남성"

                else -> {
                }
            }
            return position
        }

        fun getPosition(positionNum: Int): String {

            var position = ""
            when (Integer.valueOf(positionNum)) {
                0 -> position = "담임목사"
                1 -> position = "담임사모"
                2 -> {
                    position = "전도사"
                    position = "전도사"
                }
                3 -> position = "전도사"
                4 -> position = "교육사"
                5 -> position = "간사"
                6 -> position = "부장"
                7 -> position = "지역장"
                8 -> position = "부지역장"
                9 -> position = "총무"
                10 -> position = "부총무"
                11 -> position = "장로"
                12 -> position = "권사"
                13 -> position = "안수집사"
                14 -> position = "집사"
                15 -> position = "성도"
                else -> {
                }
            }
            return position
        }
    }

}
