package com.buel.holyhelper.model;

import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AttendDataManager 받은 데이터셋
 */
public class AttendDataModel {

    //브리핑 멘트
    private String ment="";
    //텍스트 멘트
    private String txtypeMent="";

    private String popMent="";
    //평균
    private String avg="";
    //성장률
    private double growthRate =0;
    //선택 달
    private int curMonth;
    //선택 요일
    private int dayOfWeek;
    //선택 연도
    private int curYear;
    private int curDays;
    private double growthCnt;
    private BarData barData;
    private String[] xAxisArr;

    private List<String> newList = new ArrayList<>();

    public List<String> getNewList() {
        return newList;
    }

    public void setNewList(List<String> newList) {
        this.newList = newList;
    }

    private List<String> reasonList = new ArrayList<>();
    private List<String> okExcutiveAttendList = new ArrayList<>();
    private List<String> okAttendList = new ArrayList<>();
    private List<String> noAttendList = new ArrayList<>();

    public List<String> getOkExcutiveAttendList() {
        return okExcutiveAttendList;
    }

    public void setOkExcutiveAttendList(List<String> okExcutiveAttendList) {
        this.okExcutiveAttendList = okExcutiveAttendList;
    }

    //데이터 리스트
    private HashMap<String, ArrayList<AttendModel>> weekAttendDataListMap;

    public String getPopMent() {
        return popMent;
    }

    public void setPopMent(String popMent) {
        this.popMent = popMent;
    }

    public List<String> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<String> reasonList) {
        this.reasonList = reasonList;
    }

    public String[] getxAxisArr() {
        return xAxisArr;
    }

    public void setxAxisArr(String[] xAxisArr) {
        this.xAxisArr = xAxisArr;
    }

    public BarData getBarData() {
        return barData;
    }

    public void setBarData(BarData barData) {
        this.barData = barData;
    }

    public String getTxtypeMent() {
        return txtypeMent;
    }

    public void setTxtypeMent(String txtypeMent) {
        this.txtypeMent = txtypeMent;
    }

    public List<String> getOkAttendList() {
        return okAttendList;
    }

    public void setOkAttendList(List<String> okAttendList) {
        this.okAttendList = okAttendList;
    }

    public List<String> getNoAttendList() {
        return noAttendList;
    }

    public void setNoAttendList(List<String> noAttendList) {
        this.noAttendList = noAttendList;
    }

    public double getGrowthCnt() {
        return growthCnt;
    }

    public void setGrowthCnt ( double growthCnt) {
        this.growthCnt = growthCnt;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public int getCurDays() {
        return curDays;
    }

    public void setCurDays(int curDays) {
        this.curDays = curDays;
    }


    public int getCurYear() {
        return curYear;
    }

    public void setCurYear(int curYear) {
        this.curYear = curYear;
    }

    public String getMent() {
        return ment;
    }

    public void setMent(String ment) {
        this.ment = ment;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public int getCurMonth() {
        return curMonth;
    }

    public void setCurMonth(int curMonth) {
        this.curMonth = curMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public HashMap<String, ArrayList<AttendModel>> getWeekAttendDataListMap() {
        return weekAttendDataListMap;
    }

    public void setWeekAttendDataListMap(HashMap<String, ArrayList<AttendModel>> weekAttendDataListMap) {
        this.weekAttendDataListMap = weekAttendDataListMap;
    }


}
