package com.buel.holyhelpers.ver2

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.ver2.adapter.DragDropRecyclerAdapter
import com.buel.holyhelpers.ver2.adapter.ItemMoveCallbackListener
import com.buel.holyhelpers.ver2.adapter.OnStartDragListener
import com.buel.holyhelpers.view.activity.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.commonLib.Common
import com.google.android.material.appbar.AppBarLayout
import com.orhanobut.logger.LoggerHelper
import kotlinx.android.synthetic.main.activity_home_v2.*


class HomeActivityV2 : BaseActivity(), OnStartDragListener {

    lateinit var adapter: DragDropRecyclerAdapter
    lateinit var touchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_v2)

        LoggerHelper.setLogger("holyhelper", true)

        LoggerHelper.e("isTablet : "+ Common.isTablet(this))
        setToolbar()
        setRecycler()
    }

    private fun setRecycler() {

        adapter = DragDropRecyclerAdapter(this)

        val callback: ItemTouchHelper.Callback = ItemMoveCallbackListener(adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(home_recycler_view)
        populateListItem()
        home_recycler_view.layoutManager = LinearLayoutManager(this)
        home_recycler_view.adapter = adapter
    }

    private fun populateListItem() {
        val users = listOf(
                "Check Up !",
                "Detail Analytics ! ",
                "Choose Company",
                "Settings",
                "Add Member",
                "Farmod",
                "Ganesh",
                "Hemant",
                "Ishaan",
                "Jack",
                "Kamal",
                "Lalit",
                "Mona"
        )
        adapter.setUsers(users)
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "action bar"
        setSupportActionBar(toolbar)

        home_v2_collpase_bar.title = "collapseToolbar"

        Glide.with(this)
                .load(R.drawable.ic_account)
                .apply(RequestOptions().fitCenter().circleCrop())
                .into(home_v2_collpase_bar_iv)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                kotlin.math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0 -> {
                    home_v2_collpase_bar.title = "collapseToolbar"
                    home_v2_collpase_bar_ll.visibility = View.INVISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appbar.background = getDrawable(R.color.white)
                    }
                }
                verticalOffset == 0 -> {
                    home_v2_collpase_bar_ll.visibility = View.VISIBLE
                    home_v2_collpase_bar.title = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appbar.background = getDrawable(R.color.lightGray3)
                    }
                }
                else -> {
                    home_v2_collpase_bar_ll.visibility = View.VISIBLE
                    home_v2_collpase_bar.title = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appbar.background = getDrawable(R.color.lightGray3)
                    }
                }
            }
        })
    }

    //---- OVERRIDE ----

    //toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item!!.itemId) {
            R.id.search -> {
                Toast.makeText(applicationContext, "Search Click", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.option -> {
                Toast.makeText(applicationContext, "Option Click", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        touchHelper.startDrag(viewHolder!!)
    }
}
