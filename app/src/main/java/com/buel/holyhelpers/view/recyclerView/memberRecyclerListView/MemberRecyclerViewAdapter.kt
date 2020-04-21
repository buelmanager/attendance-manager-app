package com.buel.holyhelpers.view.recyclerView.memberRecyclerListView

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.AdminMode
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.adminMode
import com.buel.holyhelpers.data.CommonData.getGroupName
import com.buel.holyhelpers.data.CommonData.getTeamName
import com.buel.holyhelpers.data.CommonData.isTutoMode
import com.buel.holyhelpers.data.CommonData.memberModel
import com.buel.holyhelpers.data.CommonData.memberUid
import com.buel.holyhelpers.data.CommonData.selectedMember
import com.buel.holyhelpers.data.CommonData.setAdminMode
import com.buel.holyhelpers.data.CommonData.viewMode
import com.buel.holyhelpers.data.ViewMode
import com.buel.holyhelpers.management.Management
import com.buel.holyhelpers.management.MemberManager
import com.buel.holyhelpers.management.PointManager
import com.buel.holyhelpers.model.AttendModel
import com.buel.holyhelpers.model.HolyModel.memberModel
import com.buel.holyhelpers.utils.AppUtil
import com.buel.holyhelpers.view.activity.MemberManagerViewActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil.Companion.simpleDoneDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleInputDoneDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleYesNoDialog
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.orhanobut.logger.LoggerHelper
import com.ramotion.foldingcell.FoldingCell
import java.util.*

/**
 * Created by blue7 on 2018-06-07.
 */
class MemberRecyclerViewAdapter(
        itemArrayList: List<memberModel>?,
        context: Context?,
        var memberRecyclerViewListener: MemberRecyclerViewListener.OnCompleteListener) :
        RecyclerView.Adapter<MemberRecyclerViewHolder>() {

    var attendMap: MutableMap<String, AttendModel>? = null

    /*fun getAttendMap(): Map<String, AttendModel>? {
        return attendMap
    }
    fun setAttendMap(attendMap: MutableMap<String, AttendModel>?) {
        this.attendMap = attendMap
    }*/

    private var mContext: Context? = null
    private val unfoldedIndexes = HashSet<Int>()
    fun registerFold(position: Int) {
        unfoldedIndexes.remove(position)
    }

    fun registerUnfold(position: Int) {
        unfoldedIndexes.add(position)
    }

    // simple methods for register cell state changes
    fun registerToggle(position: Int) {
        if (unfoldedIndexes.contains(position)) registerFold(position) else registerUnfold(position)
    }



    var itemArrayList: List<memberModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberRecyclerViewHolder {
        LoggerHelper.d("onCreateViewHolder")
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell, parent, false)
        return MemberRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberRecyclerViewHolder, position: Int) { //LoggerHelper.d("onBindViewHolder");
        setUI(holder, position)
        setMemberViewMode(holder, position)
    }

    /**
     * add event or set ui
     *
     * @param viewHolder
     * @param position
     */
    private fun setUI(viewHolder: MemberRecyclerViewHolder, position: Int) {
        val members = itemArrayList!![position]
        viewHolder.fd_cell_main_ll.visibility = View.GONE
        viewHolder.itemView.setOnClickListener { v ->
            // toggle clicked cell state
            (v as FoldingCell).toggle(false)
            // register in adapter that state for selected cell is toggled
            registerToggle(position)
        }
        if (members.userPhotoUri == null) {
            try {
                Glide.with(viewHolder.itemView.context)
                        .load(R.drawable.ic_account)
                        .apply(RequestOptions().fitCenter().circleCrop())
                        .into(viewHolder.content_avatar)
                Glide.with(viewHolder.itemView.context)
                        .load(R.drawable.ic_account)
                        .apply(RequestOptions().fitCenter().circleCrop())
                        .into(viewHolder.image_view)
            } catch (e: Exception) {
                LoggerHelper.e(e.message)
            }
        } else {
            Glide.with(viewHolder.itemView.context)
                    .load(members.userPhotoUri)
                    .apply(RequestOptions().fitCenter().circleCrop())
                    .into(viewHolder.image_view)
            Glide.with(viewHolder.itemView.context)
                    .load(members.userPhotoUri)
                    .apply(RequestOptions().fitCenter().circleCrop())
                    .into(viewHolder.content_avatar)
        }
        viewHolder.price.text = ""
        viewHolder.date.text = getGroupName(members.groupUID)
        viewHolder.time.text = getTeamName(members.teamUID)
        viewHolder.fromAddress.text = members.name
        viewHolder.toAddress.text = members.phone
        viewHolder.requestsCount.text = members.birth
        viewHolder.pledgePrice.text = members.gender
        viewHolder.titleWeight.text = members.isNew
        viewHolder.content_name_view.text = members.name
        viewHolder.head_image_right_text.text = getTeamName(members.teamUID)
        viewHolder.head_image_center_text.text = getGroupName(members.groupUID)
        viewHolder.head_image_left_text.text = members.corpsName
        viewHolder.content_from_address_1.text = members.address
        viewHolder.content_from_address_2.text = members.town
        viewHolder.content_delivery_time3.text = members.gender
        viewHolder.content_delivery_date3.text = ""
        viewHolder.content_to_address_1.text = members.phone
        //viewHolder.content_to_address_2.setText("클릭하면 전화로 연결됩니다.");
//viewHolder.content_delivery_date_badge2.setText(memberModel.leader);
        viewHolder.content_delivery_time2.text = members.isNew
        viewHolder.content_delivery_date2.text = ""
        viewHolder.content_deadline_time4.text = members.memberRegistDate
        //viewHolder.content_delivery_date_badge2.setText(memberModel.isNew);
        viewHolder.content_delivery_time1.text = members.leader
        viewHolder.content_delivery_date1.text = ""
        viewHolder.content_delivery_time.text = members.birth
        viewHolder.content_deadline_time.text = members.isExecutives
        viewHolder.content_name_view2.text = members.position
        viewHolder.content_delivery_date.text = ""
        viewHolder.content_avatar_title.text = members.isExecutives
        viewHolder.modify_iv.setOnClickListener {
            setAdminMode(AdminMode.MODIFY)
            selectedMember = members
            val intent = Intent(viewHolder.itemView.context, MemberManagerViewActivity::class.java)
            //goStartAcitivity(intent);
            LoggerHelper.d("goStartAcitivity", intent)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            viewHolder.itemView.context.startActivity(intent)
        }
        viewHolder.delete_btn.setOnClickListener { v ->
            simpleYesNoDialog(v.context, "삭제하시겠습니까?", object : OnDialogSelectListner {
                override fun onSelect(s: String) {
                    val memberManager = MemberManager()
                    memberManager.delete(members, object : Management.OnCompleteListener<Any?> {
                        override fun onComplete(data: Any?) {
                            LoggerHelper.d("아이디를 삭제하였습니다. 교적부")
                            memberRecyclerViewListener.onComplete(members, "", v)
                        }
                    })
                }
            })
        }
        viewHolder.delete_btn.visibility = View.INVISIBLE
        viewHolder.modefy_btn.visibility = View.INVISIBLE
        viewHolder.delete_iv.setOnClickListener { v: View ->
            simpleYesNoDialog(v.context, "삭제하시겠습니까?", object : OnDialogSelectListner {
                override fun onSelect(s: String) {
                    memberRecyclerViewListener.onComplete(members, "", v)
                }
            })
        }
        viewHolder.modefy_btn.setOnClickListener {
            setAdminMode(AdminMode.MODIFY)
            selectedMember = members
            val intent = Intent(viewHolder.itemView.context, MemberManagerViewActivity::class.java)
            //goStartAcitivity(intent);
            LoggerHelper.d("goStartAcitivity", intent)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            viewHolder.itemView.context.startActivity(intent)
        }
        viewHolder.content_request_btn.setOnClickListener { v: View? ->
            Toast.makeText(viewHolder.itemView.context,
                    "개인통계현황(출석/전도/정착률) 다음 시즌 준비중입니다.", Toast.LENGTH_SHORT).show()
        }
        AppUtil.setBackColor(viewHolder.itemView.context, viewHolder.modify_iv, R.color.white)
        AppUtil.setBackColor(viewHolder.itemView.context, viewHolder.delete_iv, R.color.material_500_red)
        viewHolder.call_rl.setOnClickListener { v ->
            simpleYesNoDialog(v.context, "전화를 하시겠습니까?", object : OnDialogSelectListner {
                override fun onSelect(s: String) {
                    val mTell = viewHolder.content_to_address_1.text.toString().replace("-", "")
                    //AppUtil.sendCall(MemberManagerViewActivity.this, strTell, true);
                    Common.sendDirectCall(mTell, v.context as Activity)
                }
            })
        }
        //switchicon
        viewHolder.button1.setOnClickListener { view -> onDeleteClicked(view, viewHolder, members, position) }
        //삭제버튼
/*holder.btn_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                    onDeleteClicked(v, holder, memberModel, position);
                } else {
                    MaterialDailogUtil.simpleYesNoDialog(v.getContext(),"삭제하시겠습니까?" , new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {
                            onDeleteClicked(v, holder, memberModel, position);
                        }
                    });
                }
            }
        });*/
//선택버튼
        viewHolder.btn_item_select.setOnClickListener { v -> onSelectClicked(v, position, viewHolder, members) }
        //몸통클릭
/*holder.recyclerViewItemMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onViewItemBtnClickedListener(v, position);
                return false;
            }
        });*/
    }

    private fun onViewItemBtnClickedListener(v: View, position: Int) {
        LoggerHelper.d("onViewItemBtnClickedListener")
        //if (CommonData.getViewMode() == ViewMode.ADMIN) {
        val memberModel = itemArrayList!![position]
        modify(memberModel, v)
        //}
    }

    /**
     * set view mode
     *
     * @param holder
     * @param position
     */
    @SuppressLint("ResourceAsColor", "NewApi")
    private fun setMemberViewMode(holder: MemberRecyclerViewHolder, position: Int) { //LoggerHelper.d("setMemberViewMode", "CommonData.getViewMode() : " + CommonData.getViewMode());
//holder.btn_item_select.setText("");
//holder.btn_item_delete.setText("");
//holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
        holder.btn_item_select.visibility = View.INVISIBLE
        holder.btn_item_delete.visibility = View.INVISIBLE
        holder.button1_text.visibility = View.INVISIBLE
        holder.btn_item_select_text.visibility = View.INVISIBLE
        val members = itemArrayList!![position]
        if (viewMode == ViewMode.ATTENDANCE) { //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_outline_24dp);
            holder.btn_item_select.setBackgroundResource(R.drawable.ic_sms)
            //AppUtil.setBackColor(mContext, holder.btn_item_select, R.color.colorAccent);
//holder.btn_item_select.setVisibility(View.INVISIBLE);
//AppUtil.setBackColor(mContext, holder.btn_item_delete, R.color.darkGray);
            if (attendMap != null) {
                if (attendMap!![members.name] != null) {
                    if (attendMap!![members.name]!!.attend == "true") { //AppUtil.setBackColor(mContext, holder.btn_item_delete, R.color.blue400);
                        holder.switchIcon1.isIconEnabled = true
                        holder.button1_text.text = "출석"
                        holder.back_color_rl.setBackgroundResource(R.color.fd_cell_main_color)
                        holder.btn_item_select.visibility = View.INVISIBLE
                        holder.btn_item_select_text.visibility = View.INVISIBLE
                        //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_24dp);
                    } else {
                        holder.button1_text.text = "결석"
                        holder.switchIcon1.isIconEnabled = false
                        holder.back_color_rl.setBackgroundResource(R.color.darkGray)
                        holder.btn_item_select.visibility = View.VISIBLE
                        holder.btn_item_select_text.visibility = View.VISIBLE
                        // holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_outline_24dp);
// AppUtil.setBackColor(mContext, holder.btn_item_delete, R.color.darkGray);
                    }
                } else {
                    holder.back_color_rl.setBackgroundResource(R.color.darkGray)
                    holder.switchIcon1.isIconEnabled = false
                    holder.button1_text.text = "결석"
                    holder.btn_item_select.visibility = View.VISIBLE
                    holder.btn_item_select_text.visibility = View.VISIBLE
                    //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_outline_24dp);
                }
            }
            holder.button1.visibility = View.VISIBLE
            holder.button1_text.visibility = View.VISIBLE
            holder.attend_back_veiw.visibility = View.VISIBLE
        } else if (viewMode == ViewMode.ADMIN) { //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_delete_24dp);
            if (memberModel != null) {
                if (members.name == memberModel.name) { // holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
                } else { //holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
                }
            }
        } else if (viewMode == ViewMode.SEARCH_MEMBER) { //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_delete_24dp);
        }
    }

    /**
     * SELECT BUTTON CLICKED
     * 선택버튼을 클릭하였을 때 ViewMode로 분기처리된다.
     *
     * @param v
     * @param position
     * @param holder
     * @param members
     */
    @SuppressLint("LongLogTag")
    private fun onSelectClicked(v: View, position: Int, holder: MemberRecyclerViewHolder, members: memberModel) {
        if (viewMode == ViewMode.ATTENDANCE) {
            LoggerHelper.d("결석사유!!")
            simpleInputDoneDialog(mContext!!, "결석 사유! ", "간단한 결석사유를 적어주세요.", object : OnDialogSelectListner {
                override fun onSelect(s: String) {
                    if (s == null || s == "") return
                    members.noAttendReason = s
                    simpleDoneDialog(mContext!!, "결석사유가 등록되었습니다.", s, null)
                    LoggerHelper.d("memberModel.noAttendReason : " + members.noAttendReason)
                    members.attend = "false"
                    val attendModel = AttendModel()
                    attendModel.memberName = members.name
                    attendModel.attend = members.attend
                    attendModel.groupUID = members.groupUID
                    attendModel.teamUID = members.teamUID
                    attendModel.corpsUID = members.corpsUID
                    attendModel.isExecutives = members.isExecutives
                    attendModel.noAttendReason = members.noAttendReason
                    attendModel.isNew = members.isNew
                    attendModel.memberName = members.name
                    attendMap!![members.name] = attendModel
                    //itemArrayList.set(position, members);
                    memberRecyclerViewListener.onComplete(members, "false", v)
                }
            })
        } else if (viewMode == ViewMode.ADMIN || adminMode == AdminMode.MODIFY) {
            modify(members, v)
        }
        notifyItemChanged(position)
    }

    /**
     * DELETE BUTTON CLICKED
     *
     * @param v
     * @param holder
     * @param members
     */
    private fun onDeleteClicked(v: View, holder: MemberRecyclerViewHolder, members: memberModel, position: Int) {
        if (viewMode == ViewMode.ATTENDANCE) {
            if (isTutoMode) {
                isTutoMode
                simpleDoneDialog(mContext!!,
                        "축하합니다. 튜토리얼이 완료되었습니다.", object : OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        PointManager.setPlusPoint(CommonData.personalJoinPoint)
                    }
                })
            }
            members.noAttendReason = ""
            try {
                if (attendMap!![members.name]!!.attend == "true") {
                    members.attend = "false"
                } else {
                    members.attend = "true"
                }
            } catch (e: Exception) {
                members.attend = "true"
            }
            val attendModel = AttendModel()
            attendModel.memberName = members.name
            attendModel.attend = members.attend
            attendModel.groupUID = members.groupUID
            attendModel.teamUID = members.teamUID
            attendModel.corpsUID = members.corpsUID
            attendModel.isExecutives = members.isExecutives
            attendModel.noAttendReason = members.noAttendReason
            attendModel.isNew = members.isNew
            attendModel.memberName = members.name
            attendMap!![members.name] = attendModel
            //itemArrayList.set(position, members);
            memberRecyclerViewListener.onComplete(members, members.attend, v)
        } else {
            delete(v, members)
        }
        notifyItemChanged(position)
    }

    private fun delete(v: View, members: memberModel) {
        memberRecyclerViewListener.onComplete(members, "delete", v)
    }

    private fun select(memberModel: memberModel) {
        CommonData.memberModel = memberModel
        memberUid = memberModel.uid
    }

    private fun modify(memberModel: memberModel, v: View) {
        LoggerHelper.d("onViewItemBtnClickedListener")
        memberRecyclerViewListener.onComplete(memberModel, "modify", v)
    }

    override fun getItemCount(): Int {
        return itemArrayList!!.size
    }

    init {
        if (itemArrayList != null) {
            this.itemArrayList = itemArrayList
        } else {
            this.itemArrayList = ArrayList()
        }
        mContext = context
    }
}