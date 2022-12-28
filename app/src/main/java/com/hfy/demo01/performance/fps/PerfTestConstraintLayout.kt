package com.hfy.demo01.performance.fps

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.hfy.demo01.R
import java.util.*

/**
 *
 * @author bytedance
 * @date 2022/12/12
 * @desc
 */
class PerfTestConstraintLayout: ConstraintLayout{

    object mPaint : Paint()


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.setWillNotDraw(false)

        mPaint.color = Color.parseColor("#FF0000")
        mPaint.style = Paint.Style.FILL_AND_STROKE

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    /**
     * 众所周知，当我们自定义一个View时会重写他的3个方法，onMeasure(),onLayout(),onDraw()方法，
     * 但是自定义一个ViewGroup的时候要重写onMeasure(),onLayout(),dispatchDraw()这3个方法
     * setWillNotDraw(false)，才会走draw(canvas: Canvas?)，否则直接走了draw(Canvas canvas, ViewGroup parent, long drawingTime)（通过ViewGroup.drawChild()）
     * canvas.saveLayer，很耗费性能，注意不要使用！
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun draw(canvas: Canvas) {

        super.draw(canvas)

//        for (i in 1..50){
//            canvas.drawCircle(i*20.toFloat(), 10.toFloat(),20f, mPaint)
//        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
    }
}