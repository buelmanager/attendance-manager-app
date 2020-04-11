package com.buel.holyhelpers.ver2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buel.holyhelpers.R
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.recyclerview_item.view.*


/**
 * R.layout.fragment_title_content에 해당하는 ADAPTER
 */
class HomeRecyclerviewAdapter(val items: ArrayList<String>?, val onCompleteListener: (String) -> Unit) :
        androidx.recyclerview.widget.RecyclerView.Adapter<HomeRecyclerviewAdapter.RecyclerViewHoler>() {

    private lateinit var mOnCom: OnCompleteListener<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHoler {

        return RecyclerViewHoler(itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false))
    }

    fun setItem(items: ArrayList<String>) {

    }

    override fun getItemCount(): Int = items!!.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHoler, position: Int) {
        items!![position].let { item ->
            with(holder) {


            }
        }
    }

    class RecyclerViewHoler(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.recycler_view_imageivew_profile
    }
}
