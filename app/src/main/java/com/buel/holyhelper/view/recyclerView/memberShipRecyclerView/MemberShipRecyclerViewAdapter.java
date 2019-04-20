package com.buel.holyhelper.view.recyclerView.memberShipRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.model.UserModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by blue7 on 2018-06-07.
 */

public class MemberShipRecyclerViewAdapter
        extends RecyclerView.Adapter<MemberShipRecyclerViewHolder> {


    List<UserModel> itemArrayList;
    View.OnClickListener onClickListener;
    Context context;

    public List<UserModel> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(List<UserModel> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public MemberShipRecyclerViewAdapter(Context context, ArrayList<UserModel> itemArrayList, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public MemberShipRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new MemberShipRecyclerViewHolder(view);
    }

    private final ArrayList<Integer> selected = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MemberShipRecyclerViewHolder holder, final int position) {
        final UserModel userModel = itemArrayList.get(position);
        holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);

       /* if( CommonData.getGroupModel() !=null) {
            if (groupModel.name.equals(CommonData.getGroupModel().name)) {
                holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
            } else {
                holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
            }
        }*/

        if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            //holder.btn_item_delete.setVisibility(View.GONE);
            holder.btn_item_select.setVisibility(View.GONE);
        } else if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            holder.btn_item_select.setVisibility(View.GONE);
            holder.btn_item_select.setBackgroundResource(R.drawable.ic_settings_24dp);
            holder.btn_item_delete.setBackgroundResource(R.drawable.ic_delete_24dp);
            holder.btn_item_delete.setVisibility(View.VISIBLE);
            holder.btn_item_select.setVisibility(View.INVISIBLE);
            holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
        }



        if(userModel.userPhotoUri == null)
        {
            try {
                Glide.with(context)
                        .load(R.drawable.ic_account)
                        .apply(new RequestOptions().fitCenter().circleCrop())
                        .into(holder.userImageivew);
            } catch (Exception e) {
                LoggerHelper.e(e.getMessage());
            }
        }else {
            Glide.with(context)
                    .load(userModel.userPhotoUri)
                    .apply(new RequestOptions().fitCenter().circleCrop())
                    .into(holder.userImageivew);
        }
        holder.userImageivew.setVisibility(View.VISIBLE);


        try {
            if (userModel.permission.equals("ok")) {
                holder.btn_item_delete.setBackgroundResource(R.drawable.ic_done);
            } else {
                holder.btn_item_delete.setBackgroundResource(R.drawable.ic_do_not_disturb);
            }
        }catch (Exception e)
        {
            holder.btn_item_delete.setBackgroundResource(R.drawable.ic_do_not_disturb);
            userModel.permission = "no";
        }finally {
            LoggerHelper.d("userModel.permission : " + userModel.permission );
        }


        holder.tv_item_name.setText(Html.fromHtml("<Strong>" + userModel.userName + "</Strong>"));
        holder.tv_item_txt2.setText(Html.fromHtml(userModel.userTell + "\n" + userModel.userEmail));

        holder.recyclerViewItemMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewItemBtnClickedListener(v, position);
            }
        });
        holder.btn_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onDeleteBtnClickedListener(v, position);
            }
        });

        holder.btn_item_select.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                onSelectBtnClickedListener(v, position);
            }
        });
    }

    private void onDeleteBtnClickedListener(View v, int position) {
        final UserModel userModel = itemArrayList.get(position);
        setChangePermission(v, position, userModel);
    }

    /**
     * 저장하기 버튼 클릭 : 수정하기 모드일 때
     */
    private void onOkclickedModify(int position, UserModel userModel) {

        if (userModel.permission.equals("ok")) {
            userModel.permission = "no";
        }else{
            userModel.permission = "ok";
        }
        try {
            FDDatabaseHelper.INSTANCE.sendUserDataInsertUserModel(userModel.uid, userModel, task -> {
                if (userModel.permission.equals("ok")) {
                    Toast.makeText(context, "권한이 [ " + "승락" + " ] 으로 변경되었습니다. ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "권한이 [ " + "거절" + " ] 으로 변경되었습니다. ", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position);
            });
        } catch (Exception e) {
            SuperToastUtil.toast(context, e.getMessage());
        }

    }

    private void onViewItemBtnClickedListener(View v, int position) {
        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            final UserModel userModel = itemArrayList.get(position);

            setChangePermission(v, position, userModel);
        }
    }

    private void setChangePermission(View v, int position, UserModel userModel) {
        String title;

        if(userModel.permission.equals("ok"))title = "권한을 제거 하시겠습니까?";
        else title = "권한을 승인 하시겠습니까?";

        LoggerHelper.d("MemberShipRecyclerViewAdapter", "btn_item_delete");
        MaterialDailogUtil.Companion.simpleYesNoDialog( v.getContext(), "권한을 변경하시겠습니까?" , new MaterialDailogUtil.OnDialogSelectListner() {
            @Override
            public void onSelect(String s) {
                onOkclickedModify(position, userModel);
            }
        });
    }

    private void onSelectBtnClickedListener(View v, int position) {
        onClickListener.onClick(v);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

}
