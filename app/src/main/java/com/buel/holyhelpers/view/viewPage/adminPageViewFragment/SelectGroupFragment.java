package com.buel.holyhelpers.view.viewPage.adminPageViewFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.data.FDDatabaseHelper;
import com.buel.holyhelpers.model.HolyModel;
import com.buel.holyhelpers.utils.SortMapUtil;
import com.buel.holyhelpers.view.SimpleListener;
import com.buel.holyhelpers.view.recyclerView.groupRecyclerListView.RecyclerViewAdapter;
import com.buel.holyhelpers.view.recyclerView.groupRecyclerListView.RecyclerViewHolder;
import com.buel.holyhelpers.view.recyclerView.teamRecyclerListView.TeamRecyclerViewActivity;
import com.commonLib.SuperToastUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by blue7 on 2018-05-16.
 */
public class SelectGroupFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerViewHolder> holderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LoggerHelper.d("onCreateView", "onCreateView");

        View rootView = inflater.inflate(R.layout.corps_step2_view_page_fragment, container, false);
        setPage(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        LoggerHelper.d("onResume", "onResume");
        super.onResume();
    }


    @SuppressLint("LongLogTag")
    private void setPage(View rootView) {

        LoggerHelper.d("setPage");
        recyclerView = rootView.findViewById(R.id.corps_step2_view_page_fragment_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<HolyModel.groupModel> groups = new ArrayList<>();

        try {
            groups = (ArrayList<HolyModel.groupModel>) SortMapUtil.getSortGroupList(CommonData.getHolyModel().group);
        } catch (Exception e) {
            SuperToastUtil.toastE(this.getContext(), "부서를 추가해주세요.");
            /*tvDesc.setText("여기를 클릭하여 그룹을 추가해주세요.");
            SuperToastUtil.toastE(this, "그룹을 추가해주세요.");
            tvDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(FoldCellRecyclerViewActivity.this, GroupManagerViewActivity.class));
                    finish();
                }
            });*/
            return;
        }

        holderAdapter = new RecyclerViewAdapter(groups, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.recycler_view_item_btn_delete) {
                    FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            holderAdapter.notifyDataSetChanged();
                            HashMap<String, HolyModel.groupModel> groupMap =
                                    (HashMap<String, HolyModel.groupModel>) CommonData.getHolyModel().group;
                            try {
                                List<HolyModel.groupModel> groupList = SortMapUtil.getSortGroupList(groupMap);
                                ((RecyclerViewAdapter) holderAdapter).setItemArrayList(groupList);
                                recyclerView.refreshDrawableState();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "그룹리스트가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (v.getId() == R.id.recycler_view_item_btn_select) {
                    FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            startActivity(new Intent(getContext(), TeamRecyclerViewActivity.class));
                        }
                    });
                }
            }
        });

        recyclerView.setAdapter(holderAdapter);
    }

    public void refreshFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
