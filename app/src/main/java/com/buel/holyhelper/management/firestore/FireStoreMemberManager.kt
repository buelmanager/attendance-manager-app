package com.buel.holyhelper.management.firestore

import android.annotation.SuppressLint
import android.util.Log
import com.buel.holyhelper.data.CommonData
import com.buel.holyhelper.data.FDDatabaseHelper
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.model.UserModel
import com.buel.holyhelper.view.DataTypeListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object FireStoreMemberManager {
    val TAG = CommonData.ADMOB_APP_ID + " / FireStoreMemberManager"

    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    /***
     * 반환을 UserModel 로 한다.
     */
    @SuppressLint("LongLogTag")
    @JvmStatic fun getDocData(listener: DataTypeListener.OnCompleteListener<HashMap<String?, HolyModel.memberModel>>) {
        val userRef =
                firestore.collection(FDDatabaseHelper.CORPS_TABLE)
                        .document(CommonData.getCorpsUid())
                        .collection(FDDatabaseHelper.MEMBER_TABLE)

        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Log.d(TAG, "task.isSuccessful: " + task.isSuccessful)

                var memberMap = HashMap<String?, HolyModel.memberModel>()
                var memberModel: HolyModel.memberModel

                //Log.d(TAG, "task.isSuccessful: " + task.result!!.size())

                for (document in task.result!!) {
                    memberModel = document.toObject(HolyModel.memberModel::class.java)
                    memberMap[document.id] = memberModel

                    memberModel.uid = document.id
                    //Log.d(TAG, "memberModel : " + memberModel.uid)
                    //Log.d(TAG, "memberModel : " + memberModel.name)
                    //Log.d(TAG, "===============================================")
                }
                Log.d(TAG, "memberMap size : " + memberMap.size)
                listener.onComplete(memberMap)
            } else {
                Log.d(TAG, "Error getting documents: ", task.exception)
            }
        }
    }

    /**
     * 반환을 리스트로 한다.
     */
    @SuppressLint("LongLogTag")
    fun getColData(listener: DataTypeListener.OnCompleteListener<Any>) {
        var userColRef: CollectionReference = firestore.collection(FDDatabaseHelper.USERS_TABLE)
        userColRef.orderBy("userName").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var users = ArrayList<UserModel>()
                var userModel: UserModel
                for (document in task.result!!) {
                    userModel = document.toObject(UserModel::class.java)
                    users.add(userModel)
                }
                listener.onComplete(users)

            } else {
                Log.d(TAG, "Error getting documents: ", task.exception)
            }
        }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic fun insert(dataModel: HolyModel.memberModel, listener: DataTypeListener.OnCompleteListener<Boolean>) {
        Log.d(TAG, "insert")
        val userRef =
                firestore.collection(FDDatabaseHelper.CORPS_TABLE)
                        .document(dataModel.corpsUID)
                        .collection(FDDatabaseHelper.MEMBER_TABLE)
                        .document()

        userRef.set(dataModel)
                .addOnCompleteListener { task ->
                    listener.onComplete(task.isSuccessful)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    listener.onComplete(false)
                }
    }

    /**
     * 수정은 맵형태로 받는다.
     */
    @SuppressLint("LongLogTag")
    @JvmStatic fun modify(dataModel: MutableMap<String, Any>,
                          listener: DataTypeListener.OnCompleteListener<Boolean>) {
        Log.d(TAG, "modify")
        Log.d(TAG, dataModel.toString())
        val map = dataModel
        val userRef = firestore.collection(FDDatabaseHelper.CORPS_TABLE)
                .document(CommonData.getCorpsUid())
                .collection(FDDatabaseHelper.MEMBER_TABLE)
                .document((map["uid"] as String?)!!)

        userRef.update(map as Map<String, Any>)
                .addOnCompleteListener { task ->
                    listener.onComplete(true)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    listener.onComplete(false)
                }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic fun delete(dataModel: HolyModel.memberModel, listener: DataTypeListener.OnCompleteListener<Boolean>) {
        val userRef =
                firestore.collection(FDDatabaseHelper.CORPS_TABLE)
                        .document(dataModel.corpsUID)
                        .collection(FDDatabaseHelper.MEMBER_TABLE)
                        .document(dataModel.uid)

        userRef.delete()
                .addOnCompleteListener { task ->
                    listener.onComplete(true)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    listener.onComplete(false)
                }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic
    fun multiInsert(memberList: List<HolyModel.memberModel>, listener: DataTypeListener.OnCompleteListener<Boolean>) {

        val batch = FireStoreAttendManager.firestore.batch()

        for (eleMember in memberList) {
            val attendDocRef =
                    firestore.collection(FDDatabaseHelper.CORPS_TABLE)
                            .document(eleMember.corpsUID)
                            .collection(FDDatabaseHelper.MEMBER_TABLE)
                            .document()
            batch.set(attendDocRef, eleMember)
        }

        batch.commit().addOnSuccessListener {
            listener.onComplete(true)
        }.addOnFailureListener {
            listener.onComplete(false)
        }
    }
}
