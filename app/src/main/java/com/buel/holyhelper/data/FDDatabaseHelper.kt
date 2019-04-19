package com.buel.holyhelper.data

import android.graphics.Bitmap
import android.util.Log
import com.buel.holyhelper.management.FireStoreManager
import com.buel.holyhelper.management.Management
import com.buel.holyhelper.management.firestore.FireStoreAttendManager
import com.buel.holyhelper.management.firestore.FireStoreMemberManager
import com.buel.holyhelper.management.firestore.FireStoreWriteManager
import com.buel.holyhelper.model.AttendModel
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.model.UserModel
import com.buel.holyhelper.utils.SortMapUtil
import com.buel.holyhelper.view.DataReturnListener
import com.buel.holyhelper.view.DataTypeListener
import com.buel.holyhelper.view.SimpleListener
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.orhanobut.logger.LoggerHelper
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Created by 19001283 on 2018-06-01.
 */

object FDDatabaseHelper {

    val CORPS_DATA_SET = 1
    private val GROUP_DATA_SET = 2
    private val TEAM_DATA_SET = 3
    private val MEMBER_DATA_SET = 4
    private val ATTEND_DATA_SET = 5

    val TAG = "FB_DB_HELPER"
    val ATTEND_TABLE = "attendance"
    val ATTEND = "attend"
    val USERS_TABLE = "users"
    val CORPS_TABLE = "corps"
    val GROUP_TABLE = "group"
    val TEAM_TABLE = "team"

    val ADMIN_UID_FIELD = "adminUid"

    val MEMBER_TABLE = "member"
    val ATTEND_YEAR_TABLE = "year" //년
    val ATTEND_MONTH_TABLE = "month" //월
    val ATTEND_DATE_TABLE = "date" //일
    val ATTEND_DAY_TABLE = "day" //요일
    val ATTEND_TIME_TABLE = "time" //새벽 낮 저녁 철야
    val ATTEND_MEMBER_TABLE = "member" //멤버

    private val firestore = FirebaseFirestore.getInstance()

    /**
     * @param adminUID   : adminUID 와 같은 값을 가진 corps만 가지고 온다.
     * @param onFDDListener
     */
    fun getMyCorpsData(
            adminUID: String,
            onFDDListener: FDDatabaseHelper.onFDDCallbackListener?) {
        FDDatabaseHelper.showProgress(true)

        FirebaseDatabase.getInstance().reference
                .child(CORPS_TABLE)
                .orderByChild("adminUid")
                .equalTo(adminUID)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        FDDatabaseHelper.showProgress(false)
                        onFDDListener?.onFromDataComplete(CORPS_DATA_SET, dataSnapshot)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
    }

    fun getCorpsDataToStore(
            adminUID: String,
            onDataListener: DataTypeListener.OnCompleteListener<QuerySnapshot>?) {
        FDDatabaseHelper.showProgress(true)

        firestore.collection(CORPS_TABLE)
                .whereEqualTo(ADMIN_UID_FIELD, adminUID)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onDataListener?.onComplete(task.result)
                    } else {
                        LoggerHelper.e(task.result!!.toString())
                    }
                }
    }

    fun getGroupDataToStore(
            onDataListener:DataTypeListener.OnCompleteListener<QuerySnapshot>) {
        FDDatabaseHelper.showProgress(true)

        firestore.collection(CORPS_TABLE)
                .document(CommonData.getAdminUid())
                .collection(GROUP_TABLE)
                .get().addOnSuccessListener { queryDocumentSnapshots ->

                    val tempMap = HashMap<String, HolyModel.groupModel?>()
                    val documentSnapshots = queryDocumentSnapshots.documents as ArrayList<DocumentSnapshot>

                    for (documentSnapshot in documentSnapshots) {
                        tempMap[documentSnapshot.id] = documentSnapshot.toObject<HolyModel.groupModel>(HolyModel.groupModel::class.java)
                    }

                    CommonData.getHolyModel().group = tempMap
                    CommonData.setGroupMap(tempMap)
                    LoggerHelper.d(CommonData.getHolyModel().group)
                    onDataListener.onComplete(queryDocumentSnapshots)
                }.addOnFailureListener { e -> LoggerHelper.e(e.message) }
    }

    fun getTeamDataToStore(
            onDataListener: DataTypeListener.OnCompleteListener<QuerySnapshot>) {
        FDDatabaseHelper.showProgress(true)

        //team table을 group과 같은 레벨에 넣는다.
        val colRef = firestore.collection(CORPS_TABLE)
                .document(CommonData.getAdminUid())
                .collection(TEAM_TABLE)
                //.whereEqualTo("groupUid" , CommonData.getGroupModel().uid)

        colRef
                .get().addOnSuccessListener { queryDocumentSnapshots ->
                    val tempMap = HashMap<String, HolyModel.groupModel.teamModel?>()
                    val documentSnapshots = queryDocumentSnapshots.documents as ArrayList<DocumentSnapshot>

                    for (documentSnapshot in documentSnapshots) {
                        tempMap[documentSnapshot.id] = documentSnapshot.toObject<HolyModel.groupModel.teamModel>(HolyModel.groupModel.teamModel::class.java)
                    }
                    CommonData.getGroupModel().team = tempMap
                    CommonData.setTeamMap(tempMap)

                    FDDatabaseHelper.showProgress(false)
                    onDataListener.onComplete(queryDocumentSnapshots)
                }
    }

    fun getTeamAllDataToStore(
            onDataListener: DataTypeListener.OnCompleteListener<QuerySnapshot>) {
        FDDatabaseHelper.showProgress(true)

        //team table을 group과 같은 레벨에 넣는다.
        val colRef = firestore.collection(CORPS_TABLE)
                .document(CommonData.getAdminUid())
                .collection(TEAM_TABLE)

        colRef
                .get().addOnSuccessListener { queryDocumentSnapshots ->
                    val tempMap = HashMap<String, HolyModel.groupModel.teamModel?>()
                    val documentSnapshots = queryDocumentSnapshots.documents as ArrayList<DocumentSnapshot>

                    for (documentSnapshot in documentSnapshots) {
                        tempMap[documentSnapshot.id] = documentSnapshot.toObject<HolyModel.groupModel.teamModel>(HolyModel.groupModel.teamModel::class.java)
                    }
                    //CommonData.getGroupModel().team = tempMap
                    CommonData.setTeamMap(tempMap)

                    FDDatabaseHelper.showProgress(false)
                    onDataListener.onComplete(queryDocumentSnapshots)
                }
    }

    /**
     * @param userModel
     * @param noprice
     */
    fun sendUpdateSimpleDoc(userModel: UserModel, noprice: String, onCompleteListener: DataTypeListener.OnCompleteListener<Boolean>) {
        FireStoreManager.sendUpdateSimpleDoc(userModel, noprice, DataTypeListener.OnCompleteListener {
            bool ->
            onCompleteListener.onComplete(bool as Boolean?)
        })
    }

    private fun fddatabaseHelperErrorFun(databaseError: DatabaseError) {
        LoggerHelper.e(databaseError.toString())
    }

    /**
     * USER 데이터 베이스에 UserModel 데이터를 추가한다.
     *
     * @param uid
     * @param userModel
     * @param completeListener
     */
    fun sendUserDataInsertUserModel(uid: String, userModel: UserModel, completeListener: DataTypeListener.OnCompleteListener<Boolean>) {
        FireStoreManager.sendUserDataInsertUserModel(userModel, completeListener::onComplete as DataTypeListener.OnCompleteListener<Boolean>)
        FireStoreManager.sendUserDataInsertUserModel(userModel, DataTypeListener.OnCompleteListener {
            t ->
        })
    }

    fun sendCorpsDeleteData(dataModel: HolyModel, listener: Management.OnCompleteListener<*>?) {
        FireStoreWriteManager.delete(
                firestore.collection(CORPS_TABLE)
                        .document(dataModel.uid),
                DataTypeListener.OnCompleteListener {
                    aBoolean -> listener?.onComplete(null)
                }
        )
    }

    fun sendCorpsModifyMapData(dataModel: HolyModel, map: Map<String, Any>, listener: Management.OnCompleteListener<*>?) {
        FireStoreWriteManager.modify(
                firestore.collection(CORPS_TABLE)
                        .document(dataModel.uid),
                map,
                DataTypeListener.OnCompleteListener {
                    aBoolean -> listener?.onComplete(null)
                }
        )
    }

    fun sendCorpsModifyData(dataModel: HolyModel, listener: Management.OnCompleteListener<*>?) {
        val map = dataModel.convertMap()
        FireStoreWriteManager.modify(
                firestore.collection(CORPS_TABLE)
                        .document(dataModel.uid),
                map,
                DataTypeListener.OnCompleteListener {
                    aBoolean -> listener?.onComplete(null)
                }
        )
    }

    fun sendCorpsInsertData(dataModel: HolyModel, listener: Management.OnCompleteListener<*>?) {
        FireStoreWriteManager.insert(
                firestore.collection(CORPS_TABLE)
                        .document(dataModel.adminUid),
                dataModel,
                DataTypeListener.OnCompleteListener {
                    aBoolean -> listener?.onComplete(null)
                }
        )
    }


    fun sendGroupDeleteData(dataModel: HolyModel.groupModel, listener: Management.OnCompleteListener<*>?) {
        FireStoreWriteManager.delete(
                firestore.collection(CORPS_TABLE)
                        .document(CommonData.getCorpsUid())
                        .collection(GROUP_TABLE)
                        .document(dataModel.uid)
        , DataTypeListener.OnCompleteListener {
            listener?.onComplete(null)
        })
    }

    fun sendGroupModifyData(dataModel: HolyModel.groupModel, listener: Management.OnCompleteListener<*>?) {

        val map = dataModel.convertMap()

        FireStoreWriteManager.modify(
                firestore.collection(CORPS_TABLE)
                        .document(CommonData.getCorpsUid())
                        .collection(GROUP_TABLE)
                        .document(dataModel.uid),
                map,
                DataTypeListener.OnCompleteListener {
                    listener?.onComplete(null)
                })
    }

    fun sendGroupInsertData(dataModel: HolyModel.groupModel, listener: Management.OnCompleteListener<*>?) {

        FireStoreWriteManager.insert(
                firestore.collection(CORPS_TABLE)
                        .document(CommonData.getCorpsUid())
                        .collection(GROUP_TABLE)
                        .document(),
                dataModel, DataTypeListener.OnCompleteListener {
            listener?.onComplete(null)
        })
    }


    fun sendTeamDeleteData(dataModel: HolyModel.groupModel.teamModel, listener: Management.OnCompleteListener<*>?) {

        FireStoreWriteManager.delete(
                firestore.collection(CORPS_TABLE)
                        .document(CommonData.getCorpsUid())
                        .collection(TEAM_TABLE)
                        .document(dataModel.uid),
                DataTypeListener.OnCompleteListener {
                    listener?.onComplete(null)
                })
    }

    fun sendTeamModifyData(
            dataModel: HolyModel.groupModel.teamModel,
            listener: Management.OnCompleteListener<*>?) {
        LoggerHelper.e("sendTeamModifyData")
        val map = dataModel.convertMap()
        FireStoreWriteManager.modify(
                firestore.collection(CORPS_TABLE)
                        .document(CommonData.getCorpsUid())
                        .collection(TEAM_TABLE)
                        .document(dataModel.uid),
                map,
                DataTypeListener.OnCompleteListener {
                    listener?.onComplete(null)
                })
    }

    fun sendTeamInsertData(
            dataModel: HolyModel.groupModel.teamModel,
            listener: Management.OnCompleteListener<*>?) {

        LoggerHelper.e("sendTeamInsertData")

        FireStoreWriteManager.insert(
                firestore.collection(CORPS_TABLE)
                        .document(CommonData.getCorpsUid())
                        .collection(TEAM_TABLE)
                        .document(),
                dataModel,
                DataTypeListener.OnCompleteListener {
                    listener?.onComplete(null)
                })
    }

    fun sendAttendModifyData(dataModel: AttendModel, listener: Management.OnCompleteListener<*>?) {
        FireStoreAttendManager.insert(dataModel , DataTypeListener.OnCompleteListener {
            listener?.onComplete(null)
        })
    }

    fun sendCorpsModifyData(dataModel: HolyModel, map: Map<String, Any>, listener: Management.OnCompleteListener<*>?) {
        FirebaseDatabase.getInstance().reference
                .child(CORPS_TABLE)
                .child(dataModel.uid)
                .updateChildren(map)
                .addOnCompleteListener { listener?.onComplete(null) }
    }

    fun sendMemberDeleteData(dataModel: HolyModel.memberModel, listener: Management.OnCompleteListener<*>?) {
        FireStoreMemberManager.delete(dataModel, DataTypeListener.OnCompleteListener { listener?.onComplete(null) })
    }

    fun sendMemberAddMapdata(dataModel: HolyModel.memberModel, listener: Management.OnCompleteListener<*>?) {
        val map = dataModel.convertMap()

        //TODO modify로 변경할수있도록 수정
        FireStoreMemberManager.modify(map, DataTypeListener.OnCompleteListener { listener?.onComplete(null) })
    }

    fun sendMemberInsertData(dataModel: HolyModel.memberModel, listener: Management.OnCompleteListener<*>?) {
        FireStoreMemberManager.insert(dataModel, DataTypeListener.OnCompleteListener { listener?.onComplete(null) })
    }

    /**
     * USER 데이터 베이스에 MAP 데이터를 추가한다.
     *
     * @param uid
     * @param token
     */
    fun sendUserDataAddDataMap(uid: String, key: String, token: String) {
        FireStoreManager.sendUserDataAddDataMap(uid, key, token)
    }

    fun getUserData(uid: String, onCompleteListener: DataTypeListener.OnCompleteListener<UserModel>) {
        val userModel = UserModel()
        userModel.uid = uid

        FireStoreManager.getUserData(
                userModel,
                DataTypeListener.OnCompleteListener {
                    returnUserModel ->
                    onCompleteListener.onComplete(returnUserModel as UserModel?)
                })
    }

    fun getSubAdminList(userType: String, onCompleteListener: DataTypeListener.OnCompleteListener<ArrayList<UserModel>>) {
        FireStoreManager.getSubAdminList(userType,
                DataTypeListener.OnCompleteListener {
                    userModels -> onCompleteListener.onComplete(userModels as ArrayList<UserModel>?) })
    }

    /**
     * memberModel 에 해당하는 유저의 프로필 사진을 서버 스토리지에 저장
     *
     * @param memberModel
     * @param bitmap
     * @param onCompleteListener
     */
    fun setUploadImageSendMemberStorage(memberModel: HolyModel.memberModel,
                                        bitmap: Bitmap,
                                        onCompleteListener: DataReturnListener.OnCompleteListener) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val data = baos.toByteArray()

        LoggerHelper.d("memberModel.uid : " + memberModel.uid)

        FDDatabaseHelper.showProgress(true)
        FirebaseStorage.getInstance()
                .reference
                .child("memberImages")
                .child(memberModel.uid)
                //.putFile(mImageUri)
                .putBytes(data)
                .addOnCompleteListener { task ->
                    task.result!!.storage
                            .downloadUrl.addOnSuccessListener { uri ->
                        FDDatabaseHelper.showProgress(false)
                        memberModel.userPhotoUri = uri.toString()
                        onCompleteListener.onComplete(uri.toString())
                    }
                }
    }

    /**
     * userModel 에 해당 하는 관리자의 프로필 사진을 서버 스토리지에 저장
     *
     * @param userModel
     * @param bitmap
     * @param onCompleteListener
     */
    fun setUploadImageSendUserStorage(userModel: UserModel,
                                      bitmap: Bitmap,
                                      onCompleteListener: DataReturnListener.OnCompleteListener) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val data = baos.toByteArray()
        FDDatabaseHelper.showProgress(true)
        FirebaseStorage.getInstance()
                .reference
                .child("userImages")
                .child(userModel.uid!!)
                //.putFile(mImageUri)
                .putBytes(data)
                .addOnCompleteListener { task ->
                    task.result!!.storage
                            .downloadUrl.addOnSuccessListener { uri ->
                        //LoggerHelper.d(TAG, "onSuccess: uri= " + uri.toString());
                        userModel.userPhotoUri = uri.toString()
                        LoggerHelper.d(userModel.toString())
                        FDDatabaseHelper.showProgress(false)
                        onCompleteListener.onComplete(uri.toString())
                    }
                }
    }

    /**
     * @param :          전체 corps 가지고 온다.
     * @param onFDDListener
     */
    fun getAllCorpsData(
            onFDDListener: FDDatabaseHelper.onFDDCallbackListener) {

        FDDatabaseHelper.showProgress(true)
        FirebaseDatabase.getInstance().reference
                .child(CORPS_TABLE)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        FDDatabaseHelper.showProgress(false)
                        val holyModels = ArrayList<HolyModel>()
                        if (dataSnapshot.childrenCount <= 0) {
                            return
                        }

                        LoggerHelper.d("교회/단체 정보는 " + dataSnapshot.childrenCount + " 개 입니다.")

                        for (item in dataSnapshot.children) {

                            val itemholyModel = item.getValue(HolyModel::class.java)
                            itemholyModel!!.uid = item.key
                            holyModels.add(itemholyModel)
                        }

                        val tempMap = HashMap<Int?, HolyModel>()

                        var c: Int = 0
                        for (elem in holyModels) {
                            tempMap[c] = elem
                            c++
                        }

                        val tempList = ArrayList<HolyModel?>()

                        val tm = TreeMap(tempMap)
                        val iteratorKey = tm.keys.iterator()   //키값 오름차순 정렬(기본)
                        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

                        while (iteratorKey.hasNext()) {
                            val key = iteratorKey.next()
                            tempList.add(tm[key])
                        }

                        CommonData.setHolyModelList(tempList)

                        onFDDListener?.onFromDataComplete(CORPS_DATA_SET, dataSnapshot)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
    }

    /**
     * @param aBoolean
     */
    fun showProgress(aBoolean: Boolean?) {
        //LoggerHelper.e("showProgress")
        try {
            if (aBoolean!!) {
                MaterialDailogUtil.showProgressDialog(true)
            } else {
                MaterialDailogUtil.showProgressDialog(false)
            }
        } catch (e: Exception) {
            LoggerHelper.e(e.message)
        }

    }

    /**
     * 세팅된 그룹, 팀으로 출석 데이터를 가지고 온다.
     *
     * @param onCompleteListener
     */
    fun getAttend(onCompleteListener: SimpleListener.OnCompleteListener) {
        LoggerHelper.d("getAttend")
        if (CommonData.getGroupUid() != null && CommonData.getTeamUid() != null) {
            val attendModel = AttendModel()
            attendModel.timestamp = Common.currentTimestamp()
            attendModel.year = CommonData.getSelectedYear().toString()
            attendModel.month = Common.addZero(CommonData.getSelectedMonth()).toString()
            attendModel.date = Common.addZero(CommonData.getSelectedDay()).toString()
            attendModel.day = CommonData.getSelectedDayOfWeek().toString()
            attendModel.time = CommonData.getSelectedDays().toString()
            attendModel.groupUID = CommonData.getGroupUid()
            attendModel.corpsUID = CommonData.getCorpsUid()
            attendModel.teamUID = CommonData.getTeamUid()
            attendModel.fdate = attendModel.year + "-" + attendModel.month + "-" + attendModel.date + "-" + attendModel.day + "-" + attendModel.time
            FDDatabaseHelper.showProgress(true)

            FireStoreAttendManager.getAttandAllData(attendModel, SimpleListener.OnCompleteListener {
                LoggerHelper.d("getAttandAllData")
                FDDatabaseHelper.showProgress(false)
                onCompleteListener.onComplete()
            })
        }
    }

    fun getAllcorpsMembers(onSimpleListener: SimpleListener.OnCompleteListener) {

        FireStoreMemberManager.getDocData(DataTypeListener.OnCompleteListener { stringmembersHashMap ->
            try {

                LoggerHelper.d("stringmembersHashMap : " + stringmembersHashMap.size)
                CommonData.setMembersMap(stringmembersHashMap)
            } catch (e: Exception) {
                CommonData.setMemberModel(null)
            }

            onSimpleListener.onComplete()
        })
    }

    fun getMyCorps(onSimpleListener: SimpleListener.OnCompleteListener) {
        Log.d(TAG, "getMyCorps: 교회/단체 정보를 가지고 옵니다. ")
        val adminUID = CommonData.getAdminUid()

        LoggerHelper.d("getMyCorps : adminUID : $adminUID")
        getCorpsDataToStore(adminUID, DataTypeListener.OnCompleteListener { queryDocumentSnapshots ->
            CommonData.setCorpsCnt(queryDocumentSnapshots.size())

            LoggerHelper.d("getMyCorps : [" + CommonData.getCorpsCnt() + "] 개의 단체 정보를 가지고 왔습니다.")

            if (CommonData.getCorpsCnt() == 0) {
                CommonData.setHolyModel(null)
                CommonData.setGroupModel(null)
                CommonData.setTeamModel(null)
                CommonData.setMemberModel(null)
                onSimpleListener.onComplete()
                return@OnCompleteListener
            }

            if (CommonData.getCorpsCnt() > 1) {
                LoggerHelper.d("onFromDataComplete: " + "단체의 개수가 비정상입니다.")
                onSimpleListener.onComplete()
                return@OnCompleteListener
            }

            //홀리 모델을 세팅한다.
            for (item in queryDocumentSnapshots.documents) {
                val holyModel = item.toObject(HolyModel::class.java)
                holyModel!!.uid = item.id
                CommonData.setHolyModel(holyModel)
                CommonData.setCorpsUid(item.id)
            }

            LoggerHelper.d("그룹리스트를 갱신중입니다.")
            getGroupDataToStore (DataTypeListener.OnCompleteListener {
                queryDocumentSnapshots ->
                setCurrentGrops(SimpleListener.OnCompleteListener {
                    //onSimpleListener.onComplete()
                })
            })

            getAllcorpsMembers (SimpleListener.OnCompleteListener {
                onSimpleListener.onComplete()
            })
        })
    }

    fun setCurrentTeamMaps() {
        try {
            val teams = SortMapUtil.getSortTeamList() as ArrayList<HolyModel.groupModel.teamModel>
            //LoggerHelper.d("teams.size : " + teams.size());

            if(teams.size == 0){
                CommonData.setTeamModel(null)
                CommonData.setMemberModel(null)
            }
            for (eleTeam in teams) {
                //LoggerHelper.d("eleTeam : " + eleTeam.convertMap());
                if (CommonData.getTeamModel() != null) {
                    //LoggerHelper.d("teams.size():" + teams.size() + " // CommonData.getTeamUid : " + CommonData.getTeamUid() + " // " +  eleTeam.uid);
                    if (CommonData.getTeamUid() == eleTeam.uid) {
                        CommonData.setTeamModel(eleTeam)
                        LoggerHelper.d("팀 설정 정보가 갱신됩니다.", eleTeam.convertMap())
                    }
                } else {
                    //LoggerHelper.d("설정된 팀 내용이 없습니다.");
                }
            }
        } catch (e: Exception) {
            //LoggerHelper.d("설정된 팀 내용이 없습니다. e");
        }

    }

    fun setCurrentGrops(onSimpleListener: SimpleListener.OnCompleteListener): Boolean {
        try {
            val groups = SortMapUtil.getSortGroupList(CommonData.getHolyModel().group) as ArrayList<HolyModel.groupModel>
            LoggerHelper.d("groups.size : " + groups.size)

            if(groups.size == 0){
                CommonData.setGroupModel(null)
                CommonData.setTeamModel(null)
                CommonData.setMemberModel(null)
                onSimpleListener.onComplete()
                showProgress(false)
                return true
            }

            for (eleGroup in groups) {
                LoggerHelper.d("CommonData.getGroupUid() : " + CommonData.getGroupUid())
                if (CommonData.getGroupModel() != null) {
                    LoggerHelper.d("CommonData.getGroupUid : " + CommonData.getGroupUid() + " // " + eleGroup.uid)
                    if (CommonData.getGroupUid() == eleGroup.uid) {
                        CommonData.setGroupModel(eleGroup)
                        LoggerHelper.d("그룹 설정 정보가 갱신됩니다.", eleGroup.convertMap())
                    }
                } else {
                    //LoggerHelper.d("설정된 그룹 내용이 없습니다.");
                    CommonData.setGroupModel(null)
                    CommonData.setTeamModel(null)
                    CommonData.setMemberModel(null)
                    onSimpleListener.onComplete()
                    showProgress(false)
                    return true
                }
            }

            getTeamDataToStore (DataTypeListener.OnCompleteListener {
                queryDocumentSnapshots ->
                setCurrentTeamMaps()
            })

        } catch (e: Exception) {
            CommonData.setGroupModel(null)
            CommonData.setTeamModel(null)
            CommonData.setMemberModel(null)
            //LoggerHelper.d("설정된 그룹 내용이 없습니다. e");
            onSimpleListener.onComplete()
            showProgress(false)
            return true
        }

        return false
    }

    fun getGroupNameList(groupMap: Map<String, HolyModel.groupModel>): ArrayList<String> {
        val tempList = ArrayList<String>()

        //그룹Map 에서 현재 설정된 "그룹"의 현재 날짜의 데이터를 commonData에 저장한다.
        for ((_, value) in groupMap) {
            tempList.add(value.name)
        }

        return tempList
    }

    fun getTeamNameList(teamMap: Map<String, HolyModel.groupModel.teamModel>): ArrayList<String> {
        val tempList = ArrayList<String>()

        val tm = TreeMap(teamMap)
        val iteratorKey = tm.keys.iterator()   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

        while (iteratorKey.hasNext()) {
            val key = iteratorKey.next()
            tempList.add(SortMapUtil.getInteger(tm[key]!!.name).toString())
        }
        return tempList
    }

    fun getGroupModelNameFromMap(groupMap: Map<String, HolyModel.groupModel>, name: String): HolyModel.groupModel {
        var groupModel = HolyModel.groupModel()

        //그룹Map 에서 현재 설정된 "그룹"의 현재 날짜의 데이터를 commonData에 저장한다.
        for (( key, value) in groupMap) {
            if (value.name == name) {
                groupModel = value
            }
        }

        return groupModel
    }

    fun getGroupModelFromMap(groupMap: Map<String, HolyModel.groupModel>, name: String): HolyModel.groupModel {
        var groupModel = HolyModel.groupModel()

        //그룹Map 에서 현재 설정된 "그룹"의 현재 날짜의 데이터를 commonData에 저장한다.
        for (( key, value) in groupMap) {
            if (key == name) {
                groupModel = value
            }
        }

        return groupModel
    }

    fun getTeamModelFromMap(teamMap: Map<String, HolyModel.groupModel.teamModel>, name: String): HolyModel.groupModel.teamModel? {
        var tempModel = HolyModel.groupModel.teamModel()

        //그룹Map 에서 현재 설정된 "그룹"의 현재 날짜의 데이터를 commonData에 저장한다.
        for ((_, value) in teamMap) {

            LoggerHelper.d("FDDatabaseHelper getTeamModelFromMap eleDate.getValue().uid : " + value.uid + " / name : " + name)
            if (value.name == name) {
                tempModel = value
                LoggerHelper.d("tempModel$tempModel")
                return tempModel
            }
        }

        return null
    }

    fun getAttendDayData(
            dataModel: AttendModel,
            onListener: DataTypeListener.OnCompleteListener<HashMap<String, String>>) {

        FDDatabaseHelper.showProgress(true)

        LoggerHelper.d("getAttendDayData: [ " + dataModel.date + "/" + dataModel.day + "/" + dataModel.time + " ] 출석 정보를 가지고 옵니다. ")

        FireStoreAttendManager.getAttendDayData(dataModel, DataTypeListener.OnCompleteListener {
            stringStringHashMap ->
            FDDatabaseHelper.showProgress(false)
            onListener.onComplete(stringStringHashMap as HashMap<String, String>?)
        })
    }

    /**
     * 멤버 데이터를 비교한다.
     *
     * @param cMember
     * @return
     */
    fun getCompareData(
            cMember: HolyModel.memberModel): Boolean {

        //if (CommonData.getHolyModel().memberModel == null) {
        if (CommonData.getMembersMap() == null) {
            return false
        }
        //for (Map.Entry<String, HolyModel.memberModel> elem : CommonData.getHolyModel().memberModel.entrySet()) {
        for ((_, tempMembers) in CommonData.getMembersMap()) {
            if (Common.trim(tempMembers.name) == Common.trim(cMember.name)) {
                return true
            }
        }
        return false
    }

    interface onFDDCallbackListener {
        fun onFromDataComplete(DataCode: Int, dataSnapshot: DataSnapshot?)
    }

    fun setWriteBatch(dataModel: AttendModel, FirebaseFirestore: FirebaseFirestore): WriteBatch {
        val gson = Gson()

        val batch = FirebaseFirestore.batch()
        val attendRef = FirebaseFirestore.collection(FDDatabaseHelper.ATTEND_TABLE)
                .document(FDDatabaseHelper.ATTEND)
                .collection(FDDatabaseHelper.CORPS_TABLE)

        val holyModel = HolyModel()
        holyModel.name = CommonData.getHolyModel().name
        holyModel.adminUid = CommonData.getHolyModel().adminUid
        holyModel.address = CommonData.getHolyModel().address
        holyModel.adminEmail = CommonData.getHolyModel().adminEmail
        holyModel.addressDetail = CommonData.getHolyModel().addressDetail
        holyModel.owner = CommonData.getHolyModel().owner
        holyModel.phone = CommonData.getHolyModel().phone
        holyModel.uid = CommonData.getHolyModel().uid

        val corpRef = attendRef.document(dataModel.corpsUID)
        batch.set(corpRef, holyModel)

        val groupRef = attendRef.document(dataModel.corpsUID)
                .collection(FDDatabaseHelper.GROUP_TABLE)
                .document(dataModel.groupUID)

        val group = HolyModel.groupModel()
        group.name = CommonData.getGroupModel().name
        group.etc = CommonData.getGroupModel().etc
        group.uid = CommonData.getGroupModel().uid
        group.leader = CommonData.getGroupModel().leader

        batch.set(groupRef, group)

        val dateRef = attendRef.document(dataModel.corpsUID)
                .collection(FDDatabaseHelper.GROUP_TABLE)
                .document(dataModel.groupUID)
                .collection(FDDatabaseHelper.ATTEND_DATE_TABLE)
                .document(dataModel.fdate)

        val aModel = AttendModel()
        aModel.year = CommonData.getSelectedYear().toString()
        aModel.month = CommonData.getSelectedMonth().toString()
        aModel.date = CommonData.getSelectedDay().toString()
        aModel.day = CommonData.getSelectedDayOfWeek().toString()
        aModel.time = CommonData.getSelectedDays().toString()
        aModel.timestamp = Common.currentTimestamp().toString()

        batch.set(dateRef, aModel)

        if (dataModel.attend == null) dataModel.attend = "false"

        val attendDoc = attendRef
                .document(dataModel.corpsUID)
                .collection(FDDatabaseHelper.GROUP_TABLE)
                .document(dataModel.groupUID)
                .collection(FDDatabaseHelper.ATTEND_DATE_TABLE)
                .document(dataModel.fdate)
                .collection(FDDatabaseHelper.ATTEND_MEMBER_TABLE)
                .document(dataModel.memberUID)

        batch.set(attendDoc, dataModel)
        return batch
    }
}