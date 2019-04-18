package com.buel.holyhelper.view.recyclerView.briefingRecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buel.holyhelper.R;
import com.buel.holyhelper.model.AttendModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by blue7 on 2018-06-07.
 */

public class BriefingRecyclerViewAdapter
        extends RecyclerView.Adapter<BriefingRecyclerViewHolder>{


    List<AttendModel> itemArrayList;
    View.OnClickListener onClickListener;

    Map<String, String > attendMap;
    public Map<String, String> getAttendMap() {
        return attendMap;
    }
    public void setAttendMap(Map<String, String> attendMap) {
        this.attendMap = attendMap;
    }

    public List<AttendModel> getItemArrayList() {
        return itemArrayList;
    }
    public void setItemArrayList(List<AttendModel> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public BriefingRecyclerViewAdapter(ArrayList<AttendModel> itemArrayList , View.OnClickListener onClickListener ) {
        this.onClickListener = onClickListener;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public BriefingRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        return new BriefingRecyclerViewHolder(view);
    }

    private final ArrayList<Integer> selected = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final BriefingRecyclerViewHolder holder, final int position) {
        final AttendModel attendModel = itemArrayList.get(position);
        holder.tv_item_name.setText(attendModel.memberName + " " + attendModel.attend);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

}
