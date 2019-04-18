package com.buel.holyhelper.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.PremiupType;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.model.UserModel;
import com.buel.holyhelper.view.SimpleListener;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AppUtil {

    public static Integer getRandomMaterialColor() {
        int colorNum = 0;

        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.material_500_amber);
        colorList.add(R.color.material_500_blue);
        colorList.add(R.color.material_500_cyan);
        colorList.add(R.color.material_500_deep_purple);
        colorList.add(R.color.material_500_indigo);
        colorList.add(R.color.material_500_light_blue);
        colorList.add(R.color.material_500_lime);
        colorList.add(R.color.material_500_pink);
        colorList.add(R.color.material_500_red);
        colorList.add(R.color.material_500_purple);

        int randNum = (int) getRandom(colorList.size() - 1, 0);
        colorNum = colorList.get(randNum);
        return colorNum;
    }

    public static void sendSubAdminPushMessage( Context context , List<UserModel> userModels , String title,String message){
        FcmPush fcmPush= new FcmPush();
        String strUserName = "";
        for ( UserModel eleModel : userModels){
            //String token = eleModel.pushToken;
            fcmPush.senMessage( eleModel , title , eleModel.userName + " 관리자님, " + message);
            strUserName += eleModel.userName + "\n";
        }

        if(strUserName.equals(""))return;

        MaterialDailogUtil.simpleDoneDialog(context, "운영 관리자에게 메세지를 보냈습니다.", strUserName, s -> {
            //goMain();
        });
    }
    public static void sendSharedData(Context context, String sendMsg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        if (CommonData.getIsAdsOpen().equals("true")) {
            if (CommonData.getCurrentPremiumType() == PremiupType.NORAML)
                sendMsg += "\n\n\nhttps://play.google.com/store/apps/details?id=com.buel.holyhelper";
        }
        intent.putExtra(Intent.EXTRA_TEXT, sendMsg);

        Intent chooser = Intent.createChooser(intent, "공유하기!");

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(chooser);
        }
    }

    public static float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    @SuppressLint("NewApi")
    public static void setBackColor(Context context, ImageView target, int color) {
        target.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
    }

    @SuppressLint("NewApi")
    public static void setBackColor(Context context, Button target, int color) {
        target.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
    }

    public static void setBackColor(Context context, FloatingActionButton fabtn, int color) {
        fabtn.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
    }

    public static Boolean checkAppPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        PERMISSIONS_STORAGE,
                        1);
            }
        }
        return true;
    }


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void checkCorpsPassword(Context context, HolyModel holyModel, SimpleListener listener) {

        MaterialDailogUtil.simpleInputDoneDialog(context, "비번을 입력하세요.", "이름", new MaterialDailogUtil.OnDialogSelectListner() {
            @Override
            public void onSelect(String s) {

                String strName = s;

                if (holyModel.password == null || holyModel.password.equals("")) {
                    SuperToastUtil.toastE(context, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.");

                }

                if (holyModel.password.equals(s)) {
                    CommonData.setViewMode(ViewMode.ADMIN);

                } else {
                    SuperToastUtil.toastE(context, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.");

                }
            }
        });
    }

    @SuppressLint({"NewApi", "MissingPermission"})
    public static void sendCall(Context context, String strTell, boolean direct) {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + strTell));

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
