package com.buel.holyhelpers.management.firestore

import android.annotation.SuppressLint
import android.util.Log
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.FDDatabaseHelper
import com.buel.holyhelpers.model.AttendModel
import com.buel.holyhelpers.model.DateModel
import com.buel.holyhelpers.view.DataTypeListener
import com.buel.holyhelpers.view.SimpleListener
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.LoggerHelper
import java.util.*


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
            onListener: DataTypeListener.OnCompleteListener<HashMap<String?, AttendModel>>) {

        val attendDoc = attendRef.document(dataModel.corpsUID)
                .collection(FDDatabaseHelper.GROUP_TABLE)
                .document(dataModel.groupUID)
                .collection(FDDatabaseHelper.ATTEND_DATE_TABLE)
                .document(dataModel.fdate + "^" +dataModel.teamUID)

        attendDoc.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "task.isSuccessful: " + task.isSuccessful)
                var attendModels = HashMap<String?, AttendModel>()

                try {
                    var dateModel: DateModel = task.result?.toObject(DateModel::class.java)!!
                    for ((key, attendModel) in dateModel.member) {
                        attendModels[attendModel.memberName] = attendModel
                    }
                }catch ( e:Exception){
                    LoggerHelper.d("getAttendDayData error : " + e)
                }finally {
                    onListener.onComplete(attendModels)
                }

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
        var dateModel = DateModel()
        dateModel.member = hashMapOf()
        //todo -- 출석체크 다시~~
        for (eleMember in memberList) {
            dateModel.member.put(eleMember.memberUID ,eleMember )
        }

        var attendDateRef =
                attendRef.document(memberList[0].corpsUID)
                        .collection(FDDatabaseHelper.GROUP_TABLE)
                        .document(memberList[0].groupUID)
                        .collection(FDDatabaseHelper.ATTEND_DATE_TABLE)
                        .document(memberList[0].fdate + "^" +memberList[0].teamUID)

        dateModel.fdate = memberList[0].fdate
        dateModel.timestamp = memberList[0].timestamp
        dateModel.date = memberList[0].date
        dateModel.day = memberList[0].day
        dateModel.year = memberList[0].year
        dateModel.month = memberList[0].month
        dateModel.teamUID = memberList[0].teamUID

        attendDateRef.set(dateModel).addOnSuccessListener {
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

        LoggerHelper.d(TAG, "getAttandAllData")
        LoggerHelper.d(TAG, "dataModel.month : " + dataModel.month)
        LoggerHelper.d(TAG, "dataModel.groupUID : " + dataModel.groupUID)
        LoggerHelper.d(TAG, "CommonData.getCorpsUid() : " + CommonData.corpsUid)

        var userColRef = attendRef
                .document(CommonData.corpsUid!!)
                .collection(FDDatabaseHelper.GROUP_TABLE)
                .document(dataModel.groupUID)
                .collection(FDDatabaseHelper.ATTEND_DATE_TABLE)
                .whereEqualTo("month" , dataModel.month)

        userColRef.get().addOnSuccessListener { querySnapshot ->
            Log.d(TAG, "documents  : " + querySnapshot.documents)

            var dateModelMap: HashMap<String, DateModel> = HashMap()
            Log.d(TAG, "querySnapshot.size()  : " + querySnapshot.size())
            for (document in querySnapshot) {
                val dateModel: DateModel = document.toObject(DateModel::class.java)
                dateModelMap.put(dateModel.fdate, dateModel)
            }

            CommonData.attendDateMaps = dateModelMap
            //LoggerHelper.d("getAttandAllData : complete")
            onSimpleListener.onComplete()
        }
    }
}
