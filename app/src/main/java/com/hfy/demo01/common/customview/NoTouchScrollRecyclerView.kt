package com.hfy.demo01.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * 不可手动滑动的RecyclerView
 * @author bytedance
 * @date 2023/6/19
 * @desc
 */
class NoTouchScrollRecyclerView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    override fun dispatchTouchEvent(e: MotionEvent?): Boolean {
        return false
    }
}