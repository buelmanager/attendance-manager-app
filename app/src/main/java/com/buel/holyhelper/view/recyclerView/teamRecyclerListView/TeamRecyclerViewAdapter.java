package com.buel.holyhelper.view.recyclerView.teamRecyclerListView;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.management.Management;
import com.buel.holyhelper.management.TeamManager;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.SharedPreferenceUtil;
import com.buel.holyhelper.utils.SortMapUtil;
import com.commonLib.MaterialDailogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by blue7 on 2018-06-07.
 */

public class TeamRecyclerViewAdapter
        extends RecyclerView.Adapter<TeamRecyclerViewHolder> {

    List<HolyModel.groupModel.teamModel> itemArrayList;
    View.OnClickListener onClickListener;


    public TeamRecyclerViewAdapter(ArrayList<HolyModel.groupModel.teamModel> itemArrayList,
                                   View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.itemArrayList = itemArrayList;
    }

    public void setItemArrayList(List<HolyModel.groupModel.teamModel> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public TeamRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new TeamRecyclerViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TeamRecyclerViewHolder holder, final int position) {

        final HolyModel.groupModel.teamModel team = itemArrayList.get(position);
        holder.recyclerViewItemMain.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);

        if (CommonData.getTeamModel() != null) {
            if (team.name.equals(CommonData.getTeamModel().name)) {
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

        holder.btn_item_delete.setOnClickListener(v -> onDeleteBtnClickedListener(v, position));
        holder.recyclerViewItemMain.setOnClickListener(v -> onViewItemBtnClickedListener(v, position));
        holder.btn_item_select.setOnClickListener(v -> onSelectBtnClickedListener(v, position));
        holder.tv_item_name.setText(Html.fromHtml("<Strong>" + SortMapUtil.getInteger(team.name).toString() + "</Strong>"));

        if (team.leader == null) team.leader = "";
        if (team.etc == null) team.etc = "";

        holder.tv_item_txt2.setText(Html.fromHtml(team.leader + "  " + team.etc));

    }


    private void onDeleteBtnClickedListener(View v, int position) {
        final HolyModel.groupModel.teamModel team = itemArrayList.get(position);
        MaterialDailogUtil.Companion.simpleYesNoDialog(v.getContext(), s -> {
            Management teamManager = new TeamManager();
            teamManager.delete(team, data -> {
                notifyDataSetChanged();
                SharedPreferenceUtil.initTeamModel();
                CommonData.setTeamModel(null);
                if (onClickListener != null) onClickListener.onClick(v);
            });
        });
    }

    private void onViewItemBtnClickedListener(View v, int position) {
        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            HolyModel.groupModel.teamModel teamModel = itemArrayList.get(position);
            CommonData.setTeamModel(teamModel);
            notifyDataSetChanged();
            onClickListener.onClick(v);
        }
    }

    private void onSelectBtnClickedListener(View v, int position) {
        HolyModel.groupModel.teamModel teamModel = itemArrayList.get(position);
        CommonData.setSelectedTeam(teamModel);
        notifyDataSetChanged();
        onClickListener.onClick(v);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
