package com.commonLib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.AppUtil;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.DataTypeListener;
import com.buel.holyhelper.view.SimpleListener;
import com.buel.holyhelper.view.activity.CorpsManagerViewActivity;
import com.buel.holyhelper.view.activity.GroupManagerViewActivity;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.logger.LoggerHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Created by 19001283 on 2018-07-09.
 */

public class MaterialDailogUtil {

    public static boolean isProgressSupport() {

        return (MaterialDailogUtil.progressDialog != null);
    }


    private static Boolean isCancelable = false;

    public static MaterialDialog getTeamDialog(Context context, SimpleListener.OnCompleteListener onCompleteListener) {

        if (CommonData.getHolyModel() == null) {
            SuperToastUtil.toastE(context, CommonString.INFO_TITLE_CONTROL_CORP);
            return null;
        }

        if (CommonData.getSelectedGroup() == null) {
            SuperToastUtil.toastE(context, CommonString.INFO_TITLE_CONTROL_GROUP);
            return null;
        }

        if (CommonData.getSelectedGroup().team == null || CommonData.getSelectedGroup().team.size() ==0) {
            SuperToastUtil.toastE(context, CommonString.INFO_TITLE_CONTROL_TEAM);
            return null;
        }

        ArrayList<String> names = FDDatabaseHelper.INSTANCE.getTeamNameList(CommonData.getSelectedGroup().team);

        return new MaterialDialog.Builder(context)
                .title(CommonString.INFO_TITLE_CONTROL_TEAM)
                .iconRes(R.drawable.ic_info)
                .items(names)
                .cancelable(isCancelable)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        List<HolyModel.groupModel.teamModel> teams = SortMapUtil.getSortTeamList();
                        HolyModel.groupModel.teamModel team = null;
                        for (HolyModel.groupModel.teamModel eleTeam : teams) {

                            LoggerHelper.e("SortMapUtil.getInteger(eleTeam.name) : " +  SortMapUtil.getInteger(eleTeam.name));
                            LoggerHelper.e("SortMapUtil.String.valueOf(text) : " +  String.valueOf(text));

                            int intTxt = SortMapUtil.getInteger(String.valueOf(text));
                            int intCompare = SortMapUtil.getInteger(eleTeam.name);

                            LoggerHelper.e("compare : " + (intTxt == intCompare));

                            if ( intTxt == intCompare) {
                                team = eleTeam;
                                LoggerHelper.e("MaterialDialog getTeamDialog", team.name + " // " + team.uid);
                                break;
                            }
                        }

                        try {
                            team.name = SortMapUtil.getInteger(team.name).toString();
                            CommonData.setSelectedTeam(team);
                            CommonData.setTeamModel(team);
                        } catch (Exception e) {
                            LoggerHelper.e("e : " + e.getMessage());
                        }

                        onCompleteListener.onComplete();
                    }
                }).show();
    }

    public static MaterialDialog getGroupDialog(Context context, SimpleListener.OnCompleteListener onCompleteListener) {

        if (CommonData.getHolyModel() == null) {
            Toast.makeText(context, CommonString.INFO_TITLE_CONTROL_CORP, Toast.LENGTH_SHORT).show();
            CommonData.setHistoryClass((Class) context.getClass());
            Intent intent = new Intent(context.getApplicationContext(), CorpsManagerViewActivity.class);
            context.startActivity(intent);
            return null;
        }

        if (CommonData.getHolyModel().group == null || CommonData.getHolyModel().group.size() == 0) {
            Toast.makeText(context, CommonString.GROUP_NICK + " 리스트가 없어 생성화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
            CommonData.setHistoryClass((Class) context.getClass());
            Intent intent = new Intent(context.getApplicationContext(), GroupManagerViewActivity.class);
            context.startActivity(intent);
            return null;
        }

        ArrayList<String> names = FDDatabaseHelper.INSTANCE.getGroupNameList(CommonData.getHolyModel().group);


        return new MaterialDialog.Builder(context)
                .title(CommonString.GROUP_NICK + "를 선택하세요")
                .iconRes(R.drawable.ic_info)
                .items(names)
                .cancelable(isCancelable)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        HolyModel.groupModel group = FDDatabaseHelper.INSTANCE.getGroupModelNameFromMap(CommonData.getHolyModel().group, String.valueOf(text));
                        CommonData.setGroupModel(group);
                        CommonData.setSelectedGroup(group);

                        FDDatabaseHelper.INSTANCE.getTeamDataToStore(new DataTypeListener.OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(QuerySnapshot queryDocumentSnapshots) {
                                onCompleteListener.onComplete();
                            }
                        });


                    }
                })
                .show();

    }

    public static MaterialDialog getGroupListDialog(
            Context context,
            String title,
            String content,
            ArrayList<String> arrayList,
            boolean horizontal, boolean cancelabled) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .iconRes(R.drawable.ic_info)
                .items(arrayList)
                .cancelable(isCancelable)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        LoggerHelper.d("which : " + which);
                        LoggerHelper.d("text : " + text);
                    }
                })
                .show();
    }

    public static MaterialDialog getListDialog(
            Context context,
            String title,
            String content,
            ArrayList<String> arrayList,
            boolean horizontal, boolean cancelabled) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .iconRes(R.drawable.ic_info)
                .items(arrayList)
                .cancelable(isCancelable)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        LoggerHelper.d("which : " + which);
                        LoggerHelper.d("text : " + text);
                    }
                })
                .show();
    }

    private static MaterialDialog progressDialog;

    public static void showProgressDialog(Boolean value) {
        try {
            if (value)
                MaterialDailogUtil.progressDialog.show();
            else
                MaterialDailogUtil.progressDialog.dismiss();
        } catch (Exception e) {
            LoggerHelper.e(e.getMessage());
        }
    }

    public static void deleteProgressDialog() {

        if(MaterialDailogUtil.progressDialog != null) {
            MaterialDailogUtil.progressDialog.dismiss();
            MaterialDailogUtil.progressDialog = null;
        }
    }

    public static void progressDialog(
            Context context,
            String title,
            String content,
            boolean horizontal, boolean cancelabled) {

        MaterialDailogUtil.progressDialog = new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .cancelable(isCancelable).build();
    }

    public static MaterialDialog progressDialogShowCnt(
            Context context,
            String title,
            String content,
            int MaxCnt,
            int curCnt,
            boolean horizontal, boolean cancelabled) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(false, MaxCnt)
                .progressIndeterminateStyle(horizontal)
                .cancelable(isCancelable).build();

        return dialog;
    }

    public static void simpleYesNoDialog(Context context, String title,
                                         final OnDialogSelectListner selectListner) {

        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .title(title)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                selectListner.onSelect("ok");
                            }
                        })
                        .show();
    }


    public static void fileChooserDialog(Context context) {


    }

    public static void simpleDoneDialog(Context context, String title, String content,
                                        final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(title)
                .content(content)
                .positiveText(android.R.string.ok)
                .cancelable(isCancelable)
                .onPositive((dialog1, which) -> {
                    if (selectListner != null) selectListner.onSelect("ok");
                })
                .show();

    }


    public static void simpleInputDoneDialog(Context context, String title, String hint,
                                             final OnDialogSelectListner selectListner) {
        @SuppressLint("ResourceType") MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .title(title)
                        .input(hint, null, true, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                LoggerHelper.d("input : " + String.valueOf(input));
                                if (selectListner != null)
                                    selectListner.onSelect(String.valueOf(input));
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //selectListner.onSelect("ok");
                            }
                        })
                        .show();
    }

    public static void simpleDoneDialog(Context context, String title,
                                        final OnDialogSelectListner selectListner) {

        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .title(title)
                        .positiveText(android.R.string.ok)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (selectListner != null) selectListner.onSelect("ok");
                            }
                        })
                        .show();
    }

    public static void simpleYesNoDialog(Context context, final OnDialogSelectListner selectListner) {

        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .title(R.string.yesno)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (selectListner != null) selectListner.onSelect("ok");
                            }
                        })
                        .show();
    }

    public static void noticeDialog(Context context, String content, String title, boolean isYesNo, final OnDialogSelectListner selectListner) {
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
        final LinearLayout linearLayout = layout.findViewById(R.id.notice_layout);
        final TextView textView = layout.findViewById(R.id.content_tv);
        final TextView textTitleView = layout.findViewById(R.id.content_title);
        textView.setText(Html.fromHtml(content));
        textTitleView.setText(Html.fromHtml(title));

        if (isYesNo)
            customYesNoDialog(context, layout, selectListner);
        else
            customDialog(context, layout, selectListner);
    }

    public static void noticeDialog(Context context, String content, String title, final OnDialogSelectListner selectListner) {
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
        final LinearLayout linearLayout = layout.findViewById(R.id.notice_layout);
        final TextView textView = layout.findViewById(R.id.content_tv);
        final TextView textTitleView = layout.findViewById(R.id.content_title);
        textView.setText(Html.fromHtml(content));
        textTitleView.setText(Html.fromHtml(title));
        customDialog(context, layout, selectListner);
    }

    public static void noticeDialog(Context context, String content, final OnDialogSelectListner selectListner) {
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
        final LinearLayout linearLayout = layout.findViewById(R.id.notice_layout);
        final TextView textView = layout.findViewById(R.id.content_tv);

        textView.setText(Html.fromHtml(content));
        customDialog(context, layout, selectListner);
    }

    public static void CustomDailogManager(Context context, String content, String title, final OnDialogSelectListner selectListner) {
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
        final LinearLayout linearLayout = layout.findViewById(R.id.notice_layout);
        final TextView textView = layout.findViewById(R.id.content_tv);
        final TextView textTitleView = layout.findViewById(R.id.content_title);
        textView.setText(Html.fromHtml(content));
        textTitleView.setText(Html.fromHtml(title));
        customDialog2(context, layout, selectListner);
    }

    public static void shareKakaoMessage(
            Context context,
            String title,
            String popupMsg,
            String sendMsg) {
        MaterialDailogUtil.CustomDailogManager(
                context,
                popupMsg,
                title,
                new OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {
                        if (s.equals("share")) {
                            AppUtil.sendSharedData(context, sendMsg);
                        }
                    }
                });
    }

    public static void customDialog2(Context context, View layout, OnDialogSelectListner selectListner) {
        @SuppressLint("ResourceAsColor") MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .checkBoxPrompt("don't again", false, new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Toast.makeText(context, "check : " + isChecked, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .customView(layout, false)
                        .positiveText("[ 닫기 ] ")
                        .positiveColor(R.color.darkGray)
                        .negativeText("[ 앱으로 공유 ]")
                        .cancelable(isCancelable)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (selectListner != null)
                                    selectListner.onSelect("share");
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (selectListner != null)
                                    selectListner.onSelect("ok");
                            }
                        })
                        .show();
    }

    public static void customYesNoDialog(Context context, View layout, OnDialogSelectListner selectListner) {
        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .checkBoxPrompt("don't again", false, new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Toast.makeText(context, "check : " + isChecked, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .customView(layout, false)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.no)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (selectListner != null)
                                    selectListner.onSelect("ok");
                            }
                        })
                        .show();
    }


    public static void customDialog(Context context, View layout, OnDialogSelectListner selectListner) {
        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .checkBoxPrompt("don't again", false, new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Toast.makeText(context, "check : " + isChecked, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .customView(layout, false)
                        .positiveText(android.R.string.ok)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (selectListner != null)
                                    selectListner.onSelect("ok");
                            }
                        })
                        .show();
    }

    public static void datePickerDialog2(Context context, final OnDialogSelectListner selectListner) {
        View pickerView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_datepicker, null);

        final DatePicker datePicker = pickerView.findViewById(R.id.datePicker);
        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .title(R.string.select_date)
                        .customView(datePicker, false)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int year = datePicker.getYear();
                                int mon = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, mon, day);

                                if (selectListner != null)
                                    selectListner.onSelect(year + "-" + (mon + 1) + "-" + day);
                                dialog.dismiss();
                            }
                        })
                        .show();
    }

    public static void datePickerDialog(Context context, final OnDialogSelectListner selectListner) {
        View pickerView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_datepicker, null);

        final DatePicker datePicker = pickerView.findViewById(R.id.datePicker);

        if (CommonData.getSelectedYear() != -1) {
            //datePicker.init(CommonData.getSelectedYear(),CommonData.getSelectedMonth(),CommonData.getSelectedDay());
            datePicker.init(CommonData.getSelectedYear(), CommonData.getSelectedMonth() - 1, CommonData.getSelectedDay(), null);
        }

        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_info)
                        .title(R.string.select_date)
                        .customView(datePicker, false)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int year = datePicker.getYear();
                                int mon = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, mon, day);

                                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                                CommonData.setSelectedYear(year);
                                CommonData.setSelectedMonth(mon + 1);
                                CommonData.setSelectedDay(day);
                                CommonData.setSelectedDayOfWeek(dayOfWeek);

                                if (selectListner != null)
                                    selectListner.onSelect("days : " + dayOfWeek);
                                dialog.dismiss();
                            }
                        })
                        .show();
    }

    public static void showSingleChoiceReturnStr(
            Context context,
            String title,
            List list,
            final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(
                        0,
                        (dialog, view, which, text) -> {
                            //showToast(context , which + ": " + text);
                            if (selectListner != null)
                                selectListner.onSelect(String.valueOf(text));
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
    }

    public static void showSingleChoice(
            Context context,
            String title,
            List list,
            final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(
                        0,
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
    }

    public static void showSingleChoice(
            Context context,
            String title,
            int itemsRes,
            final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(title)
                .items(itemsRes)
                .itemsCallbackSingleChoice(
                        0,
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
    }

    public static void showSingleChoice(
            Context context,
            int itemsRes,
            final OnDialogSelectListner selectListner) {

        int selectIndex = 0;
        if (CommonData.getSelectedDays() != -1) {
            selectIndex = CommonData.getSelectedDays();
        }

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(R.string.select_time)
                .items(itemsRes)
                .itemsCallbackSingleChoice(
                        selectIndex,
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

    public static void simpleListDialog(
            Context context,
            int itemsRes,
            int title,
            final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(title)
                .items(itemsRes)
                .itemsCallbackSingleChoice(
                        0,
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
    }

    public static void simpleListDialog(
            Context context,
            List<String> strings,
            final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(R.string.simple_list_title)
                .items(strings)
                .itemsCallbackSingleChoice(
                        0,
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
    }

    public static void simpleListDialog(
            Context context,
            int itemsRes,
            final OnDialogSelectListner selectListner) {

        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_info)
                .title(R.string.simple_list_title)
                .items(itemsRes)
                .itemsCallbackSingleChoice(
                        0,
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
    }

    public static void timePickerDialog(Context context, final OnDialogSelectListner selectListner) {
        View pickerView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_timepicker, null);

        final TimePicker timePicker = pickerView.findViewById(R.id.timePicker);

        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .title("시간을 선택하세요.")
                        .customView(timePicker, false)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .iconRes(R.drawable.ic_info)
                        .cancelable(isCancelable)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int hour = timePicker.getHour();
                                int minute = timePicker.getMinute();
                                int baseline = timePicker.getBaseline();

                                if (selectListner != null)
                                    selectListner.onSelect("hour : " + hour + "/ minute : " + minute + "/ baseline : " + baseline);
                                dialog.dismiss();
                            }
                        })
                        .show();

    }

    public static Toast toast;

    public static void showToast(Context context, String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public interface OnDialogSelectListner {
        void onSelect(String s);
    }

    public static String getExcutive(int positionNum) {
        String position = "";
        switch (Integer.valueOf(positionNum)) {
            case 1:
                position = "성도/회원";
                break;
            case 0:
                position = "임원";
                break;

            default:
                break;
        }
        return position;
    }

    public static String getNew(int positionNum) {
        String position = "";
        switch (Integer.valueOf(positionNum)) {
            case 1:
                position = "새신자";
                break;
            case 0:
                position = "기존";
                break;

            default:
                break;
        }
        return position;
    }


    public static String getGender(int positionNum) {
        String position = "";
        switch (Integer.valueOf(positionNum)) {
            case 1:
                position = "여성";
                break;
            case 0:
                position = "남성";
                break;

            default:
                break;
        }
        return position;
    }

    public static String getPosition(int positionNum) {

        String position = "";
        switch (Integer.valueOf(positionNum)) {
            case 0:
                position = "담임목사";
                break;
            case 1:
                position = "담임사모";
                break;
            case 2:
                position = "전도사";
            case 3:
                position = "전도사";
                break;
            case 4:
                position = "교육사";
                break;
            case 5:
                position = "간사";
                break;
            case 6:
                position = "부장";
                break;
            case 7:
                position = "지역장";
                break;
            case 8:
                position = "부지역장";
                break;
            case 9:
                position = "총무";
                break;
            case 10:
                position = "부총무";
                break;
            case 11:
                position = "장로";
                break;
            case 12:
                position = "권사";
                break;
            case 13:
                position = "안수집사";
                break;
            case 14:
                position = "집사";
                break;
            case 15:
                position = "성도";
                break;
            default:
                break;
        }
        return position;
    }

    public String getDateDay(String date, String dateType) throws Exception {

        String day = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);
        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }
        return day;
    }

}
