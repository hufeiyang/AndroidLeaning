package com.hfy.demo01.common.customview

import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.hfy.demo01.R
import com.tencent.open.log.c.o

/**
 *
 * @author bytedance
 * @date 2023/6/26
 * @desc
 */
class AppendViewAfterTextView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ViewTreeObserver.OnGlobalLayoutListener {


    private var tv: TextView? = null
    private var tvSpecial: TextView? = null
    private var text: String? = null
    private var paramsSpecial: LayoutParams? = null
    private var space = 0

    init {
        init()
    }

    private fun init() {
        space = SizeUtils.dp2px(5F)
        tv = TextView(context)
//        tv?.maxLines = 3
//        tv?.ellipsize = TextUtils.TruncateAt.END
        tv?.setTextSize(30F)
//        tv?.setLineSpacing(0f, 1.2f)
//        tv?.setIncludeFontPadding(false)

        tvSpecial = TextView(context)
        tvSpecial!!.setBackgroundResource(R.drawable.bg_stroke_ffd300_round)
        tvSpecial!!.textSize = 30f
        tvSpecial?.text = "查看原文"
        tvSpecial!!.setSingleLine()
        tvSpecial!!.setTextColor(Color.GREEN)
        paramsSpecial = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tvSpecial!!.layoutParams = paramsSpecial
        addView(tv, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        addView(tvSpecial)
    }

    fun setText(text: String?) {
        tv?.let { text?.let { it1 ->
            this.text = text
            tv?.text = this.text
            tv?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        } }
    }

    override fun onGlobalLayout() {
        tv?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
//        //截取文案展示
        text = text?.let {
            getSubString(tv!!, it, 2)
        }

        tv?.text = text

        tv?.viewTreeObserver?.addOnGlobalLayoutListener {
            //为保证TextView.getLayout()!=null，在这里再执行相关逻辑
            setMoreViewPosition()
            //记得移除，不然会一直回调
            tv?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        }
    }

    fun getSubString(tv: TextView, content: String, maxLine: Int): String {
        val contentWidth = tv.paint.measureText(content) //原始文案总长度
        val tvWidth: Float = tv.measuredWidth.toFloat()//TextView宽
        val radio = (tvWidth - (tvSpecial?.measuredWidth!!+space) * 1.5f)/tvWidth.toFloat() //TextView中最后一行的最大使用宽度（要保证tvSpecial放得下）(加了1.2的扩大)
        val realContentLines = contentWidth / tvWidth  //文案总行数
        val needShowMaxLines = maxLine + radio  //给定的展示行数
        return if (realContentLines > needShowMaxLines) { //文案总行数 大于 给定的展示行数，则需要截取
            val endIndex = content.length * (needShowMaxLines/ realContentLines) //获取要截取的位置
            content.substring(0, endIndex.toInt()) + "…"
        } else content
    }

    private fun setMoreViewPosition() {
        val layout: Layout = tv?.layout ?: return
        val lineCount: Int = layout.lineCount
        val lineWidth: Float = layout.getLineWidth(lineCount - 1)

        //获取最后行最后一个字符的下标
        val lineEnd: Int = layout.getLineEnd(lineCount - 1)

        var isTvSpecialNeedAtNextLine = false
        if (lineWidth + tvSpecial!!.measuredWidth + space - (width - paddingRight - paddingLeft) > 0) {
            isTvSpecialNeedAtNextLine = true
        }
        val lineH: Int = layout.height / layout.lineCount
        val lastLineRight = layout.getSecondaryHorizontal(lineEnd).toInt()
        paramsSpecial!!.leftMargin = lastLineRight + space
        //tvSpecial的中间和tv最后一行的中间上下对齐
        paramsSpecial!!.topMargin = layout.height - tv?.paddingBottom!! - lineH / 2 - tvSpecial!!.height / 2
        if (isTvSpecialNeedAtNextLine){////最后一行显示不下tvSpecial，将tvSpecial换行
            paramsSpecial!!.leftMargin = 0
            paramsSpecial!!.topMargin = layout.height
        }
        tvSpecial!!.layoutParams = paramsSpecial
    }

}