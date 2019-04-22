package com.buel.holyhelper.view.recyclerView.memberRecyclerListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.management.Management;
import com.buel.holyhelper.management.MemberManager;
import com.buel.holyhelper.management.PointManager;
import com.buel.holyhelper.model.AttendModel;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.AppUtil;
import com.buel.holyhelper.view.activity.MemberManagerViewActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by blue7 on 2018-06-07.
 */

public class MemberRecyclerViewAdapter
        extends RecyclerView.Adapter<MemberRecyclerViewHolder> {

    Map<String, AttendModel> attendMap;

    public Map<String, AttendModel> getAttendMap() {
        return attendMap;
    }

    private Context mContext = null;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void setAttendMap(Map<String, AttendModel> attendMap) {
        this.attendMap = attendMap;
    }

    List<HolyModel.memberModel> itemArrayList;
    MemberRecyclerViewListener.OnCompleteListener memberRecyclerViewListener;

    public MemberRecyclerViewAdapter(List<HolyModel.memberModel> itemArrayList, Context context, MemberRecyclerViewListener.OnCompleteListener memberRecyclerViewListener) {
        this.memberRecyclerViewListener = memberRecyclerViewListener;
        this.itemArrayList = itemArrayList;
        mContext = context;
    }

    public List<HolyModel.memberModel> getItemArrayList() {
        return this.itemArrayList;
    }

    public void setItemArrayList(List<HolyModel.memberModel> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public MemberRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LoggerHelper.d("onCreateViewHolder");
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new MemberRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberRecyclerViewHolder holder, final int position) {
        LoggerHelper.d("onBindViewHolder");
        setUI(holder, position);
        setMemberViewMode(holder, position);
    }

    /**
     * add event or set ui
     *
     * @param viewHolder
     * @param position
     */
    private void setUI(@NonNull MemberRecyclerViewHolder viewHolder, final int position) {
        final HolyModel.memberModel members = itemArrayList.get(position);

        viewHolder.fd_cell_main_ll.setVisibility(View.GONE);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // toggle clicked cell state
                ((FoldingCell) v).toggle(false);
                // register in adapter that state for selected cell is toggled
                registerToggle(position);
            }
        });

        if (members.userPhotoUri == null) {
            try {
                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.ic_account)
                        .apply(new RequestOptions().fitCenter().circleCrop())
                        .into(viewHolder.content_avatar);

                Glide.with(viewHolder.itemView.getContext())
                        .load(R.drawable.ic_account)
                        .apply(new RequestOptions().fitCenter().circleCrop())
                        .into(viewHolder.image_view);

            } catch (Exception e) {
                LoggerHelper.e(e.getMessage());
            }
        } else {
            Glide.with(viewHolder.itemView.getContext())
                    .load(members.userPhotoUri)
                    .apply(new RequestOptions().fitCenter().circleCrop())
                    .into(viewHolder.image_view);

            Glide.with(viewHolder.itemView.getContext())
                    .load(members.userPhotoUri)
                    .apply(new RequestOptions().fitCenter().circleCrop())
                    .into(viewHolder.content_avatar);
        }

        viewHolder.price.setText("");
        viewHolder.date.setText(CommonData.getGroupName(members.groupUID));
        viewHolder.time.setText(CommonData.getTeamName(members.teamUID));
        viewHolder.fromAddress.setText(members.name);
        viewHolder.toAddress.setText(members.phone);
        viewHolder.requestsCount.setText(members.birth);
        viewHolder.pledgePrice.setText(members.gender);
        viewHolder.titleWeight.setText(members.isNew);

        viewHolder.content_name_view.setText(members.name);
        viewHolder.head_image_right_text.setText(CommonData.getTeamName(members.teamUID));
        viewHolder.head_image_center_text.setText(CommonData.getGroupName(members.groupUID));
        viewHolder.head_image_left_text.setText(members.corpsName);


        viewHolder.content_from_address_1.setText(members.address);
        viewHolder.content_from_address_2.setText(members.town);
        viewHolder.content_delivery_time3.setText(members.gender);
        viewHolder.content_delivery_date3.setText("");

        viewHolder.content_to_address_1.setText(members.phone);
        //viewHolder.content_to_address_2.setText("클릭하면 전화로 연결됩니다.");
        //viewHolder.content_delivery_date_badge2.setText(memberModel.leader);

        viewHolder.content_delivery_time2.setText(members.isNew);
        viewHolder.content_delivery_date2.setText("");
        viewHolder.content_deadline_time4.setText(members.memberRegistDate);
        //viewHolder.content_delivery_date_badge2.setText(memberModel.isNew);
        viewHolder.content_delivery_time1.setText(members.leader);
        viewHolder.content_delivery_date1.setText("");
        viewHolder.content_delivery_time.setText(members.birth);

        viewHolder.content_deadline_time.setText(members.isExecutives);
        viewHolder.content_name_view2.setText(members.position);
        viewHolder.content_delivery_date.setText("");
        viewHolder.content_avatar_title.setText(members.isExecutives);
        viewHolder.modify_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.setAdminMode(AdminMode.MODIFY);
                CommonData.setSelectedMember(members);
                Intent intent = new Intent(viewHolder.itemView.getContext(), MemberManagerViewActivity.class);
                //goStartAcitivity(intent);
                LoggerHelper.d("goStartAcitivity", intent);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

        viewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDailogUtil.Companion.simpleYesNoDialog(v.getContext(), "삭제하시겠습니까?", new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {
                        MemberManager memberManager = new MemberManager();
                        memberManager.delete(members, new Management.OnCompleteListener() {
                            @Override
                            public void onComplete(Object data) {
                                LoggerHelper.d("아이디를 삭제하였습니다. 교적부");
                                memberRecyclerViewListener.onComplete(members, null, v);
                            }
                        });
                    }
                });
            }
        });

        viewHolder.delete_btn.setVisibility(View.INVISIBLE);
        viewHolder.modefy_btn.setVisibility(View.INVISIBLE);
        viewHolder.delete_iv.setOnClickListener(v -> MaterialDailogUtil.Companion.simpleYesNoDialog(v.getContext(), "삭제하시겠습니까?", s -> {
            MemberManager memberManager = new MemberManager();
            memberManager.delete(members, data -> LoggerHelper.d("아이디를 삭제하였습니다. 교적부"));
            memberRecyclerViewListener.onComplete(members, null, v);
        }));

        viewHolder.modefy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.setAdminMode(AdminMode.MODIFY);
                CommonData.setSelectedMember(members);
                Intent intent = new Intent(viewHolder.itemView.getContext(), MemberManagerViewActivity.class);
                //goStartAcitivity(intent);
                LoggerHelper.d("goStartAcitivity", intent);

                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });
        viewHolder.content_request_btn.setOnClickListener(
                v -> Toast.makeText(viewHolder.itemView.getContext(),
                        "개인통계현황(출석/전도/정착률) 다음 시즌 준비중입니다.", Toast.LENGTH_SHORT).show());


        AppUtil.setBackColor(viewHolder.itemView.getContext(), viewHolder.modify_iv, R.color.white);
        AppUtil.setBackColor(viewHolder.itemView.getContext(), viewHolder.delete_iv, R.color.material_500_red);

        viewHolder.call_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MaterialDailogUtil.Companion.simpleYesNoDialog(v.getContext(), "전화를 하시겠습니까?", new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {
                        String mTell = viewHolder.content_to_address_1.getText().toString().replace("-", "");
                        //AppUtil.sendCall(MemberManagerViewActivity.this, strTell, true);
                        Common.sendDirectCall(mTell, (Activity) v.getContext());
                    }
                });
            }
        });


        //switchicon
        viewHolder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClicked(view, viewHolder, members, position);
            }
        });

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
        viewHolder.btn_item_select.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(final View v) {
                onSelectClicked(v, position, viewHolder, members);
            }
        });

        //몸통클릭
        /*holder.recyclerViewItemMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onViewItemBtnClickedListener(v, position);
                return false;
            }
        });*/

    }

    private void onViewItemBtnClickedListener(View v, int position) {

        LoggerHelper.d("onViewItemBtnClickedListener");
        //if (CommonData.getViewMode() == ViewMode.ADMIN) {
        HolyModel.memberModel memberModel = itemArrayList.get(position);
        modify(memberModel, v);

        //}
    }

    /**
     * set view mode
     *
     * @param holder
     * @param position
     */
    @SuppressLint({"ResourceAsColor", "NewApi"})
    private void setMemberViewMode(@NonNull MemberRecyclerViewHolder holder, int position) {
        LoggerHelper.d("setMemberViewMode", "CommonData.getViewMode() : " + CommonData.getViewMode());

        //holder.btn_item_select.setText("");
        //holder.btn_item_delete.setText("");
        //holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
        holder.btn_item_select.setVisibility(View.INVISIBLE);
        holder.btn_item_delete.setVisibility(View.INVISIBLE);
        holder.button1_text.setVisibility(View.INVISIBLE);
        holder.btn_item_select_text.setVisibility(View.INVISIBLE);

        final HolyModel.memberModel members = itemArrayList.get(position);
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {

            //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_outline_24dp);
            holder.btn_item_select.setBackgroundResource(R.drawable.ic_sms);
            //AppUtil.setBackColor(mContext, holder.btn_item_select, R.color.colorAccent);
            //holder.btn_item_select.setVisibility(View.INVISIBLE);
            //AppUtil.setBackColor(mContext, holder.btn_item_delete, R.color.darkGray);
            if (attendMap != null) {
                if (attendMap.get(members.name) != null) {
                    if (attendMap.get(members.name).attend.equals("true")) {
                        //AppUtil.setBackColor(mContext, holder.btn_item_delete, R.color.blue400);
                        holder.switchIcon1.setIconEnabled(true);

                        holder.button1_text.setText("출석");

                        holder.back_color_rl.setBackgroundResource(R.color.fd_cell_main_color);
                        holder.btn_item_select.setVisibility(View.INVISIBLE);
                        holder.btn_item_select_text.setVisibility(View.INVISIBLE);
                        //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_24dp);
                    } else {
                        holder.button1_text.setText("결석");
                        holder.switchIcon1.setIconEnabled(false);
                        holder.back_color_rl.setBackgroundResource(R.color.darkGray);
                        holder.btn_item_select.setVisibility(View.VISIBLE);
                        holder.btn_item_select_text.setVisibility(View.VISIBLE);
                       // holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_outline_24dp);
                       // AppUtil.setBackColor(mContext, holder.btn_item_delete, R.color.darkGray);
                    }
                } else {
                    holder.back_color_rl.setBackgroundResource(R.color.darkGray);
                    holder.switchIcon1.setIconEnabled(false);
                    holder.button1_text.setText("결석");
                    holder.btn_item_select.setVisibility(View.VISIBLE);
                    holder.btn_item_select_text.setVisibility(View.VISIBLE);
                    //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_check_box_outline_24dp);
                }
            }
            holder.button1.setVisibility(View.VISIBLE);
            holder.button1_text.setVisibility(View.VISIBLE);
            holder.attend_back_veiw.setVisibility(View.VISIBLE);
        } else if (CommonData.getViewMode() == ViewMode.ADMIN) {
            //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_delete_24dp);
            if (CommonData.getMemberModel() != null) {
                if (members.name.equals(CommonData.getMemberModel().name)) {
                    // holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
                } else {
                    //holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
                }
            }
        } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            //holder.btn_item_delete.setBackgroundResource(R.drawable.ic_delete_24dp);
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
    private void onSelectClicked(View v, int position, @NonNull MemberRecyclerViewHolder holder, HolyModel.memberModel members) {
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {

            LoggerHelper.d("결석사유!!");
            MaterialDailogUtil.Companion.simpleInputDoneDialog(mContext, "결석 사유! ", "간단한 결석사유를 적어주세요.", new MaterialDailogUtil.OnDialogSelectListner() {
                @Override
                public void onSelect(String s) {
                    if (s == null || s.equals("")) return;
                    members.noAttendReason = s;

                    MaterialDailogUtil.Companion.simpleDoneDialog(mContext, "결석사유가 등록되었습니다.", s, null);
                    LoggerHelper.d("memberModel.noAttendReason : " + members.noAttendReason);

                    members.attend = "false";

                    AttendModel attendModel = new AttendModel();
                    attendModel.memberName = members.name;
                    attendModel.attend = members.attend;
                    attendModel.groupUID = members.groupUID;
                    attendModel.teamUID = members.teamUID;
                    attendModel.corpsUID = members.corpsUID;
                    attendModel.isExecutives = members.isExecutives;
                    attendModel.noAttendReason = members.noAttendReason;
                    attendModel.isNew = members.isNew;
                    attendModel.memberName = members.name;

                    attendMap.put(members.name, attendModel);
                    itemArrayList.set(position, members);

                    memberRecyclerViewListener.onComplete(members, "false", v);

                }
            });
        } else if (CommonData.getViewMode() == ViewMode.ADMIN || CommonData.getAdminMode() == AdminMode.MODIFY) {
            modify(members, v);
        }
        notifyItemChanged(position);
    }

    /**
     * DELETE BUTTON CLICKED
     *
     * @param v
     * @param holder
     * @param members
     */
    private void onDeleteClicked(View v, @NonNull MemberRecyclerViewHolder holder, HolyModel.memberModel members, Integer position) {
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            if (CommonData.isTutoMode()) {
                CommonData.setIsTutoMode(false);
                MaterialDailogUtil.Companion.simpleDoneDialog(mContext,
                        "축하합니다. 튜토리얼이 완료되었습니다.", new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                PointManager.setPlusPoint(CommonData.getPersonalJoinPoint());
                            }
                        });
            }

            members.noAttendReason = "";
            try {
                if (attendMap.get(members.name).attend.equals("true")) {
                    members.attend = "false";
                } else {
                    members.attend = "true";
                }
            } catch (Exception e) {
                members.attend = "true";
            }

            AttendModel attendModel = new AttendModel();
            attendModel.memberName = members.name;
            attendModel.attend = members.attend;
            attendModel.groupUID = members.groupUID;
            attendModel.teamUID = members.teamUID;
            attendModel.corpsUID = members.corpsUID;
            attendModel.isExecutives = members.isExecutives;
            attendModel.noAttendReason = members.noAttendReason;
            attendModel.isNew = members.isNew;
            attendModel.memberName = members.name;

            attendMap.put(members.name, attendModel);
            itemArrayList.set(position, members);
            memberRecyclerViewListener.onComplete(members, members.attend, v);
        } else {
            delete(v, members);
        }
        notifyItemChanged(position);
    }

    private void delete(final View v, HolyModel.memberModel members) {
        memberRecyclerViewListener.onComplete(members, "delete", v);
    }

    private void select(HolyModel.memberModel memberModel) {
        CommonData.setMemberModel(memberModel);
        CommonData.setMemberUid(memberModel.uid);
    }

    private void modify(HolyModel.memberModel memberModel, View v) {
        LoggerHelper.d("onViewItemBtnClickedListener");
        memberRecyclerViewListener.onComplete(memberModel, "modify", v);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
