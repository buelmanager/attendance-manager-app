package com.buel.holyhelper.management.firestore

import android.annotation.SuppressLint
import android.util.Log
import com.buel.holyhelper.data.CommonData
import com.buel.holyhelper.data.FDDatabaseHelper
import com.buel.holyhelper.model.AttendModel
import com.buel.holyhelper.model.DateModel
import com.buel.holyhelper.view.DataTypeListener
import com.buel.holyhelper.view.SimpleListener
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.LoggerHelper


object FireStoreAttendManager {
    val TAG = "FireStoreAttendManager"

    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    var attendRef = firestore.collection(FDDatabaseHelper.ATTEND_TABLE)
            .document(FDDatabaseHelper.ATTEND)
            .collection(FDDatabaseHelper.CORPS_TABLE)

    /**
     * 반환을 리스트로 한다.
     */
    @SuppressLint("LongLogTag")
    @JvmStatic
    fun getAttendDayData(
            dataModel: AttendModel,
            onListener: DataTypeListener.OnCompleteListener<HashMap<String?, String>>) {

        val attendDoc = attendRef
                .document(CommonData.getCorpsUid())
                .collection(FDDatabaseHelper.ATTEND_MEMBER_TABLE)
                .whereEqualTo("fdate", dataModel.fdate)
                .whereEqualTo("teamUID", dataModel.teamUID)

        attendDoc.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Log.d(TAG, "task.isSuccessful: " + task.isSuccessful)

                var attendModels = HashMap<String?, String>()
                var attendModel: AttendModel
                for (document in task.result!!) {
                    attendModel = document.toObject(AttendModel::class.java)
                    attendModels[attendModel.memberName] = attendModel.attend ?: "false"
                }
                onListener.onComplete(attendModels)
            } else {
                onListener.onComplete(null)
                Log.d(TAG, "Error getAttendDayData documents: ", task.exception)
            }
        }
    }

    /**
     * 반환을 리스트로 한다.
     */
    @SuppressLint("LongLogTag")
    @JvmStatic
    fun getAttendRefData(
            dataModel: AttendModel,
            onListener: DataTypeListener.OnCompleteListener<HashMap<String?, String>>) {

        val attendDoc = attendRef
                .document(dataModel.corpsUID)
                .collection(FDDatabaseHelper.GROUP_TABLE)
                .document(dataModel.groupUID)
                .collection(FDDatabaseHelper.ATTEND_DATE_TABLE)
                .document(dataModel.fdate)
                .collection(FDDatabaseHelper.ATTEND_MEMBER_TABLE)
                .whereEqualTo("teamUID", CommonData.getTeamUid())

        attendDoc.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Log.d(TAG, "task.isSuccessful: " + task.isSuccessful)

                var attendModels = HashMap<String?, String>()
                var attendModel: AttendModel
                for (document in task.result!!) {
                    attendModel = document.toObject(AttendModel::class.java)
                    attendModels[attendModel.memberName] = attendModel.attend ?: "false"
                }
                onListener.onComplete(attendModels)
            } else {
                onListener.onComplete(null)
                Log.d(TAG, "Error getAttendDayData documents: ", task.exception)
            }
        }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic
    fun insert(dataModel: AttendModel, listener: DataTypeListener.OnCompleteListener<Boolean>) {

        Log.d(TAG, dataModel.toString())

        val attendDoc = attendRef
                .document(dataModel.corpsUID)

        attendDoc
                .collection(FDDatabaseHelper.ATTEND_MEMBER_TABLE)
                .document()
                .set(dataModel)
                .addOnSuccessListener {
                    listener.onComplete(true)
                }
                .addOnFailureListener {
                    listener.onComplete(false)
                }
    }

    @SuppressLint("LongLogTag")
    @JvmStatic
    fun multiInsert(memberList: List<AttendModel>, listener: DataTypeListener.OnCompleteListener<Boolean>) {

        val batch = firestore.batch()

        for (eleMember in memberList) {
            val attendDocRef =
                    attendRef.document(eleMember.corpsUID)
                            .collection(FDDatabaseHelper.ATTEND_MEMBER_TABLE)
                            .document(eleMember.memberUID)
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
    fun getAttandAllData(
            dataModel: AttendModel,
            onSimpleListener: SimpleListener.OnCompleteListener) {

        Log.d(TAG, "getAttandAllData")
        Log.d(TAG, "dataModel.month : " + dataModel.month)
        Log.d(TAG, "dataModel.groupUID : " + dataModel.groupUID)
        Log.d(TAG, "CommonData.getCorpsUid() : " + CommonData.getCorpsUid())

        var userColRef = attendRef
                .document(CommonData.getCorpsUid())
                .collection(FDDatabaseHelper.ATTEND_MEMBER_TABLE)
                .whereEqualTo("month", dataModel.month)
                .whereEqualTo("groupUID", dataModel.groupUID)

        userColRef.get().addOnSuccessListener { querySnapshot ->
            Log.d(TAG, "documents  : " + querySnapshot.documents)

            var attendMap: HashMap<String, AttendModel> = HashMap()
            var dateModelMap: HashMap<String, DateModel> = HashMap()
            val dateModel = DateModel()

            Log.d(TAG, "querySnapshot.size()  : " + querySnapshot.size())

            for (document in querySnapshot) {

                val attendModel: AttendModel = document.toObject(AttendModel::class.java)
                attendMap.put(document.id, attendModel)
                dateModel.member = attendMap
                dateModelMap.put(attendModel.fdate, dateModel)
            }

            CommonData.setAttendDateMaps(dateModelMap)
            LoggerHelper.d("getAttandAllData : complete")
            onSimpleListener.onComplete()
        }
    }
}
