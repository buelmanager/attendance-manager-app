package com.buel.holyhelper.view.viewPage.briefingPageViewFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.model.AttendDataModel;
import com.buel.holyhelper.model.AttendModel;
import com.buel.holyhelper.model.ChartListenerModel;
import com.buel.holyhelper.utils.AttendDataManager;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.DataTypeListener;
import com.buel.holyhelper.view.MakeBarChartView;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.HashMap;

/**
 * Created by blue7 on 2018-05-16.
 */
public class DetailBriefingFragment extends AbstBriefingFragment {

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private LinearLayout linearLayout;
    private NestedScrollView scrollView;
    private TextView tvTitle;
    View rootView;

    HashMap<String, Integer> okDateMaps;
    HashMap<String, Integer> noDateMaps;


    AttendModel attendModel = null;

    AttendDataModel attendDataModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.corps_step1_view_page_fragment, container, false);
        LoggerHelper.i("GroupBriefingFragment onCreateView");

        this.rootView = rootView;

        mTfRegular = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");

        linearLayout = rootView.findViewById(R.id.corps_step1_view_page_fragment_ll);
        scrollView = rootView.findViewById(R.id.corps_step1_view_page_fragment_sv);
        tvTitle = rootView.findViewById(R.id.corps_step1_view_page_tv_title);

        //LoggerHelper.d("CommonData.getGroupUid() : " + CommonData.getGroupUid());
        //LoggerHelper.d("CommonData.getGroupUid() : " + CommonData.getTeamUid());

        if (CommonData.getGroupUid() != null &&
                CommonData.getTeamUid() != null) {
            getAttandData();
            tvTitle.setText("* 현재 설정된 [ " + CommonData.getGroupModel().name + " " + SortMapUtil.getInteger(CommonData.getTeamModel().name) + " : " + CommonData.getTeamModel().etc + " ] 의 통계입니다.");
        } else {
            SuperToastUtil.toastE(getContext(), "부서 또는 소그룹, 날짜 정보가 설정되지 않았습니다.");
        }
        return rootView;
    }

    private Context context;

    @SuppressLint("SetTextI18n")
    private void setPage(AttendDataModel attendDataModel) {

        this.attendDataModel = attendDataModel;

        if (CommonData.getGroupModel() == null || CommonData.getTeamModel() == null) {
            SuperToastUtil.toastE(getContext(), CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP);
            return;
        }

        if (linearLayout.getClipChildren()) linearLayout.removeAllViews();

        context = rootView.getContext();
        View convertView3 = LayoutInflater.from(context).inflate(
                R.layout.viewpager_title, null);
        TextView titleTv3 = (TextView) convertView3.findViewById(R.id.view_pager_tv_title);
        titleTv3.setTextSize(20);
        titleTv3.setText(Html.fromHtml(
                CommonData.getCurrentFullDateStr() + "  " +
                        "<strong>월간 통계</strong>"));

        linearLayout.addView(convertView3);

        View convertView = LayoutInflater.from(context).inflate(R.layout.viewpager_title, null);
        TextView titleTv = (TextView) convertView.findViewById(R.id.view_pager_tv_title);

        titleTv.setText(Html.fromHtml("<strong> [ " + " " + CommonData.getGroupModel().name + " " + SortMapUtil.getInteger(CommonData.getTeamModel().name) + " : " + CommonData.getTeamModel().etc + " ]</strong> 출석통계"));
        linearLayout.addView(convertView);

        String type1 = "출석";
        String mentStr = attendDataModel.getMent();

        MakeBarChartView chart = new MakeBarChartView(attendDataModel.getBarData(), context, attendDataModel.getxAxisArr(), mentStr, new DataTypeListener.OnCompleteListener<ChartListenerModel>() {
            /**
             * CHART 를 클릭했을 때 해당 값을 받아온다.
             * @param chartListenerModel
             */
            @Override
            public void onComplete(ChartListenerModel chartListenerModel) {

                if (chartListenerModel.xData.equals("-")) return;

                if (chartListenerModel.yData > 0) {
                    getCurDateAttendData(chartListenerModel.xData, type1);
                }
            }
        });

        View chartView = chart.getConvertView();
        linearLayout.addView(chartView);

        convertView5 = LayoutInflater.from(context).inflate(R.layout.viewpager_content, null);
        contentTv = (TextView) convertView5.findViewById(R.id.view_pager_tv_content);
        contentTitletv = (TextView) convertView5.findViewById(R.id.view_pager_tv_title);
        shareIV = (ImageView) convertView5.findViewById(R.id.view_pager_content_share);
        //AppUtil.setBackColor(context , shareIV , R.color.vordiplom_color_orange);
        linearLayout.addView(convertView5);

        shareIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAttendDataModel != null) {
                    MaterialDailogUtil.Companion.shareKakaoMessage(context, type1 + " 통계 ", selectedAttendDataModel.getPopMent(), selectedAttendDataModel.getTxtypeMent());
                }else{
                    Toast.makeText(context, "출석 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        setDetailMent();
    }

    private void setDetailMent()
    {
        if(selectedAttendDataModel ==null){
            contentTitletv.setText(Html.fromHtml("<strong>[ 당일 통계 ] </strong><BR> 확인하실 날짜의 그래프를 클릭하세요."));
            contentTv.setText("");
            shareIV.setVisibility(View.GONE);
        }else{
            contentTitletv.setText(Html.fromHtml("<strong> [ " + selectedAttendDataModel.getCurDays() + "일 출석통계 ] </strong>"));
            contentTv.setText(Html.fromHtml(selectedAttendDataModel.getPopMent()));
            shareIV.setVisibility(View.VISIBLE);
        }
    }

    private AttendDataModel selectedAttendDataModel;

    private void getCurDateAttendData(String xData, String type1) {
        selectedAttendDataModel = attendDataManager.getCurDateAttendDataModel(xData);
        String popMent = selectedAttendDataModel.getPopMent();
        String sendMent = selectedAttendDataModel.getTxtypeMent();
        setDetailMent();
    }

    ImageView shareIV;
    View convertView5;
    TextView contentTv;
    TextView contentTitletv;
    AttendDataManager attendDataManager;

    /**
     * 월간 데이터를 AttendDataModel 형식으로 받아온다.
     */
    public void getAttandData() {
        okDateMaps = new HashMap<>();
        noDateMaps = new HashMap<>();
        attendModel = new AttendModel();

        attendDataManager = new AttendDataManager();
        attendDataManager.getMonthlyDate(
                AttendDataManager.DATA_TYPE.TEAM_DATA,
                CommonData.getSelectedYear(),
                CommonData.getSelectedMonth(),
                CommonData.getSelectedDayOfWeek(),
                CommonData.getSelectedDay(),
                CommonData.getSelectedDays(),
                CommonData.getTeamUid(),
                CommonData.getAttendDateMaps(),
                new DataTypeListener.OnCompleteListener<AttendDataModel>() {
                    private AttendDataModel attendDataModel;

                    @Override
                    public void onComplete(AttendDataModel attendDataModel) {

                        setPage(attendDataModel);
                    }
                });
    }
}