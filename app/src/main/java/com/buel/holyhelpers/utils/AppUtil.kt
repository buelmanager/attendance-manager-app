package com.buel.holyhelpers.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.currentPremiumType
import com.buel.holyhelpers.data.CommonData.setViewMode
import com.buel.holyhelpers.data.PremiupType
import com.buel.holyhelpers.data.ViewMode
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.model.UserModel
import com.buel.holyhelpers.view.SimpleListener
import com.commonLib.MaterialDailogUtil.Companion.simpleDoneDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleInputDoneDialog
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.commonLib.SuperToastUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

object AppUtil {
    @JvmStatic
    val randomMaterialColor: Int
        get() {
            var colorNum = 0
            val colorList: MutableList<Int> = ArrayList()
            colorList.add(R.color.material_500_amber)
            colorList.add(R.color.material_500_blue)
            colorList.add(R.color.material_500_cyan)
            colorList.add(R.color.material_500_deep_purple)
            colorList.add(R.color.material_500_indigo)
            colorList.add(R.color.material_500_light_blue)
            colorList.add(R.color.material_500_lime)
            colorList.add(R.color.material_500_pink)
            colorList.add(R.color.material_500_red)
            colorList.add(R.color.material_500_purple)
            val randNum = getRandom(colorList.size - 1.toFloat(), 0f).toInt()
            colorNum = colorList[randNum]
            return colorNum
        }

    fun sendSubAdminPushMessage(context: Context?, userModels: List<UserModel>, title: String?, message: String) {
        val fcmPush = FcmPush()
        var strUserName = ""
        for (eleModel in userModels) { //String token = eleModel.pushToken;
            fcmPush.senMessage(eleModel, title, eleModel.userName + " 관리자님, " + message)
            strUserName += eleModel.userName + "\n"
        }
        if (strUserName == "") return
        simpleDoneDialog(context!!, "운영 관리자에게 메세지를 보냈습니다.", strUserName, object : OnDialogSelectListner {
            override fun onSelect(s: String) {

            }
        })
    }

    fun sendSharedData(context: Context, sendMsg: String?) {
        var sendMsg = sendMsg
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        if (CommonData.isAdsOpen.equals("true")) {
            if (currentPremiumType == PremiupType.NORAML) sendMsg += "\n\n\nhttps://play.google.com/store/apps/details?id=com.buel.holyhelper"
        }
        intent.putExtra(Intent.EXTRA_TEXT, sendMsg)
        val chooser = Intent.createChooser(intent, "공유하기!")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooser)
        }
    }

    fun getRandom(range: Float, startsfrom: Float): Float {
        return (Math.random() * range).toFloat() + startsfrom
    }

    @SuppressLint("NewApi")
    fun setBackColor(context: Context, target: ImageView, color: Int) {
        target.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(color))
    }

    @SuppressLint("NewApi")
    fun setBackColor(context: Context, target: Button, color: Int) {
        target.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(color))
    }

    fun setBackColor(context: Context, fabtn: FloatingActionButton, color: Int) {
        fabtn.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(color))
    }

    fun checkAppPermission(context: Context?): Boolean {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity?)!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions((context as Activity?)!!,
                        PERMISSIONS_STORAGE,
                        1)
            }
        }
        return true
    }

    private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun checkCorpsPassword(context: Context?, holyModel: HolyModel, listener: SimpleListener?) {
        simpleInputDoneDialog(context!!, "비번을 입력하세요.", "이름", object : OnDialogSelectListner {
            override fun onSelect(s: String) {
                val strName = s
                if (holyModel.password == null || holyModel.password == "") {
                    SuperToastUtil.toastE(context, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.")
                }
                if (holyModel.password == s) {
                    setViewMode(ViewMode.ADMIN)
                } else {
                    SuperToastUtil.toastE(context, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.")
                }
            }
        })
    }

    @SuppressLint("NewApi", "MissingPermission")
    fun sendCall(context: Context, strTell: String, direct: Boolean) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$strTell")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}