package com.buel.holyhelpers.utils

import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.model.PushDTO
import com.buel.holyhelpers.model.UserModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class FcmPush() {
    var JSON = MediaType.parse("application/json; charset=utf-8")
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = CommonString.FIREBASE_SERVER_KEY;
    val okHttpClient: OkHttpClient by lazy { OkHttpClient() }
    val gson: Gson by lazy { Gson() }

    init {

    }

    fun senMessage(userModel: UserModel , title:String?, message:String?)
    {
        var token = userModel.pushToken
        var pushDTO = PushDTO()
        pushDTO.to = token
        pushDTO.data?.title = title
        pushDTO.data?.body = message

        var body = RequestBody.create(JSON, gson.toJson(pushDTO))
        var request = Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","key=" + serverKey)
                .url(url)
                .post(body)
                .build()

        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

            }

        })
    }
}