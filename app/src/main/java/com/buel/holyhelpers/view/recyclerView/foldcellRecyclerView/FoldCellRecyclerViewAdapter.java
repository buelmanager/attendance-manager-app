package com.buel.holyhelpers.view.recyclerView.foldcellRecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.AdminMode;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.management.Management;
import com.buel.holyhelpers.management.MemberManager;
import com.buel.holyhelpers.model.HolyModel;
import com.buel.holyhelpers.utils.AppUtil;
import com.buel.holyhelpers.view.activity.MemberManagerViewActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by blue7 on 2018-06-07.
 */

public class FoldCellRecyclerViewAdapter
        extends RecyclerView.Adapter<FoldCellRecyclerViewHolder> {

    List<HolyModel.memberModel> itemArrayList;
    View.OnClickListener onClickListener;

    public void setItemArrayList(List<HolyModel.memberModel> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public FoldCellRecyclerViewAdapter(ArrayList<HolyModel.memberModel> itemArrayList, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public FoldCellRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        FoldingCell cell = (FoldingCell) view;

        return new FoldCellRecyclerViewHolder(cell);
    }

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

    @Override
    //뷰를 컨트롤 하는곳
    public void onBindViewHolder(@NonNull final FoldCellRecyclerViewHolder viewHolder, final int position) {
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
                                onClickListener.onClick(v);
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
            onClickListener.onClick(v);
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
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

}
