package com.hfy.demo01.performance.fps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hfy.demo01.R
import com.hfy.demo01.databinding.ActivityPerformanceLearningBinding

class PerformanceLearningActivity : AppCompatActivity() {

    val TAG: String = "PerformanceLearning"
    var binding:ActivityPerformanceLearningBinding? = null

    companion object {
        @JvmStatic
        fun launch(activity: FragmentActivity) {
            Log.i("PerformanceLearning", "launch: ")

            val intent = Intent(activity, PerformanceLearningActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_performance_learning)

        initView()
    }

    private fun initView() {
        binding?.rvPerformanceLearning?.adapter = PerfAdapter(R.layout.item_perf_learning).also {
            val list = mutableListOf<PerfBean>()
            for (i in 1..200){
                list.add(PerfBean("PerfItem-$i"))
            }
            it.setList(list)
        }
        binding?.rvPerformanceLearning?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

}