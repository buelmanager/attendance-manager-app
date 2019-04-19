package com.buel.holyhelper.view.recyclerView.memberRecyclerListView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.github.zagum.switchicon.SwitchIconView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by blue7 on 2018-06-07.
 */
public class MemberRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView price;
    ImageView btn_item_delete;
    ImageView btn_item_select;
    ImageView modify_iv;
    ImageView delete_iv;
    ImageView image_view;
    RelativeLayout call_rl;
    ImageView call_btn;
    ImageView content_avatar;
    View attend_back_veiw;
    TextView contentRequestBtn;
    TextView pledgePrice;
    TextView fromAddress;
    TextView toAddress;
    TextView requestsCount;
    TextView titleWeight;
    TextView content_name_view;
    TextView head_image_right_text;
    TextView head_image_center_text;
    TextView head_image_left_text;
    TextView content_from_address_1;
    TextView content_to_address_1;
    TextView content_from_address_2;
    TextView content_to_address_2;
    TextView content_delivery_time;
    TextView content_deadline_time;
    TextView content_delivery_date;
    TextView content_avatar_title;
    TextView content_request_btn;
    TextView modefy_btn;
    TextView content_delivery_time3;
    TextView content_delivery_time2;
    TextView content_deadline_time4;
    TextView content_delivery_date_badge2;
    TextView content_delivery_time1;
    TextView content_delivery_date1;
    TextView content_delivery_date3;
    TextView content_name_view2;
    TextView content_delivery_date2;
    TextView btn_item_select_text;
    TextView button1_text;
    TextView date;
    TextView time;
    TextView delete_btn;
    View itemView;
    SwitchIconView switchIcon1;
    View button1;
    LinearLayout fd_cell_main_ll;
    RelativeLayout back_color_rl;

    public MemberRecyclerViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        back_color_rl = itemView.findViewById(R.id.back_color_rl);
        button1_text = itemView.findViewById(R.id.button1_text);
        btn_item_select_text = itemView.findViewById(R.id.btn_item_select_text);
        btn_item_select = itemView.findViewById(R.id.btn_item_select);
        btn_item_delete = itemView.findViewById(R.id.btn_item_delete);
        switchIcon1 = itemView.findViewById(R.id.switchIcon1);
        button1 = itemView.findViewById(R.id.button1);
        image_view = itemView.findViewById(R.id.image_view);
        content_avatar = itemView.findViewById(R.id.content_avatar);
        titleWeight = itemView.findViewById(R.id.title_weight);
        price = itemView.findViewById(R.id.title_price);
        time = itemView.findViewById(R.id.title_time_label);
        date = itemView.findViewById(R.id.title_date_label);
        fromAddress = itemView.findViewById(R.id.title_from_address);
        toAddress = itemView.findViewById(R.id.title_to_address);
        requestsCount = itemView.findViewById(R.id.title_requests_count);
        pledgePrice = itemView.findViewById(R.id.title_pledge);
        contentRequestBtn = itemView.findViewById(R.id.content_request_btn);
        modefy_btn = itemView.findViewById(R.id.modefy_btn);
        delete_btn = itemView.findViewById(R.id.delete_btn);
        call_rl = itemView.findViewById(R.id.call_rl);
        attend_back_veiw = itemView.findViewById(R.id.attend_back_veiw);
        content_delivery_date2 = itemView.findViewById(R.id.content_delivery_date2);
        content_deadline_time4 = itemView.findViewById(R.id.content_deadline_time4);
        content_delivery_time2 = itemView.findViewById(R.id.content_delivery_time2);
        content_delivery_date1 = itemView.findViewById(R.id.content_delivery_date1);
        content_delivery_time1 = itemView.findViewById(R.id.content_delivery_time1);
        content_delivery_time3 = itemView.findViewById(R.id.content_delivery_time3);
        content_delivery_date3 = itemView.findViewById(R.id.content_delivery_date3);
        content_name_view = itemView.findViewById(R.id.content_name_view);
        head_image_right_text = itemView.findViewById(R.id.head_image_right_text);
        head_image_right_text = itemView.findViewById(R.id.head_image_right_text);
        head_image_center_text = itemView.findViewById(R.id.head_image_center_text);
        head_image_left_text = itemView.findViewById(R.id.head_image_left_text);
        content_from_address_1 = itemView.findViewById(R.id.content_from_address_1);
        content_to_address_1 = itemView.findViewById(R.id.content_to_address_1);
        content_from_address_2 = itemView.findViewById(R.id.content_from_address_2);
        content_to_address_2 = itemView.findViewById(R.id.content_to_address_2);
        content_delivery_time = itemView.findViewById(R.id.content_delivery_time);
        content_deadline_time = itemView.findViewById(R.id.content_deadline_time);
        content_avatar_title = itemView.findViewById(R.id.content_avatar_title);
        content_delivery_date = itemView.findViewById(R.id.content_delivery_date);
        content_request_btn = itemView.findViewById(R.id.content_request_btn);
        content_delivery_date_badge2 = itemView.findViewById(R.id.content_delivery_date_badge2);
        content_name_view2 = itemView.findViewById(R.id.content_name_view2);
        fd_cell_main_ll = itemView.findViewById(R.id.fd_cell_main_ll);
        modify_iv = itemView.findViewById(R.id.modify_iv);
        delete_iv = itemView.findViewById(R.id.delete_iv);
        call_btn = itemView.findViewById(R.id.call_btn);
    }
}
