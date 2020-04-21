package com.buel.holyhelpers.view.recyclerView.groupRecyclerListView;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.AdminMode;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.data.ViewMode;
import com.buel.holyhelpers.management.GroupManager;
import com.buel.holyhelpers.management.Management;
import com.buel.holyhelpers.model.HolyModel;
import com.buel.holyhelpers.utils.SharedPreferenceUtil;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by blue7 on 2018-06-07.
 */

public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewHolder> {


    List<HolyModel.groupModel> itemArrayList;
    View.OnClickListener onClickListener;

    public List<HolyModel.groupModel> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(List<HolyModel.groupModel> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public RecyclerViewAdapter(ArrayList<HolyModel.groupModel> itemArrayList, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    private final ArrayList<Integer> selected = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        final HolyModel.groupModel group = itemArrayList.get(position);
        holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);

        if (CommonData.getGroupModel() != null) {
            if (group.name.equals(CommonData.getGroupModel().name)) {
                holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
            } else {
                holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
            }
        }

        if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            holder.btn_item_delete.setVisibility(View.GONE);
            holder.btn_item_select.setVisibility(View.GONE);
        } else if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            holder.btn_item_select.setBackgroundResource(R.drawable.ic_settings_24dp);
            holder.btn_item_delete.setBackgroundResource(R.drawable.ic_delete_24dp);
            holder.btn_item_delete.setVisibility(View.VISIBLE);
            holder.btn_item_select.setVisibility(View.VISIBLE);
            holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
        }

        holder.tv_item_name.setText(Html.fromHtml("<Strong>" + group.name + "</Strong>"));
        holder.tv_item_txt2.setText("");

        if (group.leader != null)
            holder.tv_item_txt2.setText(Html.fromHtml(group.leader));

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
        final HolyModel.groupModel group = itemArrayList.get(position);
        LoggerHelper.d("MemberShipRecyclerViewAdapter", "btn_item_delete");
        MaterialDailogUtil.Companion.simpleYesNoDialog(v.getContext(), new MaterialDailogUtil.OnDialogSelectListner() {
            @Override
            public void onSelect(String s) {
                Management groupManager = new GroupManager();
                groupManager.delete(group, new Management.OnCompleteListener() {
                    @Override
                    public void onComplete(Object data) {
                        SharedPreferenceUtil.initModelData();
                        CommonData.setCurGroupModel(null);
                        CommonData.setCurTeamModel(null);
                        if (onClickListener != null) onClickListener.onClick(v);
                    }
                });
            }
        });
    }

    private void onViewItemBtnClickedListener(View v, int position) {
        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            LoggerHelper.d("MemberShipRecyclerViewAdapter", "btn_item_select");
            HolyModel.groupModel groupModel = itemArrayList.get(position);
            CommonData.setCurTeamModel(null);
            CommonData.setCurGroupModel(groupModel);
            notifyDataSetChanged();
            onClickListener.onClick(v);
        }
    }

    private void onSelectBtnClickedListener(View v, int position) {
        HolyModel.groupModel groupModel = itemArrayList.get(position);
        CommonData.setSelectedGroup(groupModel);
        onClickListener.onClick(v);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

}
