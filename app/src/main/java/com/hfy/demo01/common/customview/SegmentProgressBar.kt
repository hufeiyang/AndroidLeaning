package com.hfy.demo01.common.customview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.FloatRange
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils

/**
 *
 * @author bytedance
 * @date 2023/6/14
 * @desc
 */
class SegmentProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private val TAG: String? = "SegmentProgressBar"
    private val DURATION_DEFAULT: Long = 1500

    private var mSegmentDuration: Long = DURATION_DEFAULT
    private var mSegmentSize: Int = 0
    private var mCurrentSelectedSegmentPosition: Int = 0
    private var mCurrentSelectedSegmentProgress: Float = 0f
    private var mCurrentProgressBar: ProgressBar? = null

    private var mIsAnimationMode: Boolean = true

    private var mProgressBarListener: ISegmentProgressBarListener? = null

    private val progressBarList = mutableListOf<ProgressBar>()

    private val dividerWidth = SizeUtils.dp2px(2f)

    private var animator:ValueAnimator? = null


    interface ISegmentProgressBarListener {
        fun onClickSegment(segmentSize: Int, currentPosition: Int, clickPosition: Int)
        fun onAutoSelectedSegment(segmentSize: Int, currentPosition: Int)
    }

    /**
     * 初始化
     */
    fun initialize(
        segmentSize: Int,
        segmentDuration: Long = DURATION_DEFAULT,
        autoAnimated: Boolean = false,
        progressBarListener: ISegmentProgressBarListener? = null
    ) {

        mSegmentSize = segmentSize
        mSegmentDuration = segmentDuration
        mProgressBarListener = progressBarListener

        for (i in 0 until mSegmentSize) {
            val progressBar = HorizontalProgressBar(context)
            progressBar.max = 100
            val marginLayoutParams = layoutParams as MarginLayoutParams
            val margin = marginLayoutParams.leftMargin + marginLayoutParams.rightMargin
            val progressBarWidth = (ScreenUtils.getAppScreenWidth() - margin -(dividerWidth * (mSegmentSize - 1))) / mSegmentSize
            progressBar.layoutParams = MarginLayoutParams(progressBarWidth, LayoutParams.MATCH_PARENT).also {
                if ( i != mSegmentSize){
                    it.marginEnd = dividerWidth
                }
            }
            addView(progressBar)
            progressBarList.add(progressBar)
        }
    }


    fun startAutoPlay(
        _currentPosition: Int,
        @FloatRange(from = 0.0, to = 1.0) _currProgress: Float = 0f
    ) {
        mCurrentSelectedSegmentPosition = _currentPosition
        mCurrentSelectedSegmentProgress = _currProgress
        if ((mCurrentSelectedSegmentPosition < mSegmentSize)) {
            mCurrentProgressBar = progressBarList[mCurrentSelectedSegmentPosition]
        }

        autoplay()
    }

        private fun autoplay() {

            if (animator == null) {
                animator = ObjectAnimator.ofInt((mCurrentSelectedSegmentProgress * 100).toInt(), 100)
                animator?.duration = mSegmentDuration
                animator?.interpolator = LinearInterpolator()
                animator?.addUpdateListener {
                    if (mIsAnimationMode){
                        mCurrentProgressBar?.progress = it.animatedValue as Int
                    }
                }
                animator?.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        Log.i(TAG, "onAnimationStart, position:$mCurrentSelectedSegmentPosition")

                        mCurrentSelectedSegmentProgress = 0f

                        if(mCurrentSelectedSegmentPosition == 0){
                            //清除1->mSegmentSize的进度块
                            for (i in 1 until mSegmentSize) {
                                if ((progressBarList[i].progress != 0)) {
                                    progressBarList[i].progress = 0
                                }
                            }
                        }
                        mProgressBarListener?.onAutoSelectedSegment(mSegmentSize, mCurrentSelectedSegmentPosition)

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (!mIsAnimationMode){
                            return
                        }

                        ++mCurrentSelectedSegmentPosition
                        if(mCurrentSelectedSegmentPosition >= mSegmentSize){
                            mCurrentSelectedSegmentPosition = 0
                        }
                        mCurrentProgressBar = progressBarList[mCurrentSelectedSegmentPosition]

                        Log.i(TAG, "onAnimationEnd, new position:$mCurrentSelectedSegmentPosition")

                        animator?.start()
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}

                })
            }

            animator?.start()
        }

    /**
     * 暂停当前分快的进度动画
     */
    fun pauseCurrentSegmentAnimation(){
        animator?.pause()
    }

    /**
     * 继续当前分块的进度动画
     */
    fun resumeCurrentSegmentAnimation(){
        animator?.resume()
    }


    fun getCurrentSelectedSegment(): Int {
        return mCurrentSelectedSegmentPosition
    }

    fun getCurrentSelectedSegmentProgress(): Float {
        return mCurrentSelectedSegmentProgress
    }

    /**
     * 关闭动画模式
     */
    fun closeAnimationMode(){
        if (!mIsAnimationMode) {
            Log.i(TAG, "AnimationMode has been closed.")
            return
        }
        Log.i(TAG, "closeAnimationMode.")

        mIsAnimationMode = false
        animator?.cancel()
        animator = null
        mCurrentProgressBar?.progress = mCurrentProgressBar?.max!!
        mCurrentSelectedSegmentProgress = 1f
    }


    /**
     * 设置选中的分块，0到segmentIndex的进度条都是满进度
     */
    fun setCurrentSelectedSegment(selectedPosition:Int){
        if (mIsAnimationMode){
            Log.i(TAG, "setCurrentSelectedSegment. segmentIndex:$selectedPosition，but now is AnimationMode!")
            return
        }

        Log.i(TAG, "setCurrentSelectedSegment. segmentIndex:$selectedPosition")

        if(animator?.isRunning == true){
            animator?.cancel()
        }

        mCurrentSelectedSegmentPosition = selectedPosition
        mCurrentProgressBar = progressBarList[mCurrentSelectedSegmentPosition]
        mCurrentSelectedSegmentProgress = 1f
        for (i in 0 .. selectedPosition) {
            if (progressBarList[i].progress != progressBarList[i].max){
                progressBarList[i].progress = progressBarList[i].max
            }
        }

        for (i in (selectedPosition + 1) until mSegmentSize) {
            if (progressBarList[i].progress != 0){
                progressBarList[i].progress = 0
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }
}
