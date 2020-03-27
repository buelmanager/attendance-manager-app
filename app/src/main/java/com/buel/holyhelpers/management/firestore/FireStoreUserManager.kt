package com.buel.holyhelpers.management.firestore

import android.annotation.SuppressLint
import android.util.Log
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.FDDatabaseHelper
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.model.UserModel
import com.buel.holyhelpers.view.DataTypeListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FireStoreUserManager {
    val TAG = CommonData.ADMOB_APP_ID + " / FireStoreUserManager"

    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    /***
     * 반환을 UserModel 로 한다.
     */
    @SuppressLint("LongLogTag")
    fun getDocData(dataModel: Any, listener: DataTypeListener.OnCompleteListener<Any>) {
        var userDocRef: DocumentReference =
                firestore.collection(FDDatabaseHelper.USERS_TABLE)
                        .document((dataModel as UserModel).uid!!)

        userDocRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userModel = task.result!!.toObject(UserModel::class.java)
                listener.onComplete(userModel)
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
    fun insert(dataModel: Any, listener: DataTypeListener.OnCompleteListener<Boolean>) {
        val userRef =
                firestore.collection(FDDatabaseHelper.USERS_TABLE)
                        .document((dataModel as UserModel).uid!!)
        userRef.set(dataModel)
                .addOnCompleteListener { task ->
                    listener.onComplete(task.isSuccessful)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
    }

    /**
     * 수정은 맵형태로 받는다.
     */
    @SuppressLint("LongLogTag")
    fun modify(dataModel: java.util.HashMap<String, Any>,
               listener: DataTypeListener.OnCompleteListener<Any>) {
        val map = dataModel
        val userRef = firestore.collection(FDDatabaseHelper.USERS_TABLE)
                .document((map["uid"] as String?)!!)
        userRef.update(map as Map<String, Any>)
                .addOnCompleteListener { task ->
                    listener.onComplete(task)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
    }

    @SuppressLint("LongLogTag")
    fun delete(dataModel: UserModel, listener: DataTypeListener.OnCompleteListener<Any>) {
        val userRef =
                firestore.collection(FDDatabaseHelper.USERS_TABLE)
                        .document(dataModel.uid!!)
        userRef.delete()
                .addOnCompleteListener { task ->
                    listener.onComplete(task)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
    }


    @SuppressLint("LongLogTag")
    @JvmStatic
    fun multiInsert(memberList: List<HolyModel.memberModel>, listener: DataTypeListener.OnCompleteListener<Boolean>) {

        val batch = FireStoreAttendManager.firestore.batch()

        for (eleMember in memberList) {
            val attendDocRef =
                    firestore.collection(FDDatabaseHelper.USERS_TABLE)
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
