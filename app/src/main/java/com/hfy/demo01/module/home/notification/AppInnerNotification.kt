package com.hfy.demo01.module.home.notification

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.blankj.utilcode.util.ReflectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.hfy.demo01.R
import com.hfy.simpleimageloader.ImageLoader


/**
 *
 * @author bytedance
 * @date 2023/5/24
 * @desc
 */
class AppInnerNotification(
    val context: Context,
    val title: String?,
    val content: String?,
    val iconUrl: String?,
    val buttonText: String?,
    val openUrl: String?,
    val isDark: Boolean?
) : View.OnTouchListener{

    val TAG:String = "AppInnerNotification"

    private var windowManager: WindowManager? = null

    private var mToast: Toast? = null

    private val CANCEL_NOTIFICATION_EVENT = 1000

    private val mShowDuration: Int = 3

    private var mNeedDismiss: Boolean = false

    var mView:View? = null

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == CANCEL_NOTIFICATION_EVENT) {
                dismiss()
            }
        }
    }

    fun show() {
        mView = View.inflate(context, R.layout.v_app_inner_notificaiton, null)
        if (mView == null) {
            return
        }
        mHandler.post {
            try {
                //1.准备好view
                initView()
                //2.使用准备好的view创建端内通知
                buildNotification()
                //3.展示通知
                animShow()
                //4.x秒后自动消失
                mHandler.sendEmptyMessageDelayed(
                    CANCEL_NOTIFICATION_EVENT,
                    (mShowDuration * 1000).toLong()
                )
            } catch (e: Throwable) {
                e.message?.let { Log.e(TAG, it) }
            }
        }
    }

    private fun dismiss() {
        if (mToast != null) {
            mToast?.cancel()
        }else{
            animDismiss()
        }
    }


    @SuppressLint("SoonBlockedPrivateApi")
    private fun buildNotification() {


//        //先尝试使用toast，需要设置toast为可点击->反射mTN修改window param的flag—>但由于Android9及以上mTN为隐藏无法反射
//        val isGetTNSuccess: Boolean = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
//            true
//        }else{
//            try {
//                Toast::class.java.getDeclaredField("mTN")
//                true
//            }catch (e: Throwable) {
//                Log.d(TAG, "initToast: reflection failure")
//                false
//            }
//        }
//
//        Log.i(TAG, "buildNotification: isGetTNSuccess:$isGetTNSuccess")
//        if (isGetTNSuccess) {
//            //成功，就使用Toast方案
//            initToast()
//            return
//        }

        //由于Android10及以上无法修改Toast为可点击，所以还是使用添加window方案

        //使用添加window方案，type是非系统window
        initWindowManager()
    }

    private fun initView() {
        val ivIcon = mView?.findViewById<ImageView>(R.id.iv_icon)
        val tvTitle = mView?.findViewById<TextView>(R.id.tv_title)
        val tvContent = mView?.findViewById<TextView>(R.id.tv_content)
        val button = mView?.findViewById<Button>(R.id.btn_click)
        val ivClose = mView?.findViewById<ImageView>(R.id.iv_close)

        tvTitle?.text = title
        tvContent?.text = content
        button?.text = buttonText
        iconUrl?.isNotEmpty().let {
            ImageLoader.with(context).loadBitmapAsync(iconUrl, ivIcon)
        }

        //是否dark
        if (isDark == true) {
            mView?.isSelected = true
        }

        //有无openUrl
        if (openUrl.isNullOrEmpty()) {
            ivClose?.visibility = View.VISIBLE
            button?.visibility = View.GONE
            ivClose?.setOnClickListener { dismiss() }
        }

        val dp10 = SizeUtils.dp2px(10f)
        var paddingLeft = dp10
        var paddingRight = dp10
        //有无icon时
        if (iconUrl.isNullOrEmpty()) {
            ivIcon?.visibility = View.GONE
            paddingLeft = SizeUtils.dp2px(4f)
        }

        //是否横屏时
        if (ScreenUtils.isLandscape()) {
            paddingRight = SizeUtils.dp2px(12f)
        }
        mView?.setPadding(paddingLeft, dp10, paddingRight, dp10)

        //点击按钮
        button?.setOnClickListener{
            if (mNeedDismiss) {
                Log.d(TAG, "onClick: mNeedDismiss is true so banner has been dismiss,not jump")
                return@setOnClickListener
            }
            dismiss()
            jump()
        }
        mNeedDismiss = false
        mView?.setOnTouchListener(this)
    }


    private fun initToast() {
        mToast = Toast(context)
        mToast?.view = mView
        mToast?.duration = Toast.LENGTH_LONG

        //通过反射设置Toast可点击
        try {
            val mToastReflect = ReflectUtils.reflect(mToast)
            val mTN:Any = mToastReflect.field("mTN").get()

            val mTNReflect = ReflectUtils.reflect(mTN)
            val params: Any = mTNReflect.field("mParams").get()

            if (params is WindowManager.LayoutParams) {
                setLayoutParams(params)
                params.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
//                params.windowAnimations = R.style.ClickToast

                mTNReflect.field("mParams", params)
                mToastReflect.field("mTN", mTN)
                mToast?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, params.y)
            }
        } catch (e: Throwable) {
            Log.d(TAG, "initToast: reflection failure")
        }

        mToast?.show()
    }

    private fun initWindowManager() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val params: WindowManager.LayoutParams = WindowManager.LayoutParams()
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        params.format = PixelFormat.TRANSLUCENT

        setLayoutParams(params)

        windowManager?.addView(mView, params)
    }

    private fun setLayoutParams(params: WindowManager.LayoutParams) {
        // 设置通知栏的长和宽
        var marginTopValue = 32f
        var marginHorizontalValue = 14f
        //是否横屏时
        if (ScreenUtils.isLandscape()) {
            marginTopValue = 8f
            marginHorizontalValue = 156f
        }
        val marginTop: Int = SizeUtils.dp2px(marginTopValue)
        val marginHorizontal: Int = SizeUtils.dp2px(marginHorizontalValue)

        params.width = ScreenUtils.getScreenWidth() - marginHorizontal * 2
        params.height = SizeUtils.dp2px(68f)
        params.y = marginTop
        params.alpha = 1f
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL

        //设置必须触摸通知栏才可以关掉
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_FULLSCREEN
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
    }

    /**
     * 动画，从顶部弹出
     */
    private fun animShow() {
        //使用动画从顶部弹出
        mView?.let {
            val animator: ObjectAnimator = ObjectAnimator.ofFloat(mView, "translationY", -SizeUtils.dp2px(90f).toFloat(), mView!!.getTranslationY())
            animator.duration = 400
            animator.start()
        }

    }

    /**
     * 动画，从顶部收回
     */
    private fun animDismiss() {
        if (mView == null || mView?.parent == null) {
            return
        }
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(mView, "translationY", mView!!.translationY,  -SizeUtils.dp2px(90f).toFloat())
        animator.duration = 400
        animator.start()
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (null != mView && null != mView?.parent) {
                    windowManager?.removeViewImmediate(mView)
                    mView = null
                }
            }
        })
    }

    private fun jump() {
        // TODO: 跳转目标页面
        //        VRouter
        ToastUtils.showShort("跳转目标页面")
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        var y1 = 0f
        var y2 = 0f
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.action == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            y1 = event.y
        }
        if (event.action == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            y2 = event.y
            if (y1 - y2 > 30) {
                Log.d(TAG, "监听到上划")
                mNeedDismiss = true
            }
            if (mNeedDismiss) {
                Log.d(TAG, "监听到滑动，消除弹窗")
                dismiss()
            }
        }
        return false
    }



    class Builder(val context: Context){

        private var mTitle:String? = null
        private var mContent:String? = null
        private var mIconUrl:String? = null
        private var mButtonText:String? = null
        private var mOpenUrl:String? = null
        private var mIsDark:Boolean? = null

        fun setTitle(title: String?): Builder {
            mTitle = title
            return this
        }

        fun setContent(content: String?): Builder {
            mContent = content
            return this
        }

        fun setIconUrl(iconUrl: String?): Builder {
            mIconUrl = iconUrl
            return this
        }

        fun setButtonText(buttonText: String?): Builder {
            mButtonText = buttonText
            return this
        }

        fun setOpenUrl(openUrl: String?): Builder {
            mOpenUrl = openUrl
            return this
        }

        fun setDark(isDark: Boolean): Builder {
            mIsDark = isDark
            return this
        }

        fun build():AppInnerNotification {
            return AppInnerNotification(context, mTitle, mContent, mIconUrl, mButtonText, mOpenUrl, mIsDark)
        }
    }
    
}