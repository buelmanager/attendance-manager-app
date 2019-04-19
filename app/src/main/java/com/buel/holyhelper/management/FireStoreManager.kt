@file:Suppress("UNCHECKED_CAST")

package com.buel.holyhelper.management

import android.annotation.SuppressLint
import android.util.Log
import com.buel.holyhelper.data.CommonData
import com.buel.holyhelper.management.firestore.FireStoreUserManager
import com.buel.holyhelper.model.UserModel
import com.buel.holyhelper.view.DataTypeListener
import com.google.firebase.database.FirebaseDatabase
import java.util.*


object FireStoreManager {
    val TAG = CommonData.ADMOB_APP_ID + " / FireStoreWriteManager"

    /**
     * 원하는 유저의 데이터만 가지고 온다.
     */
    @JvmStatic fun getUserData(userModel: UserModel, onDataTypeListener: DataTypeListener.OnCompleteListener<Any>) {
        FireStoreUserManager.getDocData(userModel, DataTypeListener.OnCompleteListener { t ->
            onDataTypeListener.onComplete(t as UserModel?)
        })
    }

    /**
     * admin uid 에 해당하는 유저만 가지고 온다.
     */
    @JvmStatic fun getSubAdminList(userType: String, onDataTypeListener: DataTypeListener.OnCompleteListener<Any>) {
        FireStoreUserManager.getColData(DataTypeListener.OnCompleteListener { t ->
            val userModel = t as ArrayList<UserModel>
            val tempModels = java.util.ArrayList<UserModel>()
            for (eleUserModel in userModel) {
                if (eleUserModel.adminUID == CommonData.getAdminUid()) {
                    if (userType == eleUserModel.userType) {
                        tempModels.add(eleUserModel)
                    }
                }
            }
            onDataTypeListener.onComplete(tempModels)
        })
    }

    /**
     * USER 데이터 베이스에 UserModel 데이터를 추가한다.
     *
     * @param uid
     * @param userModel
     * @param taskOnCompleteListener
     */
    @JvmStatic fun sendUserDataInsertUserModel(userModel: UserModel, onCompleteListener: DataTypeListener.OnCompleteListener<Boolean>) {
        FireStoreUserManager.insert(userModel, DataTypeListener.OnCompleteListener {
            onCompleteListener.onComplete(it)
        })
    }

    /**
     * USER 데이터 베이스에 MAP 데이터를 추가한다.
     *
     * @param uid
     * @param value
     */
    @SuppressLint("LongLogTag")
    @JvmStatic fun sendUserDataAddDataMap(uid: String, key: String, value: String) {
        val tokenMap = HashMap<String, Any>()
        tokenMap[key] = value
        tokenMap["uid"] = uid

        FireStoreUserManager.modify(tokenMap, DataTypeListener.OnCompleteListener {
            Log.d(TAG, "데이터 추가 완료되었습니다.")
            Log.d(TAG, "key : $key value : $value")
        })
    }

    /**
     * @param userModel
     * @param noprice
     */
    @JvmStatic fun sendUpdateSimpleDoc(userModel: UserModel, noprice: String, onDataTypeListener :DataTypeListener.OnCompleteListener<Any>) {
        FirebaseDatabase.getInstance().reference
                .child(noprice)
                .push()
                .setValue(userModel)
                .addOnCompleteListener { onDataTypeListener.onComplete(true) }
    }
}