package com.buel.holyhelper.view.recyclerView.memberRecyclerListView;

import android.view.View;
import com.buel.holyhelper.model.HolyModel;

public interface MemberRecyclerViewListener {
    public interface OnCompleteListener {
        public void onComplete(HolyModel.memberModel members, String value, View view);
    }
}
