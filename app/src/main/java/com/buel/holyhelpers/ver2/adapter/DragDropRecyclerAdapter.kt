package com.buel.holyhelpers.ver2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.orhanobut.logger.LoggerHelper
import kotlinx.android.synthetic.main.recyclerview_home_v2_item.view.*
import java.util.*


class DragDropRecyclerAdapter(private val startDragListener: OnStartDragListener) :
        RecyclerView.Adapter<DragDropRecyclerAdapter.ItemViewHolder>(),
        ItemMoveCallbackListener.Listener {
    private var users = emptyList<String>().toMutableList()
    private var subRLs = emptyList<String>().toMutableList()
    fun setUsers(newUsers: List<String>) {
        users.addAll(newUsers)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)

        holder.itemView.home_v2_item_title_iv.setOnClickListener {

        }

        val containerll:LinearLayout = holder.itemView.home_v2_item_container_ll


        if ((containerll as LinearLayout).childCount > 0) return // (containerll as LinearLayout).removeAllViews()

        val icon:ImageView = holder.itemView.home_v2_item_icon_iv
        icon.setBackgroundColor( R.color.black)

        val random = Random()
        val num = random.nextInt(5)

        for(i in 1..num){
            val inflater = holder.itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val subRL:RelativeLayout = inflater.inflate(R.layout.home_v2_item_sub_title, null) as RelativeLayout
            val subTitleIv= subRL.findViewById<ImageView>(R.id.home_v2_item_sub_title_iv)
            subTitleIv.setOnClickListener {

            }
            containerll.addView(subRL)
        }

        if(num==0)
            holder.itemView.home_v2_item_bottom.visibility = View.GONE

        /*holder.itemView.home_v2_item_move_iv.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                this.startDragListener.onStartDrag(holder)
            }
            return@setOnTouchListener true
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_home_v2_item, parent, false)
        return ItemViewHolder(itemView)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(text: String) {
            itemView.home_v2_item_title.text = text
        }
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(users, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(users, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)

        LoggerHelper.e(users.toString())
    }

    override fun onRowSelected(itemViewHolder: ItemViewHolder) {
    }

    override fun onRowClear(itemViewHolder: ItemViewHolder) {
    }
}