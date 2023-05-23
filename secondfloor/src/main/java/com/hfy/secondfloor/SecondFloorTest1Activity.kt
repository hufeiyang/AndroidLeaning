package com.hfy.secondfloor

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.hfy.secondfloor.adapter.BaseRecyclerAdapter
import com.hfy.secondfloor.adapter.SmartViewHolder
import com.hfy.secondfloor.util.StatusBarUtil
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.header.TwoLevelHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener

/**
 *
 */
class SecondFloorTest1Activity : AppCompatActivity() {

    companion object {
        fun launch(activity: Activity) {
            val intent = Intent(activity, SecondFloorTest1Activity::class.java)
            activity.startActivity(intent)
        }
    }

    private var cunrrentTwoLevelHeaderState: RefreshState? = null
    lateinit var toolbar: Toolbar
    lateinit var twoLevelHeader: TwoLevelHeader
    lateinit var ivSecondFloor:View
    lateinit var refreshLayout: RefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_floor)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        ivSecondFloor = findViewById(R.id.iv_second_floor)
        twoLevelHeader = findViewById(R.id.twoLevelHeader)
        refreshLayout = findViewById(R.id.refreshLayout)


        //一楼列表
        initContentList()
        //一楼列表
        initSecondFloorList()

        //主动打开二楼
        toolbar.setOnClickListener { twoLevelHeader.openTwoLevel(true) }
        twoLevelHeader.setOnTwoLevelListener {
            Toast.makeText(this@SecondFloorTest1Activity, "触发二楼事件", Toast.LENGTH_SHORT).show()
            //二楼内容变可见
            findViewById<View>(R.id.second_floor_content).animate().alpha(1f).duration = 2000
            //true 将会展开二楼状态 false 关闭刷新
            true
        }


        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setMargin(this, findViewById(R.id.classicsHeader))
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.toolbar))
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.rv_content_list))
    }

    private fun initSecondFloorList() {

        //二楼引导图 先移到上面去
        ivSecondFloor.post {
            ivSecondFloor.translationY = Math.min(0 - ivSecondFloor.height + toolbar.height,
                refreshLayout.layout.height - ivSecondFloor.height).toFloat()
        }

        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.rv_second_floor_list)
        if (recyclerView != null) {
            recyclerView.isNestedScrollingEnabled = false
            val secondFloorDataList = mutableListOf<Void?>()
            for (i in 0..20) {
                secondFloorDataList.add(null)
            }
            recyclerView.adapter = object :
                BaseRecyclerAdapter<Void?>(
                    secondFloorDataList,
                    android.R.layout.simple_list_item_2
                ) {
                override fun onBindViewHolder(
                    holder: SmartViewHolder,
                    model: Void?,
                    position: Int
                ) {
                    holder.text(
                        android.R.id.text1,
                        getString(R.string.item_example_number_title, position)
                    )
                    holder.text(
                        android.R.id.text2,
                        getString(R.string.item_example_number_abstract_second_floor, position)
                    )
                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant)
                }
            }
        }
    }


    private fun initContentList() {
        val rVContent: RecyclerView = findViewById(R.id.rv_content_list)
        rVContent.isNestedScrollingEnabled = false

        val firstFloorDataList = mutableListOf<String>()
        for (i in 0..20){
            firstFloorDataList.add("一楼数据：$i")
        }
        val adapter = object : BaseRecyclerAdapter<String>(firstFloorDataList, android.R.layout.simple_list_item_2) {
            override fun onBindViewHolder(
                holder: SmartViewHolder,
                model: String,
                position: Int
            ) {
                holder.text(android.R.id.text1, position.toString())
                holder.text(android.R.id.text2, model)
                holder.textColorId(android.R.id.text2, R.color.colorTextAssistant)
            }
        }
        rVContent.adapter = adapter

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                refreshLayout.finishLoadMore(1000)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                Toast.makeText(this@SecondFloorTest1Activity, "触发刷新事件", Toast.LENGTH_SHORT).show()
                val firstFloorDataList = mutableListOf<String>()
                for (i in 0..20){
                    firstFloorDataList.add("一楼刷新的数据：$i")
                }
                adapter.refresh(firstFloorDataList)
                refreshLayout.finishRefresh(1000)
            }

            override fun onHeaderMoving(
                header: RefreshHeader,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int
            ) {
                toolbar.alpha = 1 - Math.min(percent, 1f)
                ivSecondFloor.translationY = Math.min(offset - ivSecondFloor.height + toolbar.height,
                    refreshLayout.layout.height - ivSecondFloor.height).toFloat()
            }

            override fun onStateChanged(
                refreshLayout: RefreshLayout,
                oldState: RefreshState,
                newState: RefreshState
            ) {
                if (oldState == RefreshState.TwoLevel) {
                    findViewById<View>(R.id.second_floor_content).animate().alpha(0f).setDuration(1000)
                }
                cunrrentTwoLevelHeaderState = newState
            }
        })

//        refreshLayout.setOnRefreshListener { refreshLayout ->
//            Toast.makeText(this@SecondFloorTest1Activity, "触发刷新事件", Toast.LENGTH_SHORT).show()
//            refreshLayout.finishRefresh(2000)
//        }
    }

    override fun onBackPressed() {
        if (cunrrentTwoLevelHeaderState?.isTwoLevel == true) {
            twoLevelHeader.finishTwoLevel();
            findViewById<View>(R.id.second_floor_content).animate().alpha(0f).setDuration(1000)
            return
        }

        super.onBackPressed()
    }
}