package com.buel.holyhelpers.view.recyclerView.memberRecyclerListView;

import android.view.View;
import com.buel.holyhelpers.model.HolyModel;

public interface MemberRecyclerViewListener {
    public interface OnCompleteListener {
        public void onComplete(HolyModel.memberModel members, String value, View view);
    }
}
