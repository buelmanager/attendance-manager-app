package com.buel.holyhelper.management.firestore

import android.annotation.SuppressLint
import android.util.Log
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.view.DataTypeListener
import com.commonLib.Common
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.LoggerHelper

object FireStoreWriteManager {
    val TAG = Common.PACKAGE_NAME + " / FireStoreWriteManager"

    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    @SuppressLint("LongLogTag")
    @JvmStatic
    fun insert(documentReference: DocumentReference,
               dataModel: Any,
               listener: DataTypeListener.OnCompleteListener<Boolean>) {
        val userRef = documentReference
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
    @JvmStatic
    fun modify(documentReference: DocumentReference,
               dataModel: Any,
               listener: DataTypeListener.OnCompleteListener<Boolean>) {
        val userRef = documentReference
        userRef.update(dataModel as MutableMap<String, Any>)
                .addOnCompleteListener { task ->
                    listener.onComplete(task.isSuccessful)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic
    fun delete(documentReference: DocumentReference,
               listener: DataTypeListener.OnCompleteListener<Boolean>) {
        val userRef = documentReference
        userRef.delete()
                .addOnCompleteListener { task ->
                    listener.onComplete(task.isSuccessful)
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic
    fun groupMultiInsert(memberList: List<HolyModel.groupModel>,
                         colReference: CollectionReference,
                         listener: DataTypeListener.OnCompleteListener<Boolean>) {

        val batch = firestore.batch()
        for (eleMember in memberList) {
            val attendDocRef = colReference.document()
            LoggerHelper.d("start groupMultiInsert : " + eleMember.name)
            batch.set(attendDocRef, eleMember)
        }

        batch.commit().addOnSuccessListener {
            listener.onComplete(true)
        }.addOnFailureListener {
            listener.onComplete(false)
        }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic
    fun teamMultiInsert(memberList: List<HolyModel.groupModel.teamModel>,
                         colReference: CollectionReference,
                         listener: DataTypeListener.OnCompleteListener<Boolean>) {

        val batch = firestore.batch()
        for (eleMember in memberList) {
            val attendDocRef = colReference.document()
            LoggerHelper.d("start teamMultiInsert : " + eleMember.name)
            batch.set(attendDocRef, eleMember)
        }

        batch.commit().addOnSuccessListener {
            listener.onComplete(true)
        }.addOnFailureListener {
            listener.onComplete(false)
        }
    }

}
