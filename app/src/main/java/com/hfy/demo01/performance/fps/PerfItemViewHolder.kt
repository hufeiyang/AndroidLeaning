package com.hfy.demo01.performance.fps

import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hfy.demo01.databinding.ItemPerfLearningBinding

class PerfItemViewHolder(view:View): BaseViewHolder(view) {
    var binding: ItemPerfLearningBinding? = null
    init {
        binding = DataBindingUtil.bind(view)
    }

}
