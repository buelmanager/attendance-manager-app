package com.buel.holyhelpers.view.recyclerView.groupRecyclerListView;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buel.holyhelpers.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by blue7 on 2018-06-07.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tv_item_txt2;
    TextView tv_item_name;
    Button btn_item_delete;
    Button btn_item_select;
    RelativeLayout recyclerViewItemMain;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        tv_item_txt2 = itemView.findViewById(R.id.recycler_view_item_tv_txt2);
        tv_item_name = itemView.findViewById(R.id.recycler_view_item_tv_name);
        btn_item_delete = itemView.findViewById(R.id.recycler_view_item_btn_delete);
        btn_item_select = itemView.findViewById(R.id.recycler_view_item_btn_select);
        recyclerViewItemMain = itemView.findViewById(R.id.recycler_view_item_rl_main);
    }


}
