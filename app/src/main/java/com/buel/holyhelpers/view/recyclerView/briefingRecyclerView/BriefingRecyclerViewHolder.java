package com.buel.holyhelpers.view.recyclerView.briefingRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.buel.holyhelpers.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by blue7 on 2018-06-07.
 */

public class BriefingRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tv_item_name;
    Button btn_item_delete;
    Button btn_item_select;

    public BriefingRecyclerViewHolder(View itemView) {
        super(itemView);
        tv_item_name = itemView.findViewById(R.id.recycler_view_item_tv_name);
        btn_item_delete = itemView.findViewById(R.id.recycler_view_item_btn_delete);
        btn_item_select = itemView.findViewById(R.id.recycler_view_item_btn_select);
    }
}
