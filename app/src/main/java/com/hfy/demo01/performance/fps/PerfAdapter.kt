package com.hfy.demo01.performance.fps

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 *
 * @author bytedance
 * @date 2022/12/12
 * @desc
 */
class PerfAdapter(layoutId:Int): BaseQuickAdapter<PerfBean, PerfItemViewHolder>(layoutId) {
    override fun convert(holder: PerfItemViewHolder, item: PerfBean) {
        holder.binding?.tvPerfName?.text = item.name

//        holder.binding?.root?.postDelayed({
//            holder.binding?.tvPerfName?.text = "我变了"
//        },3000)

        holder.binding?.root?.context?.let {
            holder.binding?.ivPerfIcon?.let { it1 ->
                Glide.with(it)
                    .load("https://tenfei03.cfp.cn/creative/vcg/800/new/VCG41N1408270968.jpg")
//                    .load("https://blog.hootsuite.com/wp-content/uploads/2022/06/How-to-Make-a-GIF-1.gif")
                    .into(it1)

            }

        }
    }
}