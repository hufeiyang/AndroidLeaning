package com.hfy.demo01.common.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar

/**
 * 默认横向的ProgressBar
 * @author bytedance
 * @date 8/12/22
 * @desc
 */
class HorizontalProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, android.R.attr.progressBarStyleHorizontal) {
}
